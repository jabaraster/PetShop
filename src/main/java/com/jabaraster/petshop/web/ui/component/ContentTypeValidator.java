/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.general.ArgUtil;

import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author jabaraster
 */
public class ContentTypeValidator implements IValidator<List<FileUpload>> {
    private static final long serialVersionUID = 1549380846302412025L;

    private final String      type;
    private final String      typeInJapanese;

    /**
     * @param pType -
     * @param pTypeInJapanese -
     */
    public ContentTypeValidator(final String pType, final String pTypeInJapanese) {
        ArgUtil.checkNullOrEmpty(pType, "pType"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pTypeInJapanese, "pTypeInJapanese"); //$NON-NLS-1$
        this.type = pType;
        this.typeInJapanese = pTypeInJapanese;
    }

    /**
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void validate(final IValidatable<List<FileUpload>> pValidatable) {
        final List<FileUpload> uploads = pValidatable.getValue();
        if (uploads == null || uploads.isEmpty()) {
            return;
        }
        final FileUpload upload = uploads.get(0);
        if (upload.getContentType().startsWith(this.type)) {
            // OK
            return;
        }
        final ValidationError error = new ValidationError(this, "type"); //$NON-NLS-1$
        error.setVariable("contentType", upload.getContentType()); //$NON-NLS-1$
        error.setVariable("typeInJapanese", this.typeInJapanese); //$NON-NLS-1$
        error.setVariable("fileName", upload.getClientFileName()); //$NON-NLS-1$
        pValidatable.error(error);
    }

    /**
     * @param pType -
     * @param pTypeInJapanese -
     * @return -
     */
    public static ContentTypeValidator type(final String pType, final String pTypeInJapanese) {
        ArgUtil.checkNullOrEmpty(pType, "pType"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pTypeInJapanese, "pTypeInJapanese"); //$NON-NLS-1$
        return new ContentTypeValidator(pType, pTypeInJapanese);
    }
}
