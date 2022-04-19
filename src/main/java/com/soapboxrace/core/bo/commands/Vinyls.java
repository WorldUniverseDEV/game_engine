package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public class Vinyls {
    public Response Command(OpenFireSoapBoxCli openFireSoapBoxCli, PersonaEntity personaEntity) {
        openFireSoapBoxCli.send(XmppChat.createSystemMessage("TBA"), personaEntity.getPersonaId());
        return Response.noContent().build();
	}
}