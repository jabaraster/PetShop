/**
 * 
 */
package com.jabaraster.petshop.web.rest;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.entity.EntityBase_;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetImageData;
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
     * @param pPetImageDataId
     * @param pHeaders @
     * @return -
     */
    @Path("images/{petImageDataId}")
    @GET
    public Response getImage( //
            @PathParam("petImageDataId") final long pPetImageDataId //
            , @Context final HttpHeaders pHeaders //
    ) {
        try {
            final List<String> ifNoneMatch = pHeaders.getRequestHeader(HttpHeaders.IF_NONE_MATCH);
            if (ifNoneMatch == null || ifNoneMatch.isEmpty()) { // isEmpty()はチェックしなくても結果は同じなのだが、DBを見に行く回数を減らすためにチェックした方がよい.
                return createImageDataResponseWithEtag(pPetImageDataId);
            }

            // ETag は quoted-string と定義されているので、前後を「"」で囲む必要があるらしい.
            // http://www.studyinghttp.net/cgi-bin/rfc.cgi?2616#Sec3.11
            // http://www.machu.jp/diary/20060826.html#p01
            final String hash = "\"" + this.petService.findImageDataHashByImageDataId(pPetImageDataId) + "\""; //$NON-NLS-1$//$NON-NLS-2$
            if (ifNoneMatch.contains(hash)) {
                return Response.notModified().build();
            }

            return createImageDataResponseWithEtag(pPetImageDataId);

        } catch (final NotFound e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
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

    private Response createImageDataResponseWithEtag(final long pPetImageDataId) throws NotFound {
        final EPetImageData imageData = this.petService.findImageDataByPetImageDataId(pPetImageDataId);
        final StreamingOutput entity = new StreamingOutput() {
            @Override
            public void write(final OutputStream pOutput) throws IOException, WebApplicationException {
                pOutput.write(imageData.getData());
            }
        };
        return Response.ok(entity) //
                .type(imageData.getContentType()) //
                .tag(imageData.getHash()) //
                .build();
    }
}
