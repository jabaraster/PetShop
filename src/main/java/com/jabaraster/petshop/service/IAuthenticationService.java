/**
 * 
 */
package com.jabaraster.petshop.service;

import com.jabaraster.petshop.model.FailAuthentication;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.service.impl.AuthenticationServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(AuthenticationServiceImpl.class)
public interface IAuthenticationService {

    /**
     * @param pUserId
     * @param pPassword
     * @return -
     * @throws FailAuthentication
     */
    LoginUser login(String pUserId, String pPassword) throws FailAuthentication;
}
