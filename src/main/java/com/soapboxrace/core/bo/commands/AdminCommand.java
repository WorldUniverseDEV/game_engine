package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.jpa.*;

public class AdminCommand {
    public Response Commands(AdminBO adminBO, PersonaEntity personaEntity, String command, Boolean webHook) {
        if (personaEntity != null && personaEntity.getUser().isAdmin()) {
            Boolean sendOrNot = Boolean.valueOf(webHook);
            adminBO.sendChatCommand(personaEntity.getPersonaId(), command, personaEntity.getName(), sendOrNot);
        }

        return Response.noContent().build();
	}
}