package net.breezeware.service.impl;

import net.breezeware.dataStore.FoodItemDataStore;
import net.breezeware.entity.FoodItem;
import net.breezeware.service.api.FoodItemManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FoodItemManagerImplementation implements FoodItemManager{

    private final FoodItemDataStore foodItemStore = new FoodItemDataStore();

    @Override
    public boolean createFoodItem(String name, int quantity, double price) {
        foodItemStore.openConnection();
        FoodItem alreadyExistFoodItem = foodItemStore.queryFoodItem(name);
        if(!Objects.isNull(alreadyExistFoodItem)){
            System.out.println("Food Item Already Exist");
            displayFoodItem(name);
            foodItemStore.closeConnection();
            return false;
        }
        FoodItem newFoodItem = new FoodItem(name,quantity,price, Instant.now(), Instant.now());

        LocalDateTime createdDateTime = LocalDateTime.ofInstant(newFoodItem.getCreated(), ZoneId.systemDefault());
        LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(newFoodItem.getModified(), ZoneId.systemDefault());

        boolean result =  foodItemStore.insertFoodItem(newFoodItem.getName(), newFoodItem.getQuantity(),
                newFoodItem.getPrice(), createdDateTime, modifiedDateTime);
        foodItemStore.closeConnection();
        return result;
    }

    @Override
    public void displayFoodItem(String foodItemName) {
        foodItemStore.openConnection();
        FoodItem foodItem = foodItemStore.queryFoodItem(foodItemName);
        System.out.println(foodItem.toString());
        foodItemStore.closeConnection();
    }

    @Override
    public void displayAllFoodItems(boolean isOrderBy, int sortOrder, String columnName) {
        foodItemStore.openConnection();
        foodItemStore.queryAllFoodItems(isOrderBy, sortOrder, columnName)
                .forEach(foodItem -> System.out.println(foodItem.toString()));
        foodItemStore.closeConnection();
    }

    @Override
    public boolean editFoodItemName(String newName, String foodItem) {
        foodItemStore.openConnection();
        FoodItem updatedFoodItem = foodItemStore.updateFoodItemName(newName, foodItem);
        if(Objects.isNull(updatedFoodItem)){
            foodItemStore.closeConnection();
           return false;
        }
        displayFoodItem(updatedFoodItem.getName());
        foodItemStore.closeConnection();
        return true;
    }

    @Override
    public boolean editFoodItemQuantity(int quantity, String foodItem) {
        foodItemStore.openConnection();
        FoodItem updatedFoodItem = foodItemStore.updateFoodItemQuantity(quantity, foodItem);
        if(Objects.isNull(updatedFoodItem)){
            foodItemStore.closeConnection();
            return false;
        }
        displayFoodItem(updatedFoodItem.getName());
        foodItemStore.closeConnection();
        return true;
    }

    @Override
    public boolean editFoodItemPrice(double price, String foodItem) {
        foodItemStore.openConnection();
        FoodItem updatedFoodItem = foodItemStore.updateFoodItemPrice(price, foodItem);
        if(Objects.isNull(updatedFoodItem)){
            foodItemStore.closeConnection();
            return false;
        }
        displayFoodItem(updatedFoodItem.getName());
        foodItemStore.closeConnection();
        return true;
    }

    @Override
    public boolean removeFoodItem(String foodItem) {
        foodItemStore.openConnection();
        boolean result = foodItemStore.removeFoodItem(foodItem);
        foodItemStore.closeConnection();
        return result;
    }

//    private Instant createdDateAndTime() {
//            //Get current date and time
//            LocalDateTime localDateTime = LocalDateTime.now();
//
//            //Format date and time
//            DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
//            localDateTime.format(dateTimePattern);
//
//            // Convert LocalDateTime to Instant
//            return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
//    }




}
