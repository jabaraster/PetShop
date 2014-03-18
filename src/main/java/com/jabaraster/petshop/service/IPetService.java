/**
 * 
 */
package com.jabaraster.petshop.service;

import jabara.general.NotFound;
import jabara.general.io.DataOperation;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPetImageData;
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
     * @param pImageDataOperation -
     */
    void register(final EPet pPet, final DataOperation pImageDataOperation);

    /**
     * @param pPet -
     * @param pNewCategoryName -
     * @param pImageDataOperation -
     */
    void register(final EPet pPet, final String pNewCategoryName, final DataOperation pImageDataOperation);

    /**
     * @param pFirst -
     * @param pCount -
     * @param pCategories null及び空リストを指定した場合、カテゴリでの絞り込みを行わない.
     * @param pSortProperty -
     * @param pAscending -
     * @return -
     */
    List<EPet> fetch(final long pFirst, final long pCount, final List<EPetCategory> pCategories, final String pSortProperty, final boolean pAscending);

    /**
     * @return -
     */
    long countAll();

    /**
     * @param pPet -
     * @return -
     * @throws NotFound -
     */
    EPetImageData findImageDataByPet(final EPet pPet) throws NotFound;
}
