package com.soapboxrace.core.bo.commands;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import java.util.List;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public class Debug {
    @EJB 
    private TokenSessionBO tokenSessionBO;

    @EJB 
    private OpenFireSoapBoxCli openFireSoapBoxCli;

    @EJB 
    private LobbyDAO lobbyDAO;

    @EJB 
    private LobbyEntrantDAO lobbyEntrantDAO;

    public Response initialize(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        TokenSessionEntity tokendata = tokenSessionBO.findByUserId(personaEntity.getUser().getId());
        Long getActiveLobbyId = 0L;
        Long getEventSessionId = 0L;

        if(tokendata.getActiveLobbyId() != null) getActiveLobbyId = tokendata.getActiveLobbyId();
        if(tokendata.getEventSessionId() != null) getEventSessionId = tokendata.getEventSessionId();

        LobbyEntity lobbyEntities = lobbyDAO.findById(getActiveLobbyId);
        if(lobbyEntities != null) {             
            Integer totalVotes = lobbyEntrantDAO.getVotes(lobbyEntities)+1;

            List<LobbyEntrantEntity> lobbyEntrants = lobbyEntities.getEntrants();
            Integer totalUsersInLobby = lobbyEntrants == null ? 1 : lobbyEntrants.size();

            openFireSoapBoxCli.send(XmppChat.createSystemMessage("--- DEBUG ---"), personaEntity.getPersonaId());
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("getActiveLobbyId: " + getActiveLobbyId ), personaEntity.getPersonaId());
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("getEventSessionId: " + getEventSessionId ), personaEntity.getPersonaId());
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("lobbyEntities: " + lobbyEntities ), personaEntity.getPersonaId());
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("totalVotes: " + totalVotes), personaEntity.getPersonaId());
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("totalUsersInLobby: " + totalUsersInLobby), personaEntity.getPersonaId());
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("--- END DEBUG ---"), personaEntity.getPersonaId());
        }

        return Response.noContent().build();
	}
}