/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.general.NotFound;
import jabara.wicket.Models;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.service.IPetService;

/**
 * @author jabaraster
 */
public class PetDeletePage extends AdministrationPageBase {
    private static final long serialVersionUID = 8856686771913768639L;

    private final Handler     handler          = new Handler();

    @Inject
    IPetService               petService;

    /**
     * 
     */
    protected final EPet      pet;

    private Form<?>           form;
    private AjaxButton        deleter;

    /**
     * @param pParameters -
     */
    public PetDeletePage(final PageParameters pParameters) {
        super(pParameters);
        final StringValue p = pParameters.get(0);
        if (p.isEmpty() || p.isNull()) {
            throw new RestartResponseException(getApplication().getHomePage());
        }
        try {
            final long petId = p.toLong();
            this.pet = this.petService.findById(petId);
        } catch (NotFound | NumberFormatException e) {
            throw new RestartResponseException(getApplication().getHomePage());
        }
        this.add(getForm());
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ペット情報の削除"); //$NON-NLS-1$
    }

    @SuppressWarnings("serial")
    private AjaxButton getDeleter() {
        if (this.deleter == null) {
            this.deleter = new IndicatingAjaxButton("deleter") { //$NON-NLS-1$
                @SuppressWarnings("unused")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, final Form<?> pForm) {
                    PetDeletePage.this.handler.onDelete();
                }
            };
        }
        return this.deleter;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getDeleter());
        }
        return this.form;
    }

    /**
     * @param pPetId -
     * @return -
     */
    public static PageParameters createParmater(final long pPetId) {
        final PageParameters ret = new PageParameters();
        ret.set(0, String.valueOf(pPetId));
        return ret;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 9208504747037807245L;

        void onDelete() {
            PetDeletePage.this.petService.delete(PetDeletePage.this.pet);
            setResponsePage(getApplication().getHomePage());
        }
    }
}
