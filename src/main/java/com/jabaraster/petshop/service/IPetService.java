/**
 * 
 */
package com.jabaraster.petshop.service;

import jabara.general.NotFound;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.service.impl.PetServiceImpl;

/**
 * @author jabaraster
 */
@ImplementedBy(PetServiceImpl.class)
public interface IPetService {

    /**
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    EPet findById(final long pId) throws NotFound;

    /**
     * @param pPet -
     */
    void register(final EPet pPet);

    /**
     * @param pPet -
     * @param pNewCategoryName -
     */
    void register(final EPet pPet, final String pNewCategoryName);

    /**
     * @param pFirst -
     * @param pCount -
     * @param pSortProperty -
     * @param pAscending -
     * @return -
     */
    List<EPet> fetch(final int pFirst, final int pCount, final String pSortProperty, final boolean pAscending);

    /**
     * @return -
     */
    long countAll();
}
