/**
 * 
 */
package com.jabaraster.petshop.service;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.ECart;
import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.service.impl.CartServiceImpl;

/**
 * @author jabaraster
 */
@ImplementedBy(CartServiceImpl.class)
public interface ICartService {

    /**
     * @param pUserId -
     * @param pPet -
     */
    void addOrder(final long pUserId, final EPet pPet);

    /**
     * @param pUserId -
     * @return -
     */
    ECart findByUserId(final long pUserId);

    /**
     * @param pUserId -
     * @param pOrder -
     */
    void removeOrder(final long pUserId, final EOrder pOrder);

}
