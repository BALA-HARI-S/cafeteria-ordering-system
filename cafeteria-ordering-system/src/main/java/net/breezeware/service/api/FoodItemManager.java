package net.breezeware.service.api;

import net.breezeware.entity.FoodItem;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface FoodItemManager {

    /**
     * Create a New Food Item with some added information of the Food Item.
     * @param name - Food Name
     * @param price - Food price
     */
    FoodItem createFoodItem(String name, double price) throws CustomException;
    FoodItem retrieveFoodItem(String foodItemName) throws CustomException;
    List<FoodItem> retrieveAllFoodItems(boolean isOrderBy, int sortOrder, String columnName) throws CustomException;
    FoodItem updateFoodItemName(String newName, String foodItem) throws CustomException;
    FoodItem updateFoodItemQuantity(int quantity, String foodItem) throws CustomException;
    FoodItem updateFoodItemPrice(double price, String foodItem) throws CustomException;
    boolean deleteFoodItem(String foodItemName) throws CustomException;
}
