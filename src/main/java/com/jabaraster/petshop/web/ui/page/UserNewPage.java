/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.wicket.Models;

import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class UserNewPage extends UserEditPageBase {
    private static final long serialVersionUID = 8316834293476226340L;

    /**
     * 
     */
    public UserNewPage() {
        super();
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ユーザ新規追加"); //$NON-NLS-1$
    }
}
