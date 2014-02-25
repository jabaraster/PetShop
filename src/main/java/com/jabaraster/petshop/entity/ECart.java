/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jabaraster
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ECart extends EntityBase<ECart> {
    private static final long serialVersionUID = 94486105819285325L;

    /**
     * 
     */
    @Getter
    @Setter
    @JoinColumn(nullable = false, unique = true)
    protected EUser           user;

    /**
     * 
     */
    @Getter
    @OneToMany
    protected List<EOrder>    orders           = new ArrayList<>();

    /**
     * @param pUser -
     */
    public ECart(final EUser pUser) {
        this.user = ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
    }

    /**
     * @param pOrder -
     */
    public void addOrder(final EOrder pOrder) {
        ArgUtil.checkNull(pOrder, "pOrder"); //$NON-NLS-1$
        this.orders.add(pOrder);

    }

    /**
     * @param pPet -
     * @return -
     * @throws NotFound -
     */
    public EOrder findOrderByPet(final EPet pPet) throws NotFound {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$

        for (final EOrder order : this.orders) {
            if (order.getPet().equals(pPet)) {
                return order;
            }
        }
        throw NotFound.GLOBAL;
    }
}
