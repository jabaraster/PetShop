/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jabaraster
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EPetCategory extends EntityBase<EPetCategory> {
    private static final long serialVersionUID    = 2877654125512099003L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_NAME = 30;

    /**
     * 
     */
    @NotNull
    @Column(nullable = false, length = MAX_CHAR_COUNT_NAME * 3, unique = true)
    protected String          name;

}
