/**
 * 
 */
package com.jabaraster.petshop.service;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.ECart;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.service.impl.CartServiceImpl;

/**
 * @author jabaraster
 */
@ImplementedBy(CartServiceImpl.class)
public interface ICartService {

    /**
     * @param pUser -
     * @param pPet -
     */
    void addOrder(final LoginUser pUser, final EPet pPet);

    /**
     * @param pUserId -
     * @return -
     */
    ECart findByUserId(final long pUserId);

}
