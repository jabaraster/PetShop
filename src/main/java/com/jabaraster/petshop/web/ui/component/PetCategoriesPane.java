/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.NullAjaxCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.service.IPetCategoryService;

/**
 * @author jabaraster
 */
public class PetCategoriesPane extends Panel {
    private static final long       serialVersionUID = 4529870251453600680L;

    @Inject
    IPetCategoryService             petCategoryService;

    private IAjaxCallback           onSelect         = NullAjaxCallback.GLOBAL;

    private List<CategoryCheck>     categoriesValue;

    private Form<?>                 form;
    private ListView<CategoryCheck> categories;
    private AjaxButton              submitter;

    /**
     * @param pId -
     */
    public PetCategoriesPane(final String pId) {
        super(pId);
        this.add(getForm());
    }

    /**
     * @return -
     */
    public List<EPetCategory> getSelectedCategories() {
        final List<EPetCategory> ret = new ArrayList<>();
        for (final CategoryCheck cc : getCategoriesValue()) {
            if (cc.isChecked()) {
                ret.add(cc.getCategory());
            }
        }
        return ret;
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        if (getApplication().getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
            pResponse.render(ComponentJavaScriptHeaderItem.forType(PetCategoriesPane.class));
        } else {
            pResponse.render(ComponentJavaScriptHeaderItem.minimizedForType(PetCategoriesPane.class));
        }
    }

    /**
     * @param pOnSelect onSelectを設定.
     */
    public void setOnSelect(final IAjaxCallback pOnSelect) {
        this.onSelect = pOnSelect == null ? NullAjaxCallback.GLOBAL : pOnSelect;
    }

    @SuppressWarnings("serial")
    private ListView<CategoryCheck> getCategories() {
        if (this.categories == null) {
            this.categories = new ListView<CategoryCheck>("categories", getCategoriesValue()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<CategoryCheck> pItem) {
                    final CategoryCheck check = pItem.getModelObject();
                    final CheckBox category = new CheckBox("category", new PropertyModel<Boolean>(check, "checked")); //$NON-NLS-1$ //$NON-NLS-2$
                    final Label label = new Label("label", check.getCategory().getName()); //$NON-NLS-1$
                    pItem.add(category);
                    pItem.add(label);
                }
            };
        }
        return this.categories;
    }

    private List<CategoryCheck> getCategoriesValue() {
        if (this.categoriesValue == null) {
            this.categoriesValue = new ArrayList<>();
            for (final EPetCategory catetory : this.petCategoryService.getAll()) {
                this.categoriesValue.add(new CategoryCheck(catetory));
            }
        }
        return this.categoriesValue;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getCategories());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    @SuppressWarnings("serial")
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    PetCategoriesPane.this.onSelect.call(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private static class CategoryCheck implements Serializable {
        private static final long  serialVersionUID = -7929078478615352601L;

        private boolean            checked          = true;
        private final EPetCategory category;

        CategoryCheck(final EPetCategory pCategory) {
            this.category = pCategory;
        }

        /**
         * @return categoryを返す.
         */
        public EPetCategory getCategory() {
            return this.category;
        }

        /**
         * @return checkedを返す.
         */
        public boolean isChecked() {
            return this.checked;
        }

        /**
         * @param pChecked checkedを設定.
         */
        @SuppressWarnings("unused")
        public void setChecked(final boolean pChecked) {
            this.checked = pChecked;
        }

    }
}
