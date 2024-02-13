package net.breezeware.entity;

import net.breezeware.exception.CustomException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MenuAvailability {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
    public static String daysToString(List<MenuAvailability> days) {
        return days.stream()
                // Map each Day object to its name (String)
                .map(Enum::name)
                // Collect the mapped names into a single string separated by ","
                .collect(Collectors.joining(","));
    }

    public static List<MenuAvailability> validateAndConvertAvailableDaysToList(String availableDay) throws CustomException {
        List<String> availableDays = Arrays.asList(availableDay.toUpperCase().split(","));
        for (String day : availableDays) {
            if (!isValidDay(day.trim())) {
                throw new CustomException("Invalid input: " + day.trim());
            }
        }
        return availableDays.stream()
                .map(String::trim)
                .map(MenuAvailability::valueOf)
                .toList();
    }
    private static boolean isValidDay(String day) {
        try {
            MenuAvailability.valueOf(day);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
