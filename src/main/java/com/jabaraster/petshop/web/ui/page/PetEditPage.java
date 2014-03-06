/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public class PetEditPage extends PetEditPageBase {
    private static final long serialVersionUID = 2252927418866949902L;

    /**
     * @param pParameters -
     */
    public PetEditPage(final PageParameters pParameters) {
        super(pParameters);
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
}
