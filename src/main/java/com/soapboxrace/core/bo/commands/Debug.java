package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.jpa.*;

public interface Debug {
    public default Response debugCommand(String token, String command, PersonaEntity personaEntity, Boolean webHook) {
        return Response.noContent().build();
	}
}