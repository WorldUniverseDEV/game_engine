package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.*;

public class Livery {
    public Response Command(OpenFireSoapBoxCli openFireSoapBoxCli, PersonaEntity personaEntity, String[] command) {
        //basic checks:
        Boolean forced = false;
        String commandParam = "";
        String liveryid = "";

        //param 1: import / export
        if(command[1] == null) {
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId());
            return Response.noContent().build();
        } else {
            commandParam = command[1];
        }

        //param 2: liveryid (only for import purposes)
        if(command[1].equals("import")) {
            if(command[2] == null) {
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId());
                return Response.noContent().build();
            } else {
                liveryid = command[2].trim();
            }
        }

        //param 3: (optional) check for forced import
        if(command[3] != null && command[3].equals("--force") && command[1].equals("import")) {
            forced = true;
        }

        //Real commands goes here:
        switch(commandParam.trim()) {
            case "import": openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_IMPORTING"), personaEntity.getPersonaId()); executeImport(liveryid, personaEntity, forced); break;
            case "export": openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_EXPORTING"), personaEntity.getPersonaId()); executeExport(personaEntity); break;
            default: openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId()); break;
        }

        return Response.noContent().build();
	}

    private static void executeExport(PersonaEntity personaEntity) {

    }

    private static void executeImport(String liveryid, PersonaEntity personaEntity, Boolean forced) {

    }
}