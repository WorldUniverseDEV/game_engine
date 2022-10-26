package com.soapboxrace.core.bo.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;

import com.soapboxrace.core.jpa.*;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.jaxb.xmpp.AchievementAwarded;
import com.soapboxrace.jaxb.xmpp.AchievementsAwarded;
import com.soapboxrace.core.dao.*;

import java.time.LocalDate;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZoneId;

public class HelpingTools {
    @EJB
    private OpenFireSoapBoxCli openFireSoapBoxCli;

    @EJB
    private PersonaDAO personaDAO;
    
    public static Boolean isNullOrEmptyCheck(String string) {
        if (string == null || string.trim().isEmpty()) { 
            return true;
        }

        return false;
    }

    public static String upperFirstSingle(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static String upperFirst(String string) {
        String words[] = string.split("\\s");  
        List<String> capitalizeWord = new ArrayList<>();  

        for(String w : words){  
            capitalizeWord.add(upperFirstSingle(w));
        }

        return String.join(" ", capitalizeWord);
    }

    public static String getClass(int classHash) {
        switch(classHash) {
            case 869393278: return "F";
            case 872416321: return "E";
            case 415909161: return "D";
            case 1866825865: return "C";
            case -406473455: return "B";
            case -405837480: return "A";
            case -2142411446: return "S";
            case 86241155: return "S1";
            case 221915816: return "S2";
            case 1526233495: return "S3";
            default: return "OPEN";
        }
    }

    public static String generateCode(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        int sep = 1;
        
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
            
            if(sep == 3) {
                sb.append('-');
                sep = 0;
            }
            
            sep++;
        }
        
        if(sb.charAt(sb.length() - 1) == '-') {
            return sb.toString().substring(0, sb.toString().length() - 1);
        }

        return sb.toString();
    }
    
    public static void broadcastUICustom(PersonaEntity personaEntity, String text, String description, int seconds, OpenFireSoapBoxCli openFireSoapBoxCli) {
		AchievementsAwarded achievementsAwarded = new AchievementsAwarded();
		achievementsAwarded.setPersonaId(personaEntity.getPersonaId());
		achievementsAwarded.setScore(personaEntity.getScore());
		AchievementAwarded achievementAwarded = new AchievementAwarded();

		String achievedOnStr = "0001-01-01T00:00:00";

		try {
			LocalDate date = LocalDate.now();
			GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
			XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			achievedOnStr = xmlCalendar.toXMLFormat();
		} catch (Exception e) {
			System.err.println("xml calendar str error");
		}

		achievementAwarded.setAchievedOn(achievedOnStr);
		achievementAwarded.setAchievementDefinitionId((long) 104);
		achievementAwarded.setClip("AchievementFlasherBase");
		achievementAwarded.setClipLengthInSeconds(seconds);
		achievementAwarded.setDescription(description);
		achievementAwarded.setIcon("BADGE18");
		achievementAwarded.setName(text);
		achievementAwarded.setPoints(0);
		achievementAwarded.setRare(false);
		achievementAwarded.setRarity(0);

		ArrayList<AchievementAwarded> achievements = new ArrayList<>();
		achievements.add(achievementAwarded);

		achievementsAwarded.setAchievements(achievements);
		openFireSoapBoxCli.send(achievementsAwarded, personaEntity.getPersonaId());
	}
}
