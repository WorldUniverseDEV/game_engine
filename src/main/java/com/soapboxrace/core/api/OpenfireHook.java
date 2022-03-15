package com.soapboxrace.core.api;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;

import com.soapboxrace.core.bo.commands.*;

import javax.ejb.EJB;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;

@Path("/ofcmdhook")
public class OpenfireHook {
    @EJB private ParameterBO parameterBO;
    @EJB private PersonaDAO personaDAO;
    @EJB private AdminBO adminBO;
    @EJB private TokenSessionBO tokenSessionBO;
    @EJB private OpenFireSoapBoxCli openFireSoapBoxCli;
    @EJB private LobbyDAO lobbyDAO;
    @EJB private LobbyEntrantDAO lobbyEntrantDAO;

    @EJB private AdminCommand adminCommand;
    @EJB private Debug debugCommand;
    @EJB private NoPowerups noPowerupsCommand;
    @EJB private Vinyls vinylsCommand;
    @EJB private DefaultCommand defaultCommand;

    @POST
    public Response openfireHook(@HeaderParam("Authorization") String token, @QueryParam("cmd") String command, @QueryParam("pid") long persona, @QueryParam("webhook") Boolean webHook) {        
        PersonaEntity personaEntity = personaDAO.find(persona);

        String correctToken = parameterBO.getStrParam("OPENFIRE_TOKEN");

        if (token == null || !MessageDigest.isEqual(token.getBytes(), correctToken.getBytes())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid token").build();
        }

        //Split up commands
        String[] commandSplitted = command.split(" ");
        switch(commandSplitted[0].trim()) {
            case "nopu":    noPowerupsCommand.initialize(token, command, personaEntity, webHook); break;
            case "debug":   debugCommand.initialize(token, command, personaEntity, webHook); break;
            case "ban":     adminCommand.initialize(token, command, personaEntity, webHook); break;
            case "kick":    adminCommand.initialize(token, command, personaEntity, webHook); break;
            case "unban":   adminCommand.initialize(token, command, personaEntity, webHook); break;
            case "vinyls":  vinylsCommand.initialize(token, command, personaEntity, webHook); break;
            default:        defaultCommand.initialize(token, command, personaEntity, webHook); break;
        }
        
        return Response.noContent().build();
    }
}
