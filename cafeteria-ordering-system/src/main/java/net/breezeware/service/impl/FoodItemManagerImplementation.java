package net.breezeware.service.impl;

import net.breezeware.dataStore.FoodItemDataStore;
import net.breezeware.entity.FoodItem;
import net.breezeware.service.api.FoodItemManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class FoodItemManagerImplementation implements FoodItemManager{

    private final FoodItemDataStore foodItemStore = new FoodItemDataStore();

    @Override
    public FoodItem createFoodItem(String name, int quantity, double price) {
        String foodItemName = capitalizeFirstLetter(name);
        foodItemStore.openConnection();
        FoodItem alreadyExistFoodItem = foodItemStore.queryFoodItem(foodItemName);
        if(!Objects.isNull(alreadyExistFoodItem)){
            System.out.println("Food Item Already Exist");
            viewFoodItem(foodItemName);
            foodItemStore.closeConnection();
        } else {
            Instant now = Instant.now();
            FoodItem newFoodItem = new FoodItem(foodItemName,quantity,price, now, now);
            LocalDateTime createdDateTime = LocalDateTime.ofInstant(newFoodItem.getCreated(), ZoneId.systemDefault());
            LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(newFoodItem.getModified(), ZoneId.systemDefault());

            FoodItem foodItem = foodItemStore.insertFoodItem(newFoodItem.getName(), newFoodItem.getQuantity(),
                    newFoodItem.getPrice(), createdDateTime, modifiedDateTime);
            if(!Objects.isNull(foodItem)){
                foodItemStore.closeConnection();
                return foodItem;
            }
        }
        foodItemStore.closeConnection();
        return null;
    }

    @Override
    public void viewFoodItem(String foodItemName) {
        foodItemStore.openConnection();
        FoodItem foodItem = foodItemStore.queryFoodItem(capitalizeFirstLetter(foodItemName));
        if(!Objects.isNull(foodItem)){
            System.out.println("\n_id | name | quantity | price | created | modified");
            System.out.println(foodItem);
            foodItemStore.closeConnection();
        } else {
            System.out.println("There is no such Food Item, Enter a valid food-item name!");
        }
        foodItemStore.closeConnection();
    }

    @Override
    public void viewAllFoodItems(boolean isOrderBy, int sortOrder, String columnName) {
        foodItemStore.openConnection();
        System.out.println("\n_id | name | quantity | price | created | modified");
        foodItemStore.queryAllFoodItems(isOrderBy, sortOrder, columnName)
                .forEach(foodItem -> System.out.println(foodItem.toString()));
        foodItemStore.closeConnection();
    }

    @Override
    public FoodItem editFoodItemName(String newName, String foodItemName) {
        foodItemStore.openConnection();
        FoodItem alreadyExistFoodItem = foodItemStore.queryFoodItem(capitalizeFirstLetter(foodItemName));
        if(Objects.isNull(alreadyExistFoodItem)){
            System.out.println("Food Item not available");
        } else {
            FoodItem updatedFoodItem = foodItemStore
                    .updateFoodItemName(capitalizeFirstLetter(newName), capitalizeFirstLetter(foodItemName));
            if(!Objects.isNull(updatedFoodItem)){
                foodItemStore.closeConnection();
                return updatedFoodItem;
            }
        }
        foodItemStore.closeConnection();
        return null;
    }

    @Override
    public FoodItem editFoodItemQuantity(int quantity, String foodItemName) {
        foodItemStore.openConnection();
        FoodItem alreadyExistFoodItem = foodItemStore.queryFoodItem(capitalizeFirstLetter(foodItemName));
        if(Objects.isNull(alreadyExistFoodItem)){
            System.out.println("Food Item not available");
        } else {
            FoodItem updatedFoodItem = foodItemStore
                    .updateFoodItemQuantity(quantity, capitalizeFirstLetter(foodItemName));
            if(!Objects.isNull(updatedFoodItem)){
                foodItemStore.closeConnection();
                return updatedFoodItem;
            }
        }
        foodItemStore.closeConnection();
        return null;
    }

    @Override
    public FoodItem editFoodItemPrice(double price, String foodItemName) {
        foodItemStore.openConnection();
        FoodItem alreadyExistFoodItem = foodItemStore.queryFoodItem(capitalizeFirstLetter(foodItemName));
        if(Objects.isNull(alreadyExistFoodItem)){
            System.out.println("Food Item not available");
        } else {
            FoodItem updatedFoodItem = foodItemStore
                    .updateFoodItemPrice(price, capitalizeFirstLetter(foodItemName));
            if(!Objects.isNull(updatedFoodItem)){
                foodItemStore.closeConnection();
                return updatedFoodItem;
            }
        }
        foodItemStore.closeConnection();
        return null;
    }

    @Override
    public boolean removeFoodItem(String foodItemName) {
        foodItemStore.openConnection();
        boolean result = foodItemStore.removeFoodItem(capitalizeFirstLetter(foodItemName));
        foodItemStore.closeConnection();
        return result;
    }

    private static String capitalizeFirstLetter(String input) {
        StringBuilder result = new StringBuilder();
        String[] words = input.split("\\s");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
            }
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - 1);
        }
        return result.toString();
    }


}
