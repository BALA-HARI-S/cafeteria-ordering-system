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
    boolean createFoodItem(String name, int quantity, double price);
    void displayFoodItem(String foodItemName);
    void displayAllFoodItems(boolean isOrderBy, int sortOrder, String columnName);
    boolean editFoodItemName(String newName, String foodItem);
    boolean editFoodItemQuantity(int quantity, String foodItem);
    boolean editFoodItemPrice(double price, String foodItem);
    boolean removeFoodItem(String foodItem);

}
