package com.soapboxrace.core.bo.commands;

import java.time.LocalDateTime;

import javax.ws.rs.core.Response;
import java.util.Set;

import com.soapboxrace.core.jpa.CarEntity;
import com.soapboxrace.core.jpa.LiveryStoreDataEntity;
import com.soapboxrace.core.jpa.LiveryStoreEntity;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.VinylEntity;
import com.soapboxrace.core.bo.util.HelpingTools;
import com.soapboxrace.core.bo.*;
import com.soapboxrace.core.dao.*;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.core.xmpp.XmppChat;

public class LiveryCommand {
    public Response Command(
        String command_unsplitted, 
        OpenFireSoapBoxCli openFireSoapBoxCli, 
        PersonaEntity personaEntity, 
        LiveryStoreDAO liveryStoreDao, 
        VinylDAO vinylDao, 
        LiveryStoreDataDAO liveryStoreDataDao, 
        ParameterBO parameterBO, 
        PersonaBO personaBO
    ) {
        /* Command construction 
         * 
         * livery import <code> (--force)
         * livery export
        */

        String[] command = command_unsplitted.split(" ");
        
        if(command.length < 2) {
            openFireSoapBoxCli.send(XmppChat.createSystemMessage("No option provided, try using import or export"), personaEntity.getPersonaId());
        } else {
            if(command[1].trim().equals("import")) {                
                if(command.length >= 3) {
                    LiveryStoreEntity liveryStoreEntity = liveryStoreDao.findLiveryByCode(command[2].trim());
                    if(liveryStoreEntity == null) {
                        openFireSoapBoxCli.send(XmppChat.createSystemMessage("That livery doesn't exists."), personaEntity.getPersonaId());
                    } else {
                        Boolean canImport = command_unsplitted.contains("--force");

                        if(canImport == false) {
                            canImport = personaBO.getDefaultCarEntity(personaEntity.getPersonaId()).getName().equals(liveryStoreEntity.getCarname());
                        }

                        if(canImport) {
                            //Remove current liveries
                            VinylEntity oldLiveries = vinylDao.findByCarId(personaBO.getDefaultCarEntity(personaEntity.getPersonaId()).getId());
                            if(oldLiveries != null) {
                                vinylDao.delete(oldLiveries);
                            }
                            
                            //Add new ones to it
                            for (LiveryStoreDataEntity vinyl : liveryStoreDataDao.getVinylsByCode(command[3])) {
                                VinylEntity DataEntity = new VinylEntity();
                                DataEntity.setCar(personaBO.getDefaultCarEntity(personaEntity.getPersonaId()));
                                DataEntity.setHash(vinyl.getHash());
                                DataEntity.setHue1(vinyl.getHue1());
                                DataEntity.setHue2(vinyl.getHue2());
                                DataEntity.setHue3(vinyl.getHue3());
                                DataEntity.setHue4(vinyl.getHue4());
                                DataEntity.setLayer(vinyl.getLayer());
                                DataEntity.setMir(vinyl.isMir());
                                DataEntity.setRot(vinyl.getRot());
                                DataEntity.setSat1(vinyl.getSat1());
                                DataEntity.setSat2(vinyl.getSat2());
                                DataEntity.setSat3(vinyl.getSat3());
                                DataEntity.setSat4(vinyl.getSat4());
                                DataEntity.setScalex(vinyl.getScalex());
                                DataEntity.setScaley(vinyl.getScaley());
                                DataEntity.setShear(vinyl.getShear());
                                DataEntity.setTranx(vinyl.getTranx());
                                DataEntity.setTrany(vinyl.getTrany());
                                DataEntity.setVar1(vinyl.getVar1());
                                DataEntity.setVar2(vinyl.getVar2());
                                DataEntity.setVar3(vinyl.getVar3());
                                DataEntity.setVar4(vinyl.getVar4());
                                vinylDao.insert(DataEntity);
                            }

                            openFireSoapBoxCli.send(XmppChat.createSystemMessage("The livery has been imported, please enter safehouse to check the result"), personaEntity.getPersonaId());
                        } else {
                            openFireSoapBoxCli.send(XmppChat.createSystemMessage("Sorry, but this livery is not compatible with your current car in use, use `/livery import " + command[2].trim() + " --force` to force the import."), personaEntity.getPersonaId());
                        }
                    }

                } else {
                    openFireSoapBoxCli.send(XmppChat.createSystemMessage("No livery name specified for import."), personaEntity.getPersonaId());
                }
            } else if(command[1].trim().equals("export")) {
                //generate the code first
                CarEntity carEntity = personaBO.getDefaultCarEntity(personaEntity.getPersonaId());

                if(carEntity != null) {
                    Set<VinylEntity> vinyls = carEntity.getVinyls();

                    if(vinyls.size() == 0) {
                        openFireSoapBoxCli.send(XmppChat.createSystemMessage("You can't export livery, this car doesnt have any livery preinstalled on it."), personaEntity.getPersonaId());
                    } else {
                        String code = HelpingTools.generateCode(parameterBO.getIntParam("SBRWR_LIVERYCODE_LENGTH", 8));

                        /*LiveryStoreEntity liveryStoreEntity = new LiveryStoreEntity();
                        liveryStoreEntity.setPersonaId(personaEntity.getPersonaId());
                        liveryStoreEntity.setCode(code);
                        liveryStoreEntity.setCreated(LocalDateTime.now());
                        liveryStoreEntity.setCarname(carEntity.getName());
                        liveryStoreDao.insert(liveryStoreEntity);

                        Integer counter = 1;
        
                        //now lets save car vinyls, but first, fetch them!
                        for (VinylEntity vinyl : vinyls) {
                            LiveryStoreDataEntity DataEntity = new LiveryStoreDataEntity();
                            DataEntity.setOrderid(counter);
                            DataEntity.setHash(vinyl.getHash());
                            DataEntity.setHue1(vinyl.getHue1());
                            DataEntity.setHue2(vinyl.getHue2());
                            DataEntity.setHue3(vinyl.getHue3());
                            DataEntity.setHue4(vinyl.getHue4());
                            DataEntity.setLayer(vinyl.getLayer());
                            DataEntity.setMir(vinyl.isMir());
                            DataEntity.setRot(vinyl.getRot());
                            DataEntity.setSat1(vinyl.getSat1());
                            DataEntity.setSat2(vinyl.getSat2());
                            DataEntity.setSat3(vinyl.getSat3());
                            DataEntity.setSat4(vinyl.getSat4());
                            DataEntity.setScalex(vinyl.getScalex());
                            DataEntity.setScaley(vinyl.getScaley());
                            DataEntity.setShear(vinyl.getShear());
                            DataEntity.setTranx(vinyl.getTranx());
                            DataEntity.setTrany(vinyl.getTrany());
                            DataEntity.setVar1(vinyl.getVar1());
                            DataEntity.setVar2(vinyl.getVar2());
                            DataEntity.setVar3(vinyl.getVar3());
                            DataEntity.setVar4(vinyl.getVar4());
                            DataEntity.setLiverycode(code);
                            liveryStoreDataDao.insert(DataEntity);
                            counter++;
                        }*/

                        openFireSoapBoxCli.send(XmppChat.createSystemMessage("Your livery is exported, your code is: " + code), personaEntity.getPersonaId());
                    }
                } else {
                    openFireSoapBoxCli.send(XmppChat.createSystemMessage("You can't export livery, this car doesnt have any livery preinstalled on it."), personaEntity.getPersonaId());
                }
            } else {
                openFireSoapBoxCli.send(XmppChat.createSystemMessage("Invalid option, try using import or export"), personaEntity.getPersonaId());
            }
        }

        return Response.noContent().build();
	}
}