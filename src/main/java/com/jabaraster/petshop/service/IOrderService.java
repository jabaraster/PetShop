/**
 * 
 */
package com.jabaraster.petshop.service;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.service.impl.OrderServiceImpl;

/**
 * @author jabaraster
 */
@ImplementedBy(OrderServiceImpl.class)
public interface IOrderService {

    /**
     * @param pUserId -
     * @param pPet -
     */
    void addOrder(final long pUserId, final EPet pPet);

    /**
     * @param pUserId -
     * @return -
     */
    List<EOrder> findByUserId(final long pUserId);

    /**
     * @param pOrder -
     */
    void removeOrder(final EOrder pOrder);

}
