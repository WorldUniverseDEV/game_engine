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
 
@Path("/api")
public class Api {

    @EJB
    private ParameterBO parameterBO;

    @EJB
    private CarDAO carDAO;

    @GET
    @Path("/getCarByID")
    @Produces(MediaType.APPLICATION_XML)
    public CarEntity getCarByID(@QueryParam("carId") Long carID, @QueryParam("adminAuth") String token) {
        String adminToken = parameterBO.getStrParam("ADMIN_AUTH");

        if (adminToken == null) {
            return new CarEntity();
        }

        if (!adminToken.equals(token)) {
            return new CarEntity();
        }

        return carDAO.find(carID);
    }
}
 