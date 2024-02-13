package net.breezeware.service.impl;

import net.breezeware.dataStore.FoodMenuDataStore;
import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.FoodMenuManager;

import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodMenuManagerImplementation implements FoodMenuManager {
    private final FoodMenuDataStore foodMenuStore;

    public FoodMenuManagerImplementation(FoodMenuDataStore foodMenuStore) {
        this.foodMenuStore = foodMenuStore;
    }

    @Override
    public FoodMenu createFoodMenu(String name, List<AvailableDay> availableDay) throws CustomException {
        String menuName = capitalizeFirstLetter(name);
        if(validateFoodMenuName(menuName) || menuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        Instant instantNow = Instant.now();
        LocalDateTime createdDateTime = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        return foodMenuStore.insertFoodMenu(menuName, getCustomStringRepresentation(availableDay),createdDateTime, modifiedDateTime);
    }

    @Override
    public List<FoodMenu> retrieveFoodMenuOfTheDay() throws CustomException {
        List<FoodMenu> foodMenuList = retrieveAllFoodMenus(true, 1, "_id");
        List<FoodMenu> foodMenuOfTheDay = new ArrayList<>();
        for(FoodMenu menu: foodMenuList){
            LocalDate currentDate = LocalDate.now();
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String today = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            for(AvailableDay day: menu.getAvailableDay()){
                if(day.name().equals(today.toUpperCase())){
                    foodMenuOfTheDay.add(menu);
                }
            }
        }
        if(foodMenuOfTheDay.isEmpty()){
            throw new CustomException("No Food Menu Available Today!");
        }
        return foodMenuOfTheDay;
    }

    @Override
    public FoodMenu retrieveFoodMenu(String foodMenuName) throws CustomException {
        if(validateFoodMenuName(foodMenuName) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        return foodMenuStore.queryFoodMenu(capitalizeFirstLetter(foodMenuName));
    }

    @Override
    public List<FoodItem> retrieveFoodMenuItems(int foodMenuId) throws CustomException {
        return foodMenuStore.queryFoodMenuItems(foodMenuId);
    }

    @Override
    public FoodItem retrieveFoodMenuItem(int foodMenuId, int foodItemId) throws CustomException {
        return foodMenuStore.queryFoodMenuItem(foodMenuId, foodItemId);
    }

    @Override
    public List<FoodMenu> retrieveAllFoodMenus(boolean isOrderBy, int sortOrder, String columnName) throws CustomException {
        return foodMenuStore.queryAllFoodMenu(isOrderBy, sortOrder, columnName);
    }

    @Override
    public boolean addFoodItemsToMenu(int foodMenuId, int foodItemId) throws CustomException {
        return foodMenuStore.addFoodItemsToMenu(foodMenuId, foodItemId);
    }

    @Override
    public boolean deleteFoodItemFromMenu(int foodMenuId, int foodItemId) throws CustomException {
        return foodMenuStore.deleteFoodItemFromMenu(foodMenuId, foodItemId);
    }

    @Override
    public boolean deleteAllFoodItemsFromMenu(int foodMenuId) throws CustomException {
        return foodMenuStore.deleteAllFoodItemsFromMenu(foodMenuId);
    }

    @Override
    public FoodMenu updateFoodMenuName(String newName, String foodMenuName) throws CustomException {
        if(validateFoodMenuName(foodMenuName) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        return foodMenuStore.updateFoodMenuName(
                capitalizeFirstLetter(newName), capitalizeFirstLetter(foodMenuName));
    }

    @Override
    public FoodMenu updateFoodMenuAvailableDay(List<AvailableDay> availableDays, String foodMenuName) throws CustomException {
        if(validateFoodMenuName(foodMenuName) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        return foodMenuStore.updateFoodMenuAvailableDay(
                getCustomStringRepresentation(availableDays), capitalizeFirstLetter(foodMenuName));
    }

    @Override
    public boolean deleteFoodMenu(String foodMenuName) throws CustomException {
        if(validateFoodMenuName(foodMenuName) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        String foodMenu = capitalizeFirstLetter(foodMenuName);
        int retrievedFoodMenuId = retrieveFoodMenu(foodMenu).getId();
        deleteAllFoodItemsFromMenu(retrievedFoodMenuId);
        return foodMenuStore.deleteFoodMenu(foodMenu);
    }

    private String capitalizeFirstLetter(String input) {
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
    private String getCustomStringRepresentation(List<AvailableDay> list) {
        StringBuilder result = new StringBuilder(String.valueOf(list.get(0)).toUpperCase());
        for (int i = 1; i < list.size(); i++) {
            result.append(",").append(String.valueOf(list.get(i)).toUpperCase());
        }
        return result.toString();
    }

    private boolean validateFoodMenuName(String foodItemName){
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher matcher = pattern.matcher(foodItemName);
        return matcher.find();
    }
}
