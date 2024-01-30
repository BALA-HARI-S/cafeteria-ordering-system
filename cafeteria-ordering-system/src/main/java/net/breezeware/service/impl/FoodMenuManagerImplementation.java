package net.breezeware.service.impl;

import net.breezeware.dataStore.FoodMenuDataStore;
import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.service.api.FoodMenuManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class FoodMenuManagerImplementation implements FoodMenuManager {
    private final FoodMenuDataStore foodMenuStore = new FoodMenuDataStore();
    @Override
    public FoodMenu createFoodMenu(String name, List<AvailableDay> availableDay) {
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
    public FoodMenu getFoodMenu(String foodMenuName) {
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.queryFoodMenu(capitalizeFirstLetter(foodMenuName));
        foodMenuStore.closeConnection();
        return foodMenu;
    }

    @Override
    public List<FoodItem> getFoodMenuItems(int foodMenuId) {
        foodMenuStore.openConnection();
        List<FoodItem> foodItems = foodMenuStore.getFoodMenuItems(foodMenuId);
        foodMenuStore.closeConnection();
        return foodItems;
    }

    @Override
    public List<FoodMenu> getAllFoodMenus() {
        foodMenuStore.openConnection();
        List<FoodMenu> foodMenuList = foodMenuStore.queryAllFoodMenu();
        foodMenuStore.closeConnection();
        return foodMenuList;
    }

    @Override
    public boolean addFoodItemsToMenu(int foodMenuId, int foodItemId) {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.addFoodItemsToMenu(foodMenuId, foodItemId);
        foodMenuStore.closeConnection();
        return result;
    }

    @Override
    public boolean removeFoodItemFromMenu(int foodMenuId, int foodItemId) {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.removeFoodItemFromMenu(foodMenuId, foodItemId);
        foodMenuStore.closeConnection();
        return result;
    }

    @Override
    public boolean removeAllFoodItemsFromMenu(int foodMenuId) {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.removeAllFoodItemsFromMenu(foodMenuId);
        foodMenuStore.closeConnection();
        return result;
    }

    @Override
    public FoodMenu editFoodMenuName(String newName, String foodMenuName) {
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.updateFoodMenuName(
                capitalizeFirstLetter(newName), capitalizeFirstLetter(foodMenuName));
        foodMenuStore.closeConnection();
        return foodMenu;
    }

    @Override
    public FoodMenu editFoodMenuAvailableDay(List<AvailableDay> availableDays, String foodMenuName) {
        foodMenuStore.openConnection();
        FoodMenu foodMenu = foodMenuStore.updateFoodMenuAvailableDay(
                getCustomStringRepresentation(availableDays), capitalizeFirstLetter(foodMenuName));
        foodMenuStore.closeConnection();
        return foodMenu;
    }

    @Override
    public boolean removeFoodMenu(String foodMenuName) {
        foodMenuStore.openConnection();
        boolean result = foodMenuStore.removeFoodMenu(foodMenuName);
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
