package com.jabaraster.petshop.service;

import jabara.general.Sort;

import java.util.List;

import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.service.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * 
 */
@ImplementedBy(UserServiceImpl.class)
public interface IUserService {

    /**
     * @param pSort ソート条件.
     * @return 全件.
     */
    List<EUser> getAll(Sort pSort);

    /**
     * 
     */
    void insertAdministratorIfNotExists();
}
