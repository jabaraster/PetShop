/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jabaraster
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EUser extends EntityBase<EUser> {
    private static final long  serialVersionUID               = 5322511553248558567L;

    /**
     * 
     */
    public static final String DEFAULT_ADMINISTRATOR_USER_ID  = "admin";             //$NON-NLS-1$
    /**
     * 
     */
    public static final String DEFAULT_ADMINISTRATOR_PASSWORD = "admin";             //$NON-NLS-1$

    /**
     * 
     */
    public static final int    MAX_CHAR_COUNT_USER_ID         = 50;

    /**
     * 
     */
    @Column(nullable = false, unique = true, length = MAX_CHAR_COUNT_USER_ID)
    @NotNull
    @Size(min = 1, max = MAX_CHAR_COUNT_USER_ID)
    protected String           userId;

    /**
     * 
     */
    @Column(nullable = false)
    protected boolean          administrator;
}
