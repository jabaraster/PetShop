package com.jabaraster.petshop.service;

import jabara.general.NotFound;
import jabara.general.Sort;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.model.Duplicate;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.model.UnmatchPassword;
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

    /**
     * @param pLoginUser -
     * @param pDeleteTargetUser -
     * @return -
     */
    boolean enableDelete(final LoginUser pLoginUser, final EUser pDeleteTargetUser);

    /**
     * @param pUser -
     */
    void delete(final EUser pUser);

    /**
     * @param pUser -
     * @param pPassword -
     * @throws Duplicate -
     */
    void insert(final EUser pUser, final String pPassword) throws Duplicate;

    /**
     * @param pFirst -
     * @param pCount -
     * @param pSort -
     * @return -
     */
    List<EUser> get(final long pFirst, final long pCount, final Sort pSort);

    /**
     * @return -
     */
    long countAll();

    /**
     * @param pUser -
     * @throws Duplicate -
     */
    void update(final EUser pUser) throws Duplicate;

    /**
     * @param pUser -
     * @param pCurrentPassword -
     * @param pNewPassword -
     * @throws UnmatchPassword -
     */
    void updatePassword(final EUser pUser, final String pCurrentPassword, final String pNewPassword) throws UnmatchPassword;
}
