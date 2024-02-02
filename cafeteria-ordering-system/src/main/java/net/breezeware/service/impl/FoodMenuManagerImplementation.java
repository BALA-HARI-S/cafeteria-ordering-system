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

public class FoodMenuManagerImplementation implements FoodMenuManager {
    private final FoodMenuDataStore foodMenuStore = new FoodMenuDataStore();
    @Override
    public FoodMenu createFoodMenu(String name, List<AvailableDay> availableDay) throws CustomException {
        String menuName = capitalizeFirstLetter(name);
        Instant instantNow = Instant.now();
        LocalDateTime createdDateTime = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.insertFoodMenu(menuName, getCustomStringRepresentation(availableDay),createdDateTime, modifiedDateTime);
        foodMenuStore.closeConnection();
        return foodMenu;
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
        return foodMenuOfTheDay;
    }

    @Override
    public FoodMenu retrieveFoodMenu(String foodMenuName) throws CustomException {
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.queryFoodMenu(capitalizeFirstLetter(foodMenuName));
        foodMenuStore.closeConnection();
        return foodMenu;
    }

    @Override
    public List<FoodItem> retrieveFoodMenuItems(int foodMenuId) throws CustomException {
        foodMenuStore.openConnection();
        List<FoodItem> foodItems = foodMenuStore.getFoodMenuItems(foodMenuId);
        foodMenuStore.closeConnection();
        return foodItems;
    }

    @Override
    public List<FoodMenu> retrieveAllFoodMenus(boolean isOrderBy, int sortOrder, String columnName) throws CustomException {
        foodMenuStore.openConnection();
        List<FoodMenu> foodMenuList = foodMenuStore.queryAllFoodMenu(isOrderBy, sortOrder, columnName);
        foodMenuStore.closeConnection();
        return foodMenuList;
    }

    @Override
    public boolean addFoodItemsToMenu(int foodMenuId, int foodItemId) throws CustomException {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.addFoodItemsToMenu(foodMenuId, foodItemId);
        foodMenuStore.closeConnection();
        return result;
    }

    @Override
    public boolean deleteFoodItemFromMenu(int foodMenuId, int foodItemId) throws CustomException {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.deleteFoodItemFromMenu(foodMenuId, foodItemId);
        foodMenuStore.closeConnection();
        return result;
    }

    @Override
    public boolean deleteAllFoodItemsFromMenu(int foodMenuId) throws CustomException {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.deleteAllFoodItemsFromMenu(foodMenuId);
        foodMenuStore.closeConnection();
        return result;
    }

    @Override
    public FoodMenu updateFoodMenuName(String newName, String foodMenuName) throws CustomException {
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.updateFoodMenuName(
                capitalizeFirstLetter(newName), capitalizeFirstLetter(foodMenuName));
        foodMenuStore.closeConnection();
        return foodMenu;
    }

    @Override
    public FoodMenu updateFoodMenuAvailableDay(List<AvailableDay> availableDays, String foodMenuName) throws CustomException {
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.updateFoodMenuAvailableDay(
                getCustomStringRepresentation(availableDays), capitalizeFirstLetter(foodMenuName));
        foodMenuStore.closeConnection();
        return foodMenu;
    }

    @Override
    public boolean deleteFoodMenu(String foodMenuName) throws CustomException {
        String foodMenu = capitalizeFirstLetter(foodMenuName);
        int retrievedFoodMenuId = retrieveFoodMenu(foodMenu).getId();
        deleteAllFoodItemsFromMenu(retrievedFoodMenuId);
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.deleteFoodMenu(foodMenu);
        foodMenuStore.closeConnection();
        return result;
    }

    private static String capitalizeFirstLetter(String input) {
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
    private static String getCustomStringRepresentation(List<AvailableDay> list) {
        StringBuilder result = new StringBuilder(String.valueOf(list.get(0)).toUpperCase());
        for (int i = 1; i < list.size(); i++) {
            result.append(",").append(String.valueOf(list.get(i)).toUpperCase());
        }
        return result.toString();
    }

}
