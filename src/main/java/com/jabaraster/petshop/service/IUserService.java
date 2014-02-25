package com.jabaraster.petshop.service;

import jabara.general.NotFound;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.service.impl.UserServiceImpl;

/**
 * 
 */
@ImplementedBy(UserServiceImpl.class)
public interface IUserService {
    /**
     * 
     */
    void insertAdministratorIfNotExists();

    /**
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    EUser findById(final long pId) throws NotFound;
}
