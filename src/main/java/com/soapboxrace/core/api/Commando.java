package com.soapboxrace.core.api;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

import com.soapboxrace.core.bo.commands.*;

import javax.ejb.EJB;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;

@Path("/ofcmdhook")
public class Commando {
    @EJB 
    private ParameterBO parameterBO;

    @EJB 
    private PersonaDAO personaDAO;

    @EJB 
    private AdminBO adminBO;

    @EJB 
    private TokenSessionBO tokenSessionBO;

    @EJB 
    private OpenFireSoapBoxCli openFireSoapBoxCli;

    @EJB 
    private LobbyDAO lobbyDAO;

    @EJB 
    private LobbyEntrantDAO lobbyEntrantDAO;

    @POST
    public Response openfireHook(@HeaderParam("Authorization") String token, @QueryParam("cmd") String command, @QueryParam("pid") long persona, @QueryParam("webhook") Boolean webHook) {        
        PersonaEntity personaEntity = personaDAO.find(persona);

        String correctToken = parameterBO.getStrParam("OPENFIRE_TOKEN");

        if (token == null || !MessageDigest.isEqual(token.getBytes(), correctToken.getBytes())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid token").build();
        }      


        //Split up commands
        String[] commandSplitted = command.split(" ");

        //print out the command executed info
        if(parameterBO.getBoolParam("SBRWR_ENABLEDEBUG")) {
            openFireSoapBoxCli.send("Command executed: " + commandSplitted[0], personaEntity.getPersonaId());
            openFireSoapBoxCli.send("Params: " + command.replace(commandSplitted[0], "").trim(), personaEntity.getPersonaId());
        }

        //Switch between them
        /*_CommandsClass commando = new _CommandsClass();
        switch(commandSplitted[0].trim()) {
            case "nopu":    commando.noPowerupsCommand(token, command, personaEntity, webHook); break;
            case "debug":   commando.debugCommand(token, command, personaEntity, webHook); break;
            case "ban":     commando.adminCommands(token, command, personaEntity, webHook); break;
            case "kick":    commando.adminCommands(token, command, personaEntity, webHook); break;
            case "unban":   commando.adminCommands(token, command, personaEntity, webHook); break;
            case "vinyls":  commando.vinylsCommand(token, command, personaEntity, webHook); break;
            default:        commando.defaultCommand(token, command, personaEntity, webHook); break;
        }
        */
        return Response.noContent().build();
    }
}
