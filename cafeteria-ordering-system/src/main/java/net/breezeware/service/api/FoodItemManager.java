package net.breezeware.service.api;

import net.breezeware.entity.FoodItem;

public interface FoodItemManager {

    /**
     * Create a New Food Item with some added information of the Food Item.
     * @param name - Food Name
     * @param quantity - Food Quantity
     * @param price - Food price
     */
    FoodItem createFoodItem(String name, int quantity, double price);
    void viewFoodItem(String foodItemName);
    void viewAllFoodItems(boolean isOrderBy, int sortOrder, String columnName);
    FoodItem editFoodItemName(String newName, String foodItem);
    FoodItem editFoodItemQuantity(int quantity, String foodItem);
    FoodItem editFoodItemPrice(double price, String foodItem);
    boolean removeFoodItem(String foodItemName);

}
