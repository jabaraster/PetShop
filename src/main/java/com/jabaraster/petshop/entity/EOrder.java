/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class EOrder extends EntityBase<EOrder> {
    private static final long serialVersionUID = -5957688491190428354L;

    /**
     * 
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    protected EPet            pet;

    /**
     * 
     */
    @Column(nullable = false)
    protected int             quantity;

    /**
     * @param pPet -
     */
    public EOrder(final EPet pPet) {
        this(pPet, 1);
    }

    /**
     * 
     */
    public void incrementQuantity() {
        this.quantity++;
    }
}
