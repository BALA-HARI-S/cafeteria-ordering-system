package net.breezeware.service.api;

import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;

import java.util.List;

public interface FoodMenuManager {
    FoodMenu createFoodMenu(String name, List<AvailableDay> availableDay);
    FoodMenu getFoodMenu(String foodMenuName);
    List<FoodItem> getFoodMenuItems(int foodMenuId);
    List<FoodMenu> getAllFoodMenus();
    boolean addFoodItemsToMenu(int foodMenuId, int foodItemId);
    boolean removeFoodItemFromMenu(int foodMenuId, int foodItemId);
    boolean removeAllFoodItemsFromMenu(int foodMenuId);
    FoodMenu editFoodMenuName(String newName, String foodMenuName);
    FoodMenu editFoodMenuAvailableDay(List<AvailableDay> availableDays, String foodMenuName);
    boolean removeFoodMenu(String foodMenuName);
}
