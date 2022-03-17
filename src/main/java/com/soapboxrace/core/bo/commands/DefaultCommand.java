package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public interface DefaultCommand {
    public default Response defaultCommand(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        new OpenFireSoapBoxCli().send(XmppChat.createSystemMessage("Unknown command: " + command), personaEntity.getPersonaId());
        return Response.noContent().build();
	}
}