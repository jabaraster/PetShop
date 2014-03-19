/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author jabaraster
 */
public class ValidationReferences {

    private static final CssResourceReference        REF_VALIDATION_ENGINE_CSS        = new CssResourceReference(ValidationReferences.class,
                                                                                              "validationEngine/css/validationEngine.jquery.css");               //$NON-NLS-1$
    private static final JavaScriptResourceReference REF_VALIDATION_ENGINE_JS         = new JavaScriptResourceReference(ValidationReferences.class,
                                                                                              "validationEngine/js/jquery.validationEngine.js");                 //$NON-NLS-1$
    private static final JavaScriptResourceReference REF_VALIDATION_ENGINE_MESSAGE_JS = new JavaScriptResourceReference(ValidationReferences.class,
                                                                                              "validationEngine/js/languages/jquery.validationEngine-ja.min.js"); //$NON-NLS-1$

    /**
     * @param pResponse -
     */
    public static void render(final IHeaderResponse pResponse) {
        pResponse.render(CssHeaderItem.forReference(REF_VALIDATION_ENGINE_CSS));
        // pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ValidationReferences.class,
        // "validationEngine/js/jquery-1.7.2.min.js")));
        pResponse.render(JavaScriptHeaderItem.forReference(REF_VALIDATION_ENGINE_JS));
        pResponse.render(JavaScriptHeaderItem.forReference(REF_VALIDATION_ENGINE_MESSAGE_JS));
    }
}
