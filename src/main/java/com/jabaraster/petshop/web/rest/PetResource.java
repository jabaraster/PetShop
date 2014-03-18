/**
 * 
 */
package com.jabaraster.petshop.web.rest;

import jabara.general.ArgUtil;
import jabara.jpa.entity.EntityBase_;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.service.IPetService;

/**
 * @author jabaraster
 */
@Path("pet")
public class PetResource {

    /**
     * 
     */
    public static final String DEFAULT_PET_LIST_FIRST = "0";  //$NON-NLS-1$
    /**
     * 
     */
    public static final String DEFAULT_PET_LIST_COUNT = "100"; //$NON-NLS-1$

    private final IPetService  petService;

    /**
     * @param pPetService -
     */
    @Inject
    public PetResource(final IPetService pPetService) {
        this.petService = ArgUtil.checkNull(pPetService, "pPetService"); //$NON-NLS-1$
    }

    /**
     * @param pFirst -
     * @param pCount -
     * @return -
     */
    @Path("index")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<EPet> getPets( //
            @QueryParam("first") @DefaultValue(DEFAULT_PET_LIST_FIRST) final int pFirst //
            , @QueryParam("count") @DefaultValue(DEFAULT_PET_LIST_COUNT) final int pCount //
    ) {
        return this.petService.fetch(pFirst, pCount, null, EntityBase_.id.getName(), true);
    }
}
