package net.breezeware.dataStore;

import net.breezeware.entity.FoodItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodItemDataStore {
    public static final String DB_NAME = "cos";
    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    public static final String DB_USER = "cos_usr";
    public static final String DB_PASSWORD= "P@ssw0rd";
    public static final String TABLE_FOOD_ITEMS = "food_items";
    public static final String COLUMN_FOOD_ID = "food_id";
    public static final String COLUMN_FOOD_NAME = "name";
    public static final String COLUMN_FOOD_QUANTITY = "quantity";
    public static final String COLUMN_FOOD_PRICE = "price";
    public static final String COLUMN_FOOD_CREATED = "created";
    public static final String COLUMN_FOOD_MODIFIED = "modified";

    public static final String INSERT_FOOD_ITEM = "INSERT INTO " + TABLE_FOOD_ITEMS +
            "(" + COLUMN_FOOD_NAME + "," + COLUMN_FOOD_QUANTITY + "," + COLUMN_FOOD_PRICE + "," +
            COLUMN_FOOD_CREATED + "," + COLUMN_FOOD_MODIFIED + ") VALUES(?, ?, ?, ?, ?)";
    public static final String QUERY_FOOD_ITEM = "SELECT * FROM " + TABLE_FOOD_ITEMS + " WHERE name = ?";
    public static final String UPDATE_FOOD_ITEM_NAME = "UPDATE " + TABLE_FOOD_ITEMS + " SET name = ? WHERE name = ?" ;
    public static final String UPDATE_FOOD_ITEM_QUANTITY = "UPDATE " + TABLE_FOOD_ITEMS + " SET quantity = ? WHERE name = ?" ;
    public static final String UPDATE_FOOD_ITEM_PRICE = "UPDATE " + TABLE_FOOD_ITEMS + " SET price = ? WHERE name = ?" ;
    public static final String UPDATE_FOOD_ITEM_MODIFIED_DATE = "UPDATE " + TABLE_FOOD_ITEMS + " SET modified = ? WHERE name = ?" ;
    public static final String DELETE_FOOD_ITEM = "DELETE FROM " + TABLE_FOOD_ITEMS + " WHERE name = ?";
    public static final int ORDER_BY_ASC = 2;
    private Connection connection;



    public void openConnection(){
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
        }
    }

    public void closeConnection(){
        try {
            if (!Objects.isNull(connection)){
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public boolean insertFoodItem(String name, int quantity, double price,
                                  LocalDateTime created, LocalDateTime modified){
        try {
            PreparedStatement insertFoodItem = connection.prepareStatement(INSERT_FOOD_ITEM,
                    Statement.RETURN_GENERATED_KEYS);
            insertFoodItem.setString(1, capitalizeFirstLetter(name));
            insertFoodItem.setInt(2, quantity);
            insertFoodItem.setDouble(3, price);
            insertFoodItem.setTimestamp(4, Timestamp.valueOf(created));
            insertFoodItem.setTimestamp(5, Timestamp.valueOf(modified));
            int rowsAffected = insertFoodItem.executeUpdate();
            if (rowsAffected> 0){
                insertFoodItem.close();
                return true;
            }
            insertFoodItem.close();
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't insert food item : " + e.getMessage());
            return false;
        }
    }

    public FoodItem queryFoodItem(String name){
        try {
            PreparedStatement queryFoodItem = connection.prepareStatement(QUERY_FOOD_ITEM);
            queryFoodItem.setString(1,name);
            ResultSet result = queryFoodItem.executeQuery();
            FoodItem foodItem = new FoodItem();
            while(result.next()){
                foodItem.setId(result.getInt("_id"));
                foodItem.setName(result.getString("name"));
                foodItem.setQuantity(result.getInt("quantity"));
                foodItem.setPrice(result.getDouble("price"));
                foodItem.setCreated(result.getTimestamp("created").toInstant());
                foodItem.setModified(result.getTimestamp("modified").toInstant());
            }
            result.close();
            queryFoodItem.close();
            return foodItem;
        } catch (SQLException e) {
            System.out.println("Couldn't query food item : " + e.getMessage());
            return null;
        }
    }

    public List<FoodItem> queryAllFoodItems(boolean isOrderBy, int sortOrder, String columnName){
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
        try{
            PreparedStatement queryFoodItems = connection.prepareStatement(getFoodItemsQuery.toString());
            ResultSet result = queryFoodItems.executeQuery();
            List<FoodItem> foodItems = new ArrayList<>();
            while(result.next()){
                FoodItem foodItem = new FoodItem();
                foodItem.setId(result.getInt("_id"));
                foodItem.setName(result.getString("name"));
                foodItem.setQuantity(result.getInt("quantity"));
                foodItem.setPrice(result.getDouble("price"));
                foodItem.setCreated(result.getTimestamp("created").toInstant());
                foodItem.setModified(result.getTimestamp("modified").toInstant());
                foodItems.add(foodItem);
            }
            result.close();
            queryFoodItems.close();
            return foodItems;

        } catch (SQLException e){
            System.out.println("Couldn't query food items : " + e.getMessage());
            return null;
        }
    }

    public FoodItem updateFoodItemName(String newName, String foodItemName){
        try{
            PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_NAME,
                    Statement.RETURN_GENERATED_KEYS);
            updateFoodItem.setString(1, newName);
            updateFoodItem.setString(2, foodItemName);
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKey = updateFoodItem.getGeneratedKeys();
                FoodItem foodItem = queryFoodItem(generatedKey.getString("name"));
                updateFoodItemModifiedDate(foodItem.getName());
                generatedKey.close();
                updateFoodItem.close();
                return foodItem;
            }
            updateFoodItem.close();
            return null;

        } catch (SQLException e){
            System.out.println("Couldn't update food item name : " + e.getMessage());
            return null;
        }
    }

    public FoodItem updateFoodItemQuantity(int quantity, String foodItemName){
        try{
            PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_QUANTITY,
                    Statement.RETURN_GENERATED_KEYS);
            updateFoodItem.setInt(1, quantity);
            updateFoodItem.setString(2, foodItemName);
            updateFoodItem.executeUpdate();
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKey = updateFoodItem.getGeneratedKeys();
                FoodItem foodItem = queryFoodItem(generatedKey.getString("name"));
                updateFoodItemModifiedDate(foodItem.getName());
                generatedKey.close();
                updateFoodItem.close();
                return foodItem;
            }
            updateFoodItem.close();
            return null;
        } catch (SQLException e){
            System.out.println("Couldn't update food item quantity : " + e.getMessage());
            return null;
        }
    }
    public FoodItem updateFoodItemPrice(double price, String foodItemName){
        try{
            PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_PRICE,
                    Statement.RETURN_GENERATED_KEYS);
            updateFoodItem.setDouble(1, price);
            updateFoodItem.setString(2, foodItemName);
            updateFoodItem.executeUpdate();
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKey = updateFoodItem.getGeneratedKeys();
                FoodItem foodItem = queryFoodItem(generatedKey.getString("name"));
                updateFoodItemModifiedDate(foodItem.getName());
                generatedKey.close();
                updateFoodItem.close();
                return foodItem;
            }
            updateFoodItem.close();
            return null;
        } catch (SQLException e){
            System.out.println("Couldn't update food item price : " + e.getMessage());
            return null;
        }
    }

    private boolean updateFoodItemModifiedDate(String foodItemName){
        try{
            PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_ITEM_MODIFIED_DATE,
                    Statement.RETURN_GENERATED_KEYS);

            LocalDateTime modifiedTime = LocalDateTime.now();
            DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
            modifiedTime.format(dateTimePattern);

            updateFoodItem.setTimestamp(1, Timestamp.valueOf(modifiedTime));
            updateFoodItem.setString(2, foodItemName);
            int rowsAffected = updateFoodItem.executeUpdate();
            if (rowsAffected > 0){
                updateFoodItem.close();
                return true;
            }
            updateFoodItem.close();
            return false;
        } catch (SQLException e){
            System.out.println("Couldn't update food last modified date : " + e.getMessage());
            return false;
        }
    }

    public boolean removeFoodItem(String foodItem) {
        try {
            PreparedStatement removeFoodItem = connection.prepareStatement(DELETE_FOOD_ITEM,
                    Statement.RETURN_GENERATED_KEYS);
            removeFoodItem.setString(1, foodItem);
            int rowsAffected = removeFoodItem.executeUpdate();
            if(rowsAffected > 0){
                removeFoodItem.close();
                return true;
            }
            removeFoodItem.close();
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't remove food item : " + e.getMessage());
            return false;
        }
    }

    private static String capitalizeFirstLetter(String input) {
        StringBuilder result = new StringBuilder();
        String[] words = input.split("\\s");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - 1);
        }
        return result.toString();
    }
}
