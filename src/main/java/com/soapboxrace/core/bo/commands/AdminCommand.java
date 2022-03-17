package com.soapboxrace.core.bo.commands;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.jpa.*;

public class AdminCommand {
    @EJB 
    private AdminBO adminBO;

    public Response initialize(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        if (personaEntity != null && personaEntity.getUser().isAdmin()) {
            Boolean sendOrNot = Boolean.valueOf(webHook);
            adminBO.sendChatCommand(personaEntity.getPersonaId(), command, personaEntity.getName(), sendOrNot);
        }

        return Response.noContent().build();
	}
}