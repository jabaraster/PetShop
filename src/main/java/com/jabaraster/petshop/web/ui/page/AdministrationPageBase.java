/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public abstract class AdministrationPageBase extends RestrictedPageBase {
    private static final long serialVersionUID = -5395202175042343970L;

    /**
     * 
     */
    public AdministrationPageBase() {
        super();
    }

    /**
     * @param pParameters
     */
    public AdministrationPageBase(final PageParameters pParameters) {
        super(pParameters);
    }

}
