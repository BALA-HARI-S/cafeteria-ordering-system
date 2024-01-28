package net.breezeware.service.api;

import net.breezeware.entity.FoodItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface FoodItemManager {

    /**
     * Create a New Food Item with some added information of the Food Item.
     * @param name - Food Name
     * @param quantity - Food Quantity
     * @param price - Food price
     */
    void createFoodItem(String name, int quantity, double price);
    void displayFoodItem(String foodItemName);
    void displayAllFoodItems(boolean isOrderBy, int sortOrder, String columnName);
    FoodItem editFoodItemName(String newName, String foodItem);
    FoodItem editFoodItemQuantity(int quantity, String foodItem);
    FoodItem editFoodItemPrice(double price, String foodItem);
    boolean removeFoodItem(String foodItemName);

}
