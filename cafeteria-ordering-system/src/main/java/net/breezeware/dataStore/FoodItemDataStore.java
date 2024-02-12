package net.breezeware.dataStore;

import net.breezeware.exception.CustomException;
import net.breezeware.entity.FoodItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodItemDataStore {
    private static final String DB_NAME = "cos";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String DB_USER = "cos_usr";
    private static final String DB_PASSWORD= "P@ssw0rd";
    private static final String TABLE_FOOD_ITEMS = "food_items";
    private static final String COLUMN_FOOD_ID = "_id";
    private static final String COLUMN_FOOD_NAME = "name";
    private static final String COLUMN_FOOD_QUANTITY = "quantity";
    private static final String COLUMN_FOOD_PRICE = "price";
    private static final String COLUMN_FOOD_CREATED = "created";
    private static final String COLUMN_FOOD_MODIFIED = "modified";

    private static final String INSERT_FOOD_ITEM = "INSERT INTO " + TABLE_FOOD_ITEMS +
            "(" + COLUMN_FOOD_NAME + "," + COLUMN_FOOD_QUANTITY + "," + COLUMN_FOOD_PRICE + "," +
            COLUMN_FOOD_CREATED + "," + COLUMN_FOOD_MODIFIED + ") VALUES(?, ?, ?, ?, ?)";
    private static final String QUERY_FOOD_ITEM = "SELECT * FROM " + TABLE_FOOD_ITEMS + " WHERE " + COLUMN_FOOD_NAME + " = ?";
    private static final String UPDATE_FOOD_ITEM_NAME = "UPDATE " + TABLE_FOOD_ITEMS + " SET " + COLUMN_FOOD_NAME + " = ? WHERE " + COLUMN_FOOD_NAME + " = ?" ;
    private static final String UPDATE_FOOD_ITEM_QUANTITY = "UPDATE " + TABLE_FOOD_ITEMS + " SET " + COLUMN_FOOD_QUANTITY + " = ? WHERE " + COLUMN_FOOD_NAME + " = ?" ;
    private static final String UPDATE_FOOD_ITEM_PRICE = "UPDATE " + TABLE_FOOD_ITEMS + " SET " + COLUMN_FOOD_PRICE + " = ? WHERE " + COLUMN_FOOD_NAME + " = ?" ;
    private static final String UPDATE_FOOD_ITEM_MODIFIED_DATE = "UPDATE " + TABLE_FOOD_ITEMS + " SET " + COLUMN_FOOD_MODIFIED + " = ? WHERE " + COLUMN_FOOD_NAME + " = ?" ;
    private static final String DELETE_FOOD_ITEM = "DELETE FROM " + TABLE_FOOD_ITEMS + " WHERE " + COLUMN_FOOD_NAME + " = ?";
    private static final int ORDER_BY_ASC = 1;
    private Connection connection;



    public void openConnection() throws CustomException {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new CustomException("Couldn't connect to database: "+ e.getMessage());
        }
    }

    public void closeConnection() throws CustomException {
        try {
            if (!Objects.isNull(connection)){
                connection.close();
            }
        } catch (SQLException e) {
            throw new CustomException("Couldn't close connection: " + e.getMessage());
        }
    }

    public FoodItem insertFoodItem(String name, int quantity, double price,
                                  LocalDateTime created, LocalDateTime modified) throws CustomException {
        int rowsAffected = 0;
        FoodItem foodItem = new FoodItem();
        try (PreparedStatement insertFoodItem = connection.prepareStatement(INSERT_FOOD_ITEM,
                Statement.RETURN_GENERATED_KEYS)) {
            insertFoodItem.setString(1, name);
            insertFoodItem.setInt(2, quantity);
            insertFoodItem.setDouble(3, price);
            insertFoodItem.setTimestamp(4, Timestamp.valueOf(created));
            insertFoodItem.setTimestamp(5, Timestamp.valueOf(modified));
            rowsAffected = insertFoodItem.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Couldn't Create Food Item");
        }
        if (rowsAffected > 0) {
            foodItem = queryFoodItem(name);
        }
        return foodItem;
    }

    public FoodItem queryFoodItem(String name) throws CustomException {
        try (PreparedStatement queryFoodItem = connection.prepareStatement(QUERY_FOOD_ITEM)){
            queryFoodItem.setString(1,name);
            ResultSet result = queryFoodItem.executeQuery();
            if(result.next()){
                FoodItem foodItem = new FoodItem();
                foodItem.setId(result.getInt(COLUMN_FOOD_ID));
                foodItem.setName(result.getString(COLUMN_FOOD_NAME));
                foodItem.setQuantity(result.getInt(COLUMN_FOOD_QUANTITY));
                foodItem.setPrice(result.getDouble(COLUMN_FOOD_PRICE));
                foodItem.setCreated(result.getTimestamp(COLUMN_FOOD_CREATED).toInstant());
                foodItem.setModified(result.getTimestamp(COLUMN_FOOD_MODIFIED).toInstant());
                result.close();
                return foodItem;
            } else {
            result.close();
            throw new CustomException("Food Item not found for name: " + name);
            }
        } catch (SQLException e) {
            throw new CustomException("Couldn't Retrieve Food Item");
        }
    }

    public List<FoodItem> queryAllFoodItems(boolean isOrderBy, int sortOrder, String columnName) throws CustomException {
        StringBuilder getFoodItemsQuery = new StringBuilder("SELECT * FROM ");
        getFoodItemsQuery.append(TABLE_FOOD_ITEMS);
        if (isOrderBy) {
            getFoodItemsQuery.append(" ORDER BY ");
            getFoodItemsQuery.append(columnName);
            if(sortOrder == ORDER_BY_ASC){
                getFoodItemsQuery.append(" ASC ");
            } else {
                getFoodItemsQuery.append(" DESC ");
            }
        }
        try(PreparedStatement queryFoodItems = connection.prepareStatement(getFoodItemsQuery.toString())){
            ResultSet result = queryFoodItems.executeQuery();
            List<FoodItem> foodItems = new ArrayList<>();
            while(result.next()){
                FoodItem foodItem = new FoodItem();
                foodItem.setId(result.getInt(COLUMN_FOOD_ID));
                foodItem.setName(result.getString(COLUMN_FOOD_NAME));
                foodItem.setQuantity(result.getInt(COLUMN_FOOD_QUANTITY));
                foodItem.setPrice(result.getDouble(COLUMN_FOOD_PRICE));
                foodItem.setCreated(result.getTimestamp(COLUMN_FOOD_CREATED).toInstant());
                foodItem.setModified(result.getTimestamp(COLUMN_FOOD_MODIFIED).toInstant());
                foodItems.add(foodItem);
            }
            result.close();
            if(foodItems.isEmpty()){
                throw new CustomException("Food Items Storage is Empty!");
            }
            return foodItems;
        } catch (SQLException e){
            throw new CustomException("Couldn't Retrieve Food Items");
        }
    }

    public FoodItem updateFoodItemName(String newName, String foodItemName) throws CustomException {
        try(PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_NAME,
                Statement.RETURN_GENERATED_KEYS)){
            updateFoodItem.setString(1, newName);
            updateFoodItem.setString(2, foodItemName);
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0) {
                updateFoodItemModifiedDate(newName);
                return queryFoodItem(newName);
            }
            else{
                throw new CustomException("Couldn't Update Food Item Name");
            }
        } catch (SQLException e){
            throw new CustomException("Couldn't Update Food Item Name");
        }
    }

    public FoodItem updateFoodItemQuantity(int quantity, String foodItemName) throws CustomException {
        try(PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_QUANTITY,
                Statement.RETURN_GENERATED_KEYS)){
            updateFoodItem.setInt(1, quantity);
            updateFoodItem.setString(2, foodItemName);
            updateFoodItem.executeUpdate();
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0) {
                updateFoodItemModifiedDate(foodItemName);
                return queryFoodItem(foodItemName);
            }
            else{
                throw new CustomException("Couldn't Update Food Item Quantity");
            }
        } catch (SQLException e){
            throw new CustomException("Couldn't Update Food Item Quantity");
        }
    }
    public FoodItem updateFoodItemPrice(double price, String foodItemName) throws CustomException {
        try(PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_PRICE,
                Statement.RETURN_GENERATED_KEYS)){
            updateFoodItem.setDouble(1, price);
            updateFoodItem.setString(2, foodItemName);
            updateFoodItem.executeUpdate();
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0) {
                updateFoodItemModifiedDate(foodItemName);
                return queryFoodItem(foodItemName);
            }
            else{
                throw new CustomException("Couldn't Update Food Item Price");
            }
        } catch (SQLException e){
            throw new CustomException("Couldn't Update Food Item Price");
        }
    }

    public boolean deleteFoodItem(String foodItem) throws CustomException {
        try(PreparedStatement removeFoodItem = connection.prepareStatement(DELETE_FOOD_ITEM,
                Statement.RETURN_GENERATED_KEYS)) {
            removeFoodItem.setString(1, foodItem);
            int rowsAffected = removeFoodItem.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new CustomException("Couldn't Delete Food Item");
        }
    }

    private void updateFoodItemModifiedDate(String foodItemName){
        try(PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_MODIFIED_DATE,
                Statement.RETURN_GENERATED_KEYS)){
            LocalDateTime modifiedTime = LocalDateTime.now();
            DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
            modifiedTime.format(dateTimePattern);
            updateFoodItem.setTimestamp(1, Timestamp.valueOf(modifiedTime));
            updateFoodItem.setString(2, foodItemName);
            updateFoodItem.executeUpdate();
        } catch (SQLException e){
            System.out.println("Couldn't update food last modified date : " + e.getMessage());
        }
    }
}
