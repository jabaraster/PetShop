/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;
import jabara.general.io.IReadableData;
import jabara.jpa.entity.EntityBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

/**
 * @author jabaraster
 */
@NoArgsConstructor
@Entity
public class EPetImageData extends EntityBase<EPetImageData> {
    private static final long serialVersionUID            = -5237376425396393851L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_CONTENT_TYPE = 100;

    /**
     * 
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    private EPet              pet;

    /**
     * 
     */
    @Lob
    @Column(nullable = false)
    protected byte[]          data;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_CONTENT_TYPE)
    protected String          contentType;

    /**
     * @param pPet -
     * @param pData -
     */
    public EPetImageData(final EPet pPet, final IReadableData pData) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$
        this.pet = pPet;

        this.contentType = pData.getContentType();
        this.data = toByteArray(pData.getInputStream());
    }

    /**
     * @return contentTypeを返す.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * @return -
     */
    public InputStream getData() {
        return new ByteArrayInputStream(this.data.clone());
    }

    @SuppressWarnings("resource")
    private static byte[] toByteArray(final InputStream pData) {
        if (pData == null) {
            return Empty.BYTE_ARRAY;
        }
        final ByteArrayOutputStream mem = new ByteArrayOutputStream();
        try {
            final byte[] buf = new byte[4096];
            final InputStream in = IoUtil.toBuffered(pData);
            for (int d = in.read(buf); d != -1; d = in.read(buf)) {
                mem.write(buf, 0, d);
            }
            return mem.toByteArray();

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        } finally {
            IoUtil.close(mem);
        }
    }
}
