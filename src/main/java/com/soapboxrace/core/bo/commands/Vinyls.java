package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public interface Vinyls {
    public default Response vinylsCommand(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        new OpenFireSoapBoxCli().send(XmppChat.createSystemMessage("TBA"), personaEntity.getPersonaId());
        return Response.noContent().build();
	}
}