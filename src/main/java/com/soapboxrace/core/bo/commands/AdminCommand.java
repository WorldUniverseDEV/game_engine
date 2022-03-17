package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.jpa.*;

interface AdminCommand {
    public default Response adminCommands(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        if (personaEntity != null && personaEntity.getUser().isAdmin()) {
            Boolean sendOrNot = Boolean.valueOf(webHook);
            new AdminBO().sendChatCommand(personaEntity.getPersonaId(), command, personaEntity.getName(), sendOrNot);
        }

        return Response.noContent().build();
	}
}