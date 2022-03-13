package com.soapboxrace.core.bo.commands;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public class AdminCommand {
    @EJB private ParameterBO parameterBO;
    @EJB private PersonaDAO personaDAO;
    @EJB private AdminBO adminBO;
    @EJB private TokenSessionBO tokenSessionBO;
    @EJB private OpenFireSoapBoxCli openFireSoapBoxCli;
    @EJB private LobbyDAO lobbyDAO;
    @EJB private LobbyEntrantDAO lobbyEntrantDAO;

    public Response initialize(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        if (personaEntity != null && personaEntity.getUser().isAdmin()) {
            Boolean sendOrNot = Boolean.valueOf(webHook);
            adminBO.sendChatCommand(personaEntity.getPersonaId(), command, personaEntity.getName(), sendOrNot);
        }

        return Response.noContent().build();
	}
}