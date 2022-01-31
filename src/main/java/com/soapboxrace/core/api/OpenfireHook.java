package com.soapboxrace.core.api;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.jpa.LobbyEntity;
import com.soapboxrace.core.jpa.LobbyEntrantEntity;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.TokenSessionEntity;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.core.xmpp.XmppChat;

import javax.ejb.EJB;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;
import java.util.List;

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

    @EJB
    private LobbyDAO lobbyDAO;

    @EJB
    private LobbyEntrantDAO lobbyEntrantDAO;

    @POST
    public Response openfireHook(@HeaderParam("Authorization") String token, @QueryParam("cmd") String command, @QueryParam("pid") long persona, @QueryParam("webhook") Boolean webHook) {
        String correctToken = parameterBO.getStrParam("OPENFIRE_TOKEN");

        if (token == null || !MessageDigest.isEqual(token.getBytes(), correctToken.getBytes())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid token").build();
        }
        
        PersonaEntity personaEntity = personaDAO.find(persona);

        if(command.contains("nopu")) {
            if(parameterBO.getBoolParam("SBRWR_ENABLE_NOPU")) {
                TokenSessionEntity tokendata = tokenSessionBO.findByUserId(personaEntity.getUser().getId());
                Long getActiveLobbyId = 0L;
                Long getEventSessionId = 0L;

                //check if is a null value
                if(tokendata.getActiveLobbyId() != null) getActiveLobbyId = tokendata.getActiveLobbyId();
                if(tokendata.getEventSessionId() != null) getEventSessionId = tokendata.getEventSessionId();

                openFireSoapBoxCli.send(XmppChat.createSystemMessage("LOBBYID: " + getActiveLobbyId), personaEntity.getPersonaId());
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("SESSIONID: " + getEventSessionId), personaEntity.getPersonaId());

                if(getActiveLobbyId != 0L && getEventSessionId == 0) { //WE'RE ON LOBBY
                    //Let's send a message of users that already voted
                   
                    /* TODO: 
                     * 1. Get all personaids on that lobby. [DONE]
                     * 2. Actually send a message informing them about vote progress. [DONE]
                     * 3. Validate that the user voted, informing them that he does not have to re-vote.
                    */

                    LobbyEntity lobbyEntities = lobbyDAO.findById(getActiveLobbyId);

                    List<LobbyEntrantEntity> lobbyEntrants = lobbyEntities.getEntrants();
                    List<LobbyEntrantEntity> lobbyEntrantsEntities = lobbyEntrantDAO.getVotes(getActiveLobbyId);

                    Integer totalVotes = lobbyEntrantsEntities.size();
                    Integer totalUsersInLobby = lobbyEntrants.size();
                    Integer totalVotesPercentage = Math.round((totalVotes * 100.0f) / totalUsersInLobby);

                    if(parameterBO.getBoolParam("SBRWR_NOPU_ENABLE_VOTEMESSAGES")) {
                        for (LobbyEntrantEntity lobbyEntrant : lobbyEntrants) {
                            openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_NOPU_USERVOTED," + lobbyEntrant.getPersona().getName() + "," + totalVotes + "," + totalUsersInLobby), lobbyEntrant.getPersona().getPersonaId());
                        }
                    }
                } else if(getActiveLobbyId != 0L && getEventSessionId != 0) {
                    if(parameterBO.getBoolParam("SBRWR_NOPU_ENABLE_WARNING_ONEVENT")) {
                        openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_NOPU_WARNING_ONEVENT"), personaEntity.getPersonaId());
                    }                
                } else {
                    if(parameterBO.getBoolParam("SBRWR_NOPU_ENABLE_WARNING_ONFREEROAM")) {
                        openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_NOPU_WARNING_ONFREEROAM"), personaEntity.getPersonaId());
                    }
                }
            }
        } else {
            if (personaEntity != null && personaEntity.getUser().isAdmin()) {
                Boolean sendOrNot = Boolean.valueOf(webHook);
                adminBO.sendChatCommand(persona, command, personaEntity.getName(), sendOrNot);
            }
        }
        
        return Response.noContent().build();
    }
}
