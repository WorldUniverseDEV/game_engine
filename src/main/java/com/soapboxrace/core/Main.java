package com.soapboxrace.core;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.annotation.PostConstruct;
import com.soapboxrace.core.bo.ParameterBO;

@Startup
@Singleton
public class Main {
    @EJB
    private ParameterBO parameterBO;

    @PostConstruct
    public void init() {
        System.setProperty("user.timezone", parameterBO.getStrParam("SBRWR_TIMEZONE", "Europe/Paris"));
        System.out.println("Using timezone: " + parameterBO.getStrParam("SBRWR_TIMEZONE", "Europe/Paris"));
    }
}