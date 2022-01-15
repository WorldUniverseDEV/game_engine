package com.soapboxrace.core.api;

import com.soapboxrace.core.bo.AdminBO;
import com.soapboxrace.core.bo.ParameterBO;
import com.soapboxrace.core.bo.TokenSessionBO;
import com.soapboxrace.core.dao.PersonaDAO;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.TokenSessionEntity;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.core.xmpp.XmppChat;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;

@Path("/ofcmdhook")
public class OpenfireHook {
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

    @POST
    public Response openfireHook(@HeaderParam("Authorization") String token, @QueryParam("cmd") String command, @QueryParam("pid") long persona, @QueryParam("webhook") Boolean webHook) {
        PersonaEntity personaEntity = personaDAO.find(persona);

        if(command.contains("nopu")) {
            TokenSessionEntity tokendata = tokenSessionBO.findByUserId(personaEntity.getUser().getId());

            if(tokendata.getActiveLobbyId() != 0) {
                //TODO: Check if user has already voted in, for now, just print its LOBBYID and SESSIONID for debug.
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("LOBBYID: " + tokendata.getActiveLobbyId()), personaEntity.getPersonaId());
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("SESSIONID: " + tokendata.getEventSessionId()), personaEntity.getPersonaId());
            } else {
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("You are not in event."), personaEntity.getPersonaId());
            }
        } else {
            String correctToken = parameterBO.getStrParam("OPENFIRE_TOKEN");

            if (token == null || !MessageDigest.isEqual(token.getBytes(), correctToken.getBytes())) {
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid token").build();
            }


            if (personaEntity != null && personaEntity.getUser().isAdmin()) {
                Boolean sendOrNot = Boolean.valueOf(webHook);
                adminBO.sendChatCommand(persona, command, personaEntity.getName(), sendOrNot);
            }
        }
        
        return Response.noContent().build();
    }
}
