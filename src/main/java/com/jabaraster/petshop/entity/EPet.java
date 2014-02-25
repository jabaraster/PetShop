/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.bean.BeanProperties;
import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jabaraster
 */
@Getter
@Setter()
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EPet extends EntityBase<EPet> {
    private static final long           serialVersionUID    = 2833344844245761649L;

    /**
     * 
     */
    public static final int             MAX_CHAR_COUNT_NAME = 50;

    private static final BeanProperties _meta               = BeanProperties.getInstance(EPet.class);

    /**
     * 
     */
    @NotNull
    @Size(min = 1, max = MAX_CHAR_COUNT_NAME)
    @Column(nullable = false, length = MAX_CHAR_COUNT_NAME * 3)
    protected String                    name;

    /**
     * 
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    protected EPetCategory              category;

    /**
     * 
     */
    protected int                       unitPrice;

    /**
     * @return -
     */
    public static BeanProperties getMeta() {
        return _meta;
    }
}
