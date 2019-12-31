/*
 * This file is part of the Soapbox Race World core source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Copyright (c) 2019.
 */

package com.soapboxrace.core.api;

import com.soapboxrace.core.api.util.Secured;
import com.soapboxrace.core.bo.SocialRelationshipBO;
import com.soapboxrace.core.bo.TokenSessionBO;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/removefriend")
public class RemoveFriend {
    @EJB
    private TokenSessionBO tokenSessionBO;

    @EJB
    private SocialRelationshipBO socialRelationshipBO;

    @GET
    @Secured
    public Response removefriend(@QueryParam("friendPersonaId") Long friendPersonaId,
                                 @HeaderParam("securityToken") String securityToken) {
        return Response.ok().entity(socialRelationshipBO.removeFriend(tokenSessionBO.getActivePersonaId(securityToken),
                friendPersonaId)).build();
    }
}