package com.soapboxrace.core.bo.commands;

import javax.ws.rs.core.Response;

import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.bo.util.HelpingTools;
import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.xmpp.*;

import java.time.LocalDateTime;

import javax.ejb.EJB;

public class Livery {
    @EJB
    private LiveryStoreDAO liveryStoreDao;

    public Response Command(OpenFireSoapBoxCli openFireSoapBoxCli, PersonaEntity personaEntity, PersonaBO personaBO, String[] command) {
        //basic checks:
        Boolean forced = false;
        String commandParam = "";
        String liveryid = "";

        //param 1: import / export
        if(command.length > 1 && command[1] == null) {
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId());
            return Response.noContent().build();
        } else {
            commandParam = command[1];
        }

        //param 2: liveryid (only for import purposes)
        if(command[1].equals("import")) {
            if(command.length > 2 && command[2] == null) {
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId());
                return Response.noContent().build();
            } else {
                try {
                    liveryid = command[2].trim();
                } catch(ArrayIndexOutOfBoundsException err) {
                    openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId());
                    return Response.noContent().build();
                }
            }
        }

        //param 3: (optional) check for forced import
        if(command.length > 3 && command[3] != null && command[3].equals("--force") && command[1].equals("import")) {
            forced = true;
        }

        //Real commands goes here:
        switch(commandParam.trim()) {
            case "import": openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_IMPORTING"), personaEntity.getPersonaId()); executeImport(openFireSoapBoxCli, liveryid, personaEntity, forced); break;
            case "export": openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_EXPORTING"), personaEntity.getPersonaId()); executeExport(openFireSoapBoxCli, personaEntity, personaBO); break;
            default: openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId()); break;
        }

        return Response.noContent().build();
	}

    private void executeExport(OpenFireSoapBoxCli openFireSoapBoxCli, PersonaEntity personaEntity, PersonaBO personaBO) {
        CarEntity userCar = personaBO.getDefaultCarEntity(personaEntity.getPersonaId());

        if(userCar != null) {
            //Createaset

            LiveryStoreEntity liveryStoreEntity = new LiveryStoreEntity();
            liveryStoreEntity.setPersona(personaEntity);
            liveryStoreEntity.setCode(HelpingTools.generateCode(6));
            //liveryStoreEntity.setData(null); //TODO: fetch user car and store compatible layers here
            liveryStoreEntity.setCarname(userCar.getName());
            liveryStoreEntity.setCreated(LocalDateTime.now());
            liveryStoreDao.insert(liveryStoreEntity);

            /*Set<VinylEntity> vinylsOnCar = userCar.getVinyls();
            Integer counter = 1;
            for (VinylEntity vinylEntity : vinylsOnCar) {
                counter++;
                
            }*/

        } else {
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("SBRWR_LIVERY_MALFORMEDCOMMAND"), personaEntity.getPersonaId());
        }
    }

    private void executeImport(OpenFireSoapBoxCli openFireSoapBoxCli, String liveryid, PersonaEntity personaEntity, Boolean forced) {

    }
}