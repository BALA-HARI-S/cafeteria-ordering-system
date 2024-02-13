package net.breezeware.service.api;

import net.breezeware.entity.MenuAvailability;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface FoodMenuManager {
    /**
     * Create food menu object and add this new food menu to data store
     * @param name Name of the food menu
     * @param menuAvailability Food menu available days
     * @return Created food menu
     * @throws CustomException If any interruptions occur during the process
     */
    FoodMenu createFoodMenu(String name, List<MenuAvailability> menuAvailability) throws CustomException;

    /**
     * Retrieve food menu from data store by comparing MenuAvailability and current day
     * @return List of food menu
     * @throws CustomException If any interruptions occur during the process
     */
    List<FoodMenu> retrieveFoodMenuOfTheDay() throws CustomException;

    /**
     * Retrieve food menu from data store with the provided food menu name
     * @param foodMenuName Food menu name that wants to be retrieved
     * @return Retrieved Food Menu
     * @throws CustomException If there are no match founds or any interruptions occurs during the process
     */
    FoodMenu retrieveFoodMenu(String foodMenuName) throws CustomException;

    /**
     * Retrieve food menu's food items from data store with the provided food menu id
     * @param foodMenuId Food menu id to retrieve food items
     * @return List of food items retrieved from food menu
     * @throws CustomException If no food items found or any interruptions occur during the process
     */
    List<FoodItem> retrieveFoodMenuItems(int foodMenuId) throws CustomException;

    /**
     * Retrieve food menu's food item from data store with the provided food menu id
     * @param foodMenuId Food menu id to retrieve food item
     * @param foodItemId Food Items id
     * @return Retrieved food item
     * @throws CustomException If the food item not found or any interruptions occur during the process
     */
    FoodItem retrieveFoodMenuItem(int foodMenuId, int foodItemId) throws CustomException;

    /**
     * Retrieve all food menu from the data store
     * @param orderStatus Arrange food menu in an order
     * @param sortOrder Ascending or descending sort order
     * @param columnName Arrange by column
     * @return List of food menus
     * @throws CustomException If any interruptions occur during the process
     */
    List<FoodMenu> retrieveAllFoodMenus(boolean orderStatus, int sortOrder, String columnName) throws CustomException;

    /**
     * Add food item to food menu data store
     * @param foodMenuId Food menu id that going to take food item
     * @param foodItemId Food item's id that wants to be added to the menu
     * @return {@code true} if food item added {@code false} otherwise
     * @throws CustomException If any interruptions occur during the process
     */
    boolean addFoodItemsToMenu(int foodMenuId, int foodItemId) throws CustomException;

    /**
     * Delete food item from food menu data store
     * @param foodMenuId Food menu id
     * @param foodItemId Food item's id that wants to be deleted from the menu
     * @return {@code true} if food item deleted {@code false} otherwise
     * @throws CustomException If any interruptions occur during the process
     */
    boolean deleteFoodItemFromMenu(int foodMenuId, int foodItemId) throws CustomException;

    /**
     * Delete all food items from food menu data store
     * @param foodMenuId food menu id
     * @return {@code true} if food items deleted {@code false} otherwise
     * @throws CustomException if any interruptions occur during the process
     */
    boolean deleteAllFoodItemsFromMenu(int foodMenuId) throws CustomException;

    /**
     * Update name of the food menu in data store
     * @param newName New food menu name
     * @param foodMenuName Name of the food menu that wants to be changed
     * @return Updated food menu
     * @throws CustomException if any interruptions occur during the process
     */
    FoodMenu updateFoodMenuName(String newName, String foodMenuName) throws CustomException;

    /**
     * Update menu availability of the food menu in data store
     * @param menuAvailability Menu available days
     * @param foodMenuName Name of the food menu that wants to be changed
     * @return Updated food menu
     * @throws CustomException if any interruptions occur during the process
     */
    FoodMenu updateFoodMenuAvailability(List<MenuAvailability> menuAvailability, String foodMenuName) throws CustomException;

    /**
     * Delete food menu in the data store
     * @param foodMenuName food menu id
     * @return {@code true} If food menu deleted {@code false} otherwise
     * @throws CustomException if any interruptions occur during the process
     */
    boolean deleteFoodMenu(String foodMenuName) throws CustomException;
}
