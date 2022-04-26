/*
 * This file is part of the Soapbox Race World core source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Copyright (c) 2020.
 */

package com.soapboxrace.core.bo;

import com.soapboxrace.core.bo.util.TimeConverter;
import com.soapboxrace.core.bo.util.HelpingTools;
import com.soapboxrace.core.bo.util.KonamiDecode;
import com.soapboxrace.core.dao.CarDAO;
import com.soapboxrace.core.jpa.CarEntity;
import com.soapboxrace.core.jpa.EventDataEntity;
import com.soapboxrace.core.jpa.EventSessionEntity;
import com.soapboxrace.jaxb.http.ArbitrationPacket;
import com.soapboxrace.jaxb.http.PursuitArbitrationPacket;
import com.soapboxrace.jaxb.http.TeamEscapeArbitrationPacket;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class LegitRaceBO {

    @EJB
    private SocialBO socialBo;

    @EJB
    private CarDAO carDAO;

    @EJB
    private ParameterBO parameterBO;

    private Boolean isLegit = true;
    private List<String> listOfReports = new ArrayList<>();

    private void sendReport(String reportType, String message) {
        if(parameterBO.getBoolParam("SBRWR_DISABLE_" + reportType + "_REPORTS")) {
            listOfReports.add("- " + message);
            isLegit = false;
        }
    }

    public boolean isLegit(Long activePersonaId, ArbitrationPacket arbitrationPacket, EventSessionEntity sessionEntity, EventDataEntity dataEntity) {
        long minimumTime = sessionEntity.getEvent().getLegitTime();
        boolean legit = dataEntity.getServerTimeInMilliseconds() >= minimumTime;
        String eventName = HelpingTools.upperFirst(sessionEntity.getEvent().getName().split("\\(")[0].trim());
        String reportMessage = "";

        if (!legit) {
            reportMessage = String.format("Abnormal event time: %d (below minimum of %d)", 
                dataEntity.getServerTimeInMilliseconds(), minimumTime);

            sendReport("ABNORMALTIME", reportMessage);
        }

        //Calculate globaltime
        if((arbitrationPacket.getAlternateEventDurationInMilliseconds()-dataEntity.getServerTimeInMilliseconds()) >= parameterBO.getIntParam("SBRWR_TIME_THRESHOLD", 10000)) {
            int timediff = (int)(arbitrationPacket.getAlternateEventDurationInMilliseconds()-dataEntity.getServerTimeInMilliseconds())/1000;

            reportMessage = String.format("Autofinish detected: timediff is %s", 
                TimeConverter.secToTime(timediff));

            sendReport("AUTOFINISH", reportMessage);
        }

        if (arbitrationPacket.getKonami() > 0) {
            reportMessage = String.format("konami => %s",
                KonamiDecode.getHacksType(arbitrationPacket.getKonami(), "konami"));

            sendReport("KONAMI", reportMessage);
        }

        if (arbitrationPacket.getHacksDetected() > 0) {
            reportMessage = String.format("hacksDetected => %s (event %s; session %d)",
                KonamiDecode.getHacksType((int)(arbitrationPacket.getHacksDetected()), "hacksDetected"), eventName, sessionEntity.getId());
    
            sendReport("HACKSDETECTED", reportMessage);
        }

        if (arbitrationPacket instanceof TeamEscapeArbitrationPacket) {
            TeamEscapeArbitrationPacket teamEscapeArbitrationPacket = (TeamEscapeArbitrationPacket) arbitrationPacket;

            if (teamEscapeArbitrationPacket.getFinishReason() != 8202) {
                if(teamEscapeArbitrationPacket.getCopsDisabled() > teamEscapeArbitrationPacket.getCopsDeployed()) {
                    reportMessage = String.format("Disabled more cops than deployed (deployed %d; disabled %d)",
                        teamEscapeArbitrationPacket.getCopsDisabled(), teamEscapeArbitrationPacket.getCopsDeployed());
                    
                    sendReport("DISABLED_COPS", reportMessage);
                }
            }
        }

        if (arbitrationPacket instanceof PursuitArbitrationPacket) {
            PursuitArbitrationPacket pursuitArbitrationPacket = (PursuitArbitrationPacket) arbitrationPacket;

            if (pursuitArbitrationPacket.getFinishReason() != 8202) {
                if (pursuitArbitrationPacket.getCopsDisabled() > pursuitArbitrationPacket.getCopsDeployed()) {
                    reportMessage = String.format("Disabled more cops than deployed (deployed %d; disabled %d)",
                        pursuitArbitrationPacket.getCopsDisabled(), pursuitArbitrationPacket.getCopsDeployed());
                
                    sendReport("DISABLED_COPS", reportMessage);
                }

                //Calc, wow
                if ((pursuitArbitrationPacket.getAlternateEventDurationInMilliseconds()/1000)/pursuitArbitrationPacket.getCopsDeployed() >= parameterBO.getIntParam("SBRWR_COPS_THRESHOLD", 25)) {
                    reportMessage = String.format("Invalid data received from pursuit outrun, over %d cops in %d seconds",
                        pursuitArbitrationPacket.getCopsDeployed(), pursuitArbitrationPacket.getAlternateEventDurationInMilliseconds()/1000);

                    sendReport("INVALID_OUTRUN_DATA", reportMessage);
                }

                if(pursuitArbitrationPacket.getTopSpeed() == 0) {
                    sendReport("NOSPEED", "User hasn't moved from place");
                }

                if(pursuitArbitrationPacket.getInfractions() == 0) {
                    sendReport("NOINFRACTION", "User didn't made any infraction");
                }
            }
        }

        CarEntity carEntity = carDAO.find(arbitrationPacket.getCarId());
        if (carEntity == null) {
            sendReport("NONEXISTENT_CAR", "User drove a car not in database.");
        }

        if (carEntity.getCarClassHash() == 0) {
            sendReport("TRAFFIC_CAR", "User drove a Traffic (or nonexistent) Car");
        }

        if(sessionEntity.getEvent().getCarClassHash() != 607077938) {
            if(carEntity.getCarClassHash() != sessionEntity.getEvent().getCarClassHash()) {
                reportMessage = String.format("User drove a car that doesn't meet the class restriction of the event (carClass %s, eventClass %s).", 
                    HelpingTools.getClass(carEntity.getCarClassHash()), HelpingTools.getClass(sessionEntity.getEvent().getCarClassHash()));

                sendReport("INVALID_CARCLASS", reportMessage);
            }
        }

        if(!listOfReports.isEmpty()) {
            listOfReports.add(String.format("\\\non event %s; session %d", eventName, sessionEntity.getId()));
            socialBo.sendReport(0L, activePersonaId, 4, String.join("\\\n", listOfReports), (int) arbitrationPacket.getCarId(), 0, arbitrationPacket.getHacksDetected());
        }

        return isLegit;
    }
}
