package com.soapboxrace.core.bo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelpingTools {
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
}
