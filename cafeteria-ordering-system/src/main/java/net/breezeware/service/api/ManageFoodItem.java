package net.breezeware.service.api;

import net.breezeware.entity.FoodItem;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface ManageFoodItem {

    /**
     * Creates a new Food Item Object and add this new Food Item to Food Items Data Store
     * @param name Takes Food Item's Name
     * @param price Takes Food Item's price
     */
    FoodItem createFoodItem(String name, double price) throws CustomException;

    /**
     * Retrieves Food Item from Food Items Data Store
     * @param foodItemName Food Item that wants to be retrieved
     * @return Retrieved Food Item
     * @throws CustomException If the Food Item not found or any interruptions occurs during the process
     */
    FoodItem retrieveFoodItem(String foodItemName) throws CustomException;

    /**
     * Retrieves all Food Items that are available in the Data Store
     * @param orderStatus Order Status (Arranging Food Items in Order)
     * @param sortOrder Sort in Ascending or Descending order
     * @param columnName Arrange by column
     * @return Food Items List
     * @throws CustomException If the Food Items Data Store is empty or any interruptions occurs during the process
     */
    List<FoodItem> retrieveAllFoodItems(boolean orderStatus, int sortOrder, String columnName) throws CustomException;

    /**
     * Updates Food Item name in the Data Store
     * @param newName New Food Item Name
     * @param foodItem Food Item Name that wants to be change
     * @return Updated Food Item
     * @throws CustomException if the Food Item in the Data Store cannot be updated or any interruptions occurs during the process
     */
    FoodItem updateFoodItemName(String newName, String foodItem) throws CustomException;

    /**
     * Updates Food Item quantity in the Data Store
     * @param quantity New Food Item Quantity
     * @param foodItem Food Item Name that wants to be change
     * @return Updated Food Item
     * @throws CustomException if the Food Item in the Data Store cannot be updated or any interruptions occurs during the process
     */
    FoodItem updateFoodItemQuantity(int quantity, String foodItem) throws CustomException;

    /**
     * Updates Food Item price in the Data Store
     * @param price New Food Item Price
     * @param foodItem Food Item Name that wants to be change
     * @return Updated Food Item
     * @throws CustomException if the Food Item in the Data Store cannot be updated or any interruptions occurs during the process
     */
    FoodItem updateFoodItemPrice(double price, String foodItem) throws CustomException;

    /**
     * Deletes the Food Item in Data Store
     * @param foodItemName Food Item that wants to be removed from Data Store
     * @return {@code true} if Food Item removed from the Data Store {@code false} otherwise
     * @throws CustomException if the Food Item in the Data Store cannot be removed or any interruptions occurs during the process
     */
    boolean deleteFoodItem(String foodItemName) throws CustomException;
}
