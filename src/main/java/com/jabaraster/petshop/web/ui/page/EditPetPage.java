/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.general.NotFound;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.FileUploadPanel;
import jabara.wicket.Models;
import jabara.wicket.ValidatorUtil;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPetCategory_;
import com.jabaraster.petshop.entity.EPet_;
import com.jabaraster.petshop.service.IPetCategoryService;
import com.jabaraster.petshop.service.IPetService;
import com.jabaraster.petshop.web.ui.component.ContentTypeValidator;
import com.jabaraster.petshop.web.ui.component.RangeField;

/**
 * @author jabaraster
 */
public class EditPetPage extends AdministrationPageBase {
    private static final long         serialVersionUID = 4199480329927173656L;

    @Inject
    IPetService                       petService;
    @Inject
    IPetCategoryService               petCategoryService;

    /**
     * 
     */
    protected final EPet              pet;

    private final Handler             handler          = new Handler();

    private Form<?>                   form;
    private FeedbackPanel             feedback;
    private TextField<String>         name;
    private RadioChoice<EPetCategory> category;
    private TextField<String>         newName;
    private RangeField<Integer>       unitPrice;
    private FileUploadPanel           petImage;
    private AjaxButton                submitter;

    /**
     *
     */
    public EditPetPage() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    public EditPetPage(final PageParameters pParameters) {
        ArgUtil.checkNull(pParameters, "pParameters"); //$NON-NLS-1$

        final StringValue p = pParameters.get(0);
        if (p.isEmpty() || p.isNull()) {
            this.pet = new EPet();
        } else {
            try {
                final long petId = p.toLong();
                this.pet = this.petService.findById(petId);
            } catch (NotFound | NumberFormatException e) {
                throw new RestartResponseException(getApplication().getHomePage());
            }
        }

        this.add(getForm());
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ペット情報"); //$NON-NLS-1$
    }

    private RadioChoice<EPetCategory> getCategory() {
        if (this.category == null) {
            final PropertyModel<EPetCategory> model = new PropertyModel<>(this.pet, EPet_.category.getName());
            final List<EPetCategory> categories = this.petCategoryService.getAll();
            final IChoiceRenderer<EPetCategory> renderer = new ChoiceRenderer<>(EPetCategory_.name.getName(), EntityBase_.id.getName());
            this.category = new RadioChoice<>("category", model, categories, renderer); //$NON-NLS-1$
        }
        return this.category;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
        }
        return this.feedback;
    }

    @SuppressWarnings("serial")
    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getName());
            this.form.add(getCategory());
            this.form.add(getUnitPrice());
            this.form.add(getNewCategory());
            this.form.add(getPetImage());
            this.form.add(getSubmitter());

            this.form.add(new AbstractFormValidator() {

                @Override
                public FormComponent<?>[] getDependentFormComponents() {
                    return new FormComponent<?>[] { getCategory(), getNewCategory() };
                }

                @Override
                public void validate(final Form<?> pForm) {
                    if (isBlank(getCategory().getValue()) && isBlank(getNewCategory().getValue())) {
                        this.error(getCategory(), "categoryIsEmpty"); //$NON-NLS-1$
                    }
                }
            });
        }
        return this.form;
    }

    private TextField<String> getName() {
        if (this.name == null) {
            this.name = new TextField<>("name", new PropertyModel<String>(this.pet, EPet_.name.getName())); //$NON-NLS-1$
            ValidatorUtil.setSimpleStringValidator(this.name, EPet.class, EPet_.name);
        }
        return this.name;
    }

    private TextField<String> getNewCategory() {
        if (this.newName == null) {
            this.newName = new TextField<>("newCategory", Models.of(Empty.STRING)); //$NON-NLS-1$
        }
        return this.newName;
    }

    @SuppressWarnings("serial")
    private FileUploadPanel getPetImage() {
        if (this.petImage == null) {
            this.petImage = new FileUploadPanel("petImage") { //$NON-NLS-1$

                @Override
                protected void onUploadError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    EditPetPage.this.handler.onPetImageUploadError(pTarget);
                }

                @Override
                protected void onUploadSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    EditPetPage.this.handler.onPetImageUploadSubmit(pTarget);
                }
            };
            this.petImage.getFile().add(ContentTypeValidator.type("image", "画像")); //$NON-NLS-1$//$NON-NLS-2$
        }
        return this.petImage;
    }

    @SuppressWarnings("serial")
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    EditPetPage.this.handler.onError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    EditPetPage.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.submitter;
    }

    @SuppressWarnings("boxing")
    private RangeField<Integer> getUnitPrice() {
        if (this.unitPrice == null) {
            final IModel<Integer> value = new PropertyModel<>(this.pet, EPet_.unitPrice.getName());
            final IModel<Integer> min = Models.readOnly(0);
            final IModel<Integer> max = Models.readOnly(Integer.MAX_VALUE);
            final IModel<Integer> step = Models.readOnly(100);
            this.unitPrice = new RangeField<>("unitPrice", Integer.class, value, min, max, step); //$NON-NLS-1$
        }
        return this.unitPrice;
    }

    private static boolean isBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }

    @SuppressWarnings("serial")
    private class Handler implements Serializable {

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender();

        void onError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getFeedback());
            pTarget.add(getName());
        }

        void onPetImageUploadError(final AjaxRequestTarget pTarget) {
            pTarget.add(getFeedback());
        }

        void onPetImageUploadSubmit(final AjaxRequestTarget pTarget) {
            // TODO Auto-generated method stub

        }

        void onSubmit(final AjaxRequestTarget pTarget) {
            final String newCategoryName = getNewCategory().getModelObject();
            if (isBlank(newCategoryName)) {
                EditPetPage.this.petService.register(EditPetPage.this.pet);
            } else {
                EditPetPage.this.petService.register(EditPetPage.this.pet, newCategoryName);
            }
        }
    }

}
