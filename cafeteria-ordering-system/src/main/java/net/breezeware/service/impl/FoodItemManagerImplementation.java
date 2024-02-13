package net.breezeware.service.impl;

import net.breezeware.exception.CustomException;
import net.breezeware.dataStore.FoodItemDataStore;
import net.breezeware.entity.FoodItem;
import net.breezeware.service.api.FoodItemManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodItemManagerImplementation implements FoodItemManager{

    private final FoodItemDataStore foodItemStore;
    public FoodItemManagerImplementation(FoodItemDataStore foodItemDataStore){
        this.foodItemStore = foodItemDataStore;
    }

    @Override
    public FoodItem createFoodItem(String name, double price) throws CustomException {
        if(validateFoodItemName(name) || name.isEmpty()){
            throw new CustomException("Food Item Name Cannot be Empty and should only contains Letters!");
        }
        String foodItemName = capitalizeFirstLetter(name);
        Instant now = Instant.now();
        int initialQuantity = 0;
        FoodItem newFoodItem = new FoodItem(foodItemName,initialQuantity,price, now, now);
        LocalDateTime createdDateTime = LocalDateTime.ofInstant(newFoodItem.getCreated(), ZoneId.systemDefault());
        LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(newFoodItem.getModified(), ZoneId.systemDefault());

        return foodItemStore.insertFoodItem(newFoodItem.getName(), newFoodItem.getQuantity(),
                newFoodItem.getPrice(), createdDateTime, modifiedDateTime);
    }

    @Override
    public FoodItem retrieveFoodItem(String foodItemName) throws CustomException {
        if(validateFoodItemName(foodItemName) || foodItemName.isEmpty()){
            throw new CustomException("Food Item Name Cannot be Empty and should only contains Letters!");
        }
        return foodItemStore.queryFoodItem(capitalizeFirstLetter(foodItemName));
    }

    @Override
    public List<FoodItem> retrieveAllFoodItems(boolean isOrderBy, int sortOrder, String columnName) throws CustomException {
        return foodItemStore.queryAllFoodItems(isOrderBy, sortOrder, columnName);
    }

    @Override
    public FoodItem updateFoodItemName(String newName, String foodItemName) throws CustomException {
        if(validateFoodItemName(foodItemName) || foodItemName.isEmpty() || validateFoodItemName(newName) || newName.isEmpty()){
            throw new CustomException("Food Item Name Cannot be Empty and should only contains Letters!");
        }
        return foodItemStore
                .updateFoodItemName(capitalizeFirstLetter(newName), capitalizeFirstLetter(foodItemName));
    }

    @Override
    public FoodItem updateFoodItemQuantity(int quantity, String foodItemName) throws CustomException {
        if(validateFoodItemName(foodItemName) || foodItemName.isEmpty()){
            throw new CustomException("Food Item Name Cannot be Empty and should only contains Letters!");
        }
        FoodItem alreadyExistFoodItem = new FoodItem();
        try{
            alreadyExistFoodItem = foodItemStore.queryFoodItem(capitalizeFirstLetter(foodItemName));
        }catch (CustomException e){
            throw new CustomException(e.getMessage());
        }
        FoodItem updatedFoodItem = new FoodItem();
        if(Objects.isNull(alreadyExistFoodItem)){
            throw new CustomException("Food Item not available");
        } else {
            updatedFoodItem = foodItemStore
                    .updateFoodItemQuantity(quantity, capitalizeFirstLetter(foodItemName));
            if(!Objects.isNull(updatedFoodItem)){
                foodItemStore.closeConnection();
                return updatedFoodItem;
            }
        }
        return updatedFoodItem;
    }

    @Override
    public FoodItem updateFoodItemPrice(double price, String foodItemName) throws CustomException {
        if(validateFoodItemName(foodItemName) || foodItemName.isEmpty()){
            throw new CustomException("Food Item Name Cannot be Empty and should only contains Letters!");
        }
        return foodItemStore
                .updateFoodItemPrice(price, capitalizeFirstLetter(foodItemName));
    }

    @Override
    public boolean deleteFoodItem(String foodItemName) throws CustomException {
        if(validateFoodItemName(foodItemName) || foodItemName.isEmpty()){
            throw new CustomException("Food Item Name Cannot be Empty and should only contains Letters!");
        }
        return foodItemStore.deleteFoodItem(capitalizeFirstLetter(foodItemName));
    }

    private String capitalizeFirstLetter(String input) {
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

    private boolean validateFoodItemName(String foodItemName){
        Pattern pattern = Pattern.compile("[^a-zA-Z ]");
        Matcher matcher = pattern.matcher(foodItemName);
        return matcher.find();
    }
}
