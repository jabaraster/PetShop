package com.jabaraster.petshop.web.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import com.jabaraster.petshop.service.IUserService;

/**
 *
 */
@Path("user")
public class UserResource {

    private final IUserService userService;

    /**
     * @param pUserService -
     */
    @Inject
    public UserResource(final IUserService pUserService) {
        this.userService = pUserService;
    }
}
