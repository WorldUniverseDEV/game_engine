package com.soapboxrace.core.api;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.CarDAO;
import com.soapboxrace.core.jpa.CarEntity;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/api")
public class Api {

    @EJB
    private ParameterBO parameterBO;

    @EJB
    private CarDAO carDAO;

    @GET
    @Path("/getCarByID")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCarByID(@QueryParam("carId") Long carID, @QueryParam("adminAuth") String token) {
        String adminToken = parameterBO.getStrParam("ADMIN_AUTH");

        if (adminToken == null) {
            return Response.ok().entity("Missing ADMIN_AUTH").build();
        }

        if (!adminToken.equals(token)) {
            return Response.ok().entity("Invalid ADMIN_AUTH").build();
        }

        if(carID == null) {
            return Response.ok().entity("Missing carId param").build();
        }

        return Response.ok().entity(carDAO.find(carID)).build();
    }
}
 