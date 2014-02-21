/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.wicket.Models;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.resource.TextTemplateResourceReference;

/**
 * @author jabaraster
 */
public class BodyCssHeaderItem extends CssReferenceHeaderItem {

    /**
     * 
     */
    public BodyCssHeaderItem() {
        super(buildBodyCssReference(), null, null, null);
    }

    /**
     * @return -
     */
    public static BodyCssHeaderItem get() {
        return new BodyCssHeaderItem();
    }

    private static TextTemplateResourceReference buildBodyCssReference() {
        final Map<String, Object> params = new HashMap<>();
        final Request request = RequestCycle.get().getRequest();
        params.put("bodyBackground", request.getContextPath() + request.getFilterPath() + "/back"); //$NON-NLS-1$ //$NON-NLS-2$
        return new TextTemplateResourceReference(BodyCssHeaderItem.class, "Body.css", "text/css", Models.readOnly(params)); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
