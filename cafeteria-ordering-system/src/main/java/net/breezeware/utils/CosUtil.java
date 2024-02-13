package net.breezeware.utils;

import net.breezeware.exception.CustomException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CosUtil {
    private CosUtil(){}
    public static String formatInstantToString(Instant instant, String pattern){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return  localDateTime.format(formatter);
    }

    public static String capitalizeFirstLetter(String input) {
        StringBuilder result = new StringBuilder();
        String[] words = input.split("\\s");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
            }
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - 1);
        }
        return result.toString();
    }

    public static String isDateTimeFormatValid(String inputDateTime) throws CustomException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
            LocalDateTime parse = LocalDateTime.parse(inputDateTime, formatter);
            return inputDateTime;
        } catch (DateTimeParseException e) {
            throw new CustomException("Input date-time does not match the pattern.");
        }
    }

    public static boolean validateName(String name, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
}
