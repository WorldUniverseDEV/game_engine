package com.soapboxrace.core.bo.commands;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public class Vinyls {
    @EJB 
    private OpenFireSoapBoxCli openFireSoapBoxCli;

    public Response initialize(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        openFireSoapBoxCli.send(XmppChat.createSystemMessage("TBA"), personaEntity.getPersonaId());
        return Response.noContent().build();
	}
}