package net.breezeware.service.api;

import net.breezeware.entity.MenuAvailability;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface FoodMenuManager {
    FoodMenu createFoodMenu(String name, List<MenuAvailability> availableDay) throws CustomException;
    List<FoodMenu> retrieveFoodMenuOfTheDay() throws CustomException;
    FoodMenu retrieveFoodMenu(String foodMenuName) throws CustomException;
    List<FoodItem> retrieveFoodMenuItems(int foodMenuId) throws CustomException;
    FoodItem retrieveFoodMenuItem(int foodMenuId, int foodItemId) throws CustomException;
    List<FoodMenu> retrieveAllFoodMenus(boolean isOrderBy, int sortOrder, String columnName) throws CustomException;
    boolean addFoodItemsToMenu(int foodMenuId, int foodItemId) throws CustomException;
    boolean deleteFoodItemFromMenu(int foodMenuId, int foodItemId) throws CustomException;
    boolean deleteAllFoodItemsFromMenu(int foodMenuId) throws CustomException;
    FoodMenu updateFoodMenuName(String newName, String foodMenuName) throws CustomException;
    FoodMenu updateFoodMenuAvailableDay(List<MenuAvailability> availableDays, String foodMenuName) throws CustomException;
    boolean deleteFoodMenu(String foodMenuName) throws CustomException;
}
