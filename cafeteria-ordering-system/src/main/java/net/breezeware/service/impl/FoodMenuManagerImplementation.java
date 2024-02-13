package net.breezeware.service.impl;

import net.breezeware.dataStore.FoodMenuDataStore;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.MenuAvailability;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.ManageFoodMenu;
import net.breezeware.utils.CosUtil;

import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodMenuManagerImplementation implements ManageFoodMenu {
    private final FoodMenuDataStore foodMenuStore;

    private final String foodMenuNamePattern = "[^a-zA-Z0-9 ]";
    public FoodMenuManagerImplementation(FoodMenuDataStore foodMenuStore) {
        this.foodMenuStore = foodMenuStore;
    }

    @Override
    public FoodMenu createFoodMenu(String name, List<MenuAvailability> menuAvailability) throws CustomException {
        String menuName = CosUtil.capitalizeFirstLetter(name);
        if(CosUtil.validateName(menuName,foodMenuNamePattern) || menuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        Instant instantNow = Instant.now();
        LocalDateTime createdDateTime = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        return foodMenuStore.insertFoodMenu(menuName, MenuAvailability.daysToString(menuAvailability),createdDateTime, modifiedDateTime);
    }

    @Override
    public List<FoodMenu> retrieveFoodMenuOfTheDay() throws CustomException {
        List<FoodMenu> foodMenuList = retrieveAllFoodMenus(true, 1, "_id");
        List<FoodMenu> foodMenuOfTheDay = new ArrayList<>();
        for(FoodMenu menu: foodMenuList){
            LocalDate currentDate = LocalDate.now();
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String today = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            for(MenuAvailability day: menu.getAvailableDay()){
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
        if(CosUtil.validateName(foodMenuName,foodMenuNamePattern) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        return foodMenuStore.queryFoodMenu(CosUtil.capitalizeFirstLetter(foodMenuName));
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
    public List<FoodMenu> retrieveAllFoodMenus(boolean orderStatus, int sortOrder, String columnName) throws CustomException {
        return foodMenuStore.queryAllFoodMenu(orderStatus, sortOrder, columnName);
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
        if(CosUtil.validateName(foodMenuName,foodMenuNamePattern) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        return foodMenuStore.updateFoodMenuName(
                CosUtil.capitalizeFirstLetter(newName), CosUtil.capitalizeFirstLetter(foodMenuName));
    }

    @Override
    public FoodMenu updateFoodMenuAvailability(List<MenuAvailability> menuAvailability, String foodMenuName) throws CustomException {
        if(CosUtil.validateName(foodMenuName,foodMenuNamePattern) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        return foodMenuStore.updateFoodMenuAvailableDay(
                MenuAvailability.daysToString(menuAvailability), CosUtil.capitalizeFirstLetter(foodMenuName));
    }

    @Override
    public boolean deleteFoodMenu(String foodMenuName) throws CustomException {
        if(CosUtil.validateName(foodMenuName,foodMenuNamePattern) || foodMenuName.isEmpty()){
            throw new CustomException("Food Item Menu Cannot be Empty and should only contains a-z,A-Z,0-9");
        }
        String foodMenu = CosUtil.capitalizeFirstLetter(foodMenuName);
        int retrievedFoodMenuId = retrieveFoodMenu(foodMenu).getId();
        deleteAllFoodItemsFromMenu(retrievedFoodMenuId);
        return foodMenuStore.deleteFoodMenu(foodMenu);
    }
}
