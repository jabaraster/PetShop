/**
 * 
 */
package com.jabaraster.petshop.service;

import jabara.general.NotFound;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.service.impl.PetCategoryServiceImpl;

/**
 * @author jabaraster
 */
@ImplementedBy(PetCategoryServiceImpl.class)
public interface IPetCategoryService {

    /**
     * @return -
     */
    List<EPetCategory> getAll();

    /**
     * @param pCategory -
     */
    void insert(final EPetCategory pCategory);

    /**
     * @param pName -
     * @return -
     * @throws NotFound -
     */
    EPetCategory findByName(final String pName) throws NotFound;

}
