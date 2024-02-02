package net.breezeware.dataStore;

import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FoodMenuDataStore {
    public static final String DB_NAME = "cos";
    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    public static final String DB_USER = "cos_usr";
    public static final String DB_PASSWORD= "P@ssw0rd";
    public static final String TABLE_FOOD_MENU = "food_menu";
    public static final String COLUMN_MENU_ID = "_id";
    public static final String COLUMN_MENU_NAME = "name";
    public static final String COLUMN_MENU_AVAILABLE_DAY = "available_day";
    public static final String COLUMN_MENU_CREATED = "created";
    public static final String COLUMN_MENU_MODIFIED = "modified";

    public static final String TABLE_FOOD_MENU_ITEMS = "food_menu_items";
    public static final String COLUMN_FOOD_MENU_ID = "food_menu_id";
    public static final String COLUMN_FOOD_ITEM_ID = "food_item_id";

    public static final String TABLE_FOOD_ITEMS = "food_items";
    public static final String COLUMN_FOOD_ID = "_id";
    public static final String COLUMN_FOOD_ITEM_NAME = "name";


    public static final String INSERT_FOOD_MENU = "INSERT INTO " + TABLE_FOOD_MENU +
            "(" + COLUMN_MENU_NAME + "," + COLUMN_MENU_AVAILABLE_DAY + "," + COLUMN_MENU_CREATED + "," +
            COLUMN_MENU_MODIFIED + ") VALUES(?, ?, ?, ?)";
    public static final String UPDATE_FOOD_MENU_NAME = "UPDATE " + TABLE_FOOD_MENU + " SET " + COLUMN_MENU_NAME + " = ? WHERE " + COLUMN_MENU_NAME + " = ?" ;
    public static final String UPDATE_FOOD_MENU_AVAILABLE_DAY = "UPDATE " + TABLE_FOOD_MENU + " SET " + COLUMN_MENU_AVAILABLE_DAY + " = ? WHERE " + COLUMN_MENU_NAME + " = ?" ;
    public static final String QUERY_FOOD_MENU = "SELECT * FROM " + TABLE_FOOD_MENU + " WHERE " + COLUMN_MENU_NAME + " = ?";
    public static final String QUERY_ALL_FOOD_MENU = "SELECT * FROM " + TABLE_FOOD_MENU;
    public static final String REMOVE_FOOD_MENU = "DELETE FROM " + TABLE_FOOD_MENU + " WHERE " + COLUMN_MENU_NAME + " = ?";


    public static final String QUERY_FOOD_MENU_ITEMS = "SELECT " + COLUMN_FOOD_ITEM_NAME + " FROM " + TABLE_FOOD_ITEMS +
            " INNER JOIN " + TABLE_FOOD_MENU_ITEMS + " ON " + TABLE_FOOD_MENU_ITEMS + "." + COLUMN_FOOD_ITEM_ID  + "="
            +  TABLE_FOOD_ITEMS + "." + COLUMN_FOOD_ID + " WHERE " + TABLE_FOOD_MENU_ITEMS + "." + COLUMN_FOOD_MENU_ID + "=?";
    public static final String INERT_FOOD_ITEM_TO_MENU = "INSERT INTO " + TABLE_FOOD_MENU_ITEMS + "(" + COLUMN_FOOD_MENU_ID
            + "," + COLUMN_FOOD_ITEM_ID + ") VALUES( ?, ?)";
    public static final String REMOVE_FOOD_ITEM_FROM_MENU = "DELETE FROM " + TABLE_FOOD_MENU_ITEMS + " WHERE " + COLUMN_FOOD_MENU_ID + " = ? AND " + COLUMN_FOOD_ITEM_ID + " = ?";
    public static final String REMOVE_ALL_FOOD_ITEMS_FROM_MENU = "DELETE FROM " + TABLE_FOOD_MENU_ITEMS + " WHERE " + COLUMN_FOOD_MENU_ID + " = ?";


    private Connection connection;
    private final FoodItemDataStore foodItemDataStore = new FoodItemDataStore();

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

    public FoodMenu insertFoodMenu(String name, String availableDay, LocalDateTime created, LocalDateTime modified){
        try (PreparedStatement insertFoodMenu = connection.prepareStatement(INSERT_FOOD_MENU)) {
            insertFoodMenu.setString(1, name);
            insertFoodMenu.setString(2, availableDay);
            insertFoodMenu.setTimestamp(3, Timestamp.valueOf(created));
            insertFoodMenu.setTimestamp(4, Timestamp.valueOf(modified));
            int rowsAffected = insertFoodMenu.executeUpdate();
            if(rowsAffected > 0){
                return queryFoodMenu(name);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public FoodMenu queryFoodMenu(String foodMenuName){
        try (PreparedStatement queryFoodItem = connection.prepareStatement(QUERY_FOOD_MENU)){
            queryFoodItem.setString(1,foodMenuName);
            ResultSet result = queryFoodItem.executeQuery();
            if(result.next()){
                FoodMenu foodMenu = new FoodMenu();
                foodMenu.setId(result.getInt(COLUMN_MENU_ID));
                foodMenu.setName(result.getString(COLUMN_MENU_NAME));
                foodMenu.setAvailableDay(convertStringTOList(result.getString(COLUMN_MENU_AVAILABLE_DAY)));
                foodMenu.setCreated(result.getTimestamp(COLUMN_MENU_CREATED).toInstant());
                foodMenu.setModified(result.getTimestamp(COLUMN_MENU_MODIFIED).toInstant());
                result.close();
                return foodMenu;
            } else {
                result.close();
                return null;
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FoodMenu> queryAllFoodMenu(){
        try (PreparedStatement queryAllFoodMenus = connection.prepareStatement(QUERY_ALL_FOOD_MENU)){
            ResultSet result = queryAllFoodMenus.executeQuery();
            List<FoodMenu> foodMenuList = new ArrayList<>();
            while(result.next()){
                foodMenuList.add(queryFoodMenu(result.getString(COLUMN_MENU_NAME)));
            }
            result.close();
            return foodMenuList;
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FoodItem> getFoodMenuItems(int foodMenuId){
        try (PreparedStatement queryFoodMenuItems = connection.prepareStatement(QUERY_FOOD_MENU_ITEMS)){
            queryFoodMenuItems.setInt(1, foodMenuId);
            ResultSet result = queryFoodMenuItems.executeQuery();

            List<FoodItem> foodMenuItems = new ArrayList<>();
            foodItemDataStore.openConnection();
            while (result.next()){
                String foodItemName = result.getString(COLUMN_FOOD_ITEM_NAME);
                foodMenuItems.add(foodItemDataStore.queryFoodItem(foodItemName));
            }
            foodItemDataStore.closeConnection();
            result.close();
            return foodMenuItems;
        } catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public boolean addFoodItemsToMenu(int foodMenuId, int foodItemId){
        try(PreparedStatement addFoodItemsToMenu = connection.prepareStatement(INERT_FOOD_ITEM_TO_MENU)){
            addFoodItemsToMenu.setInt(1,foodMenuId);
            addFoodItemsToMenu.setInt(2,foodItemId);
            int rowsAffected = addFoodItemsToMenu.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean removeFoodItemFromMenu(int foodMenuId, int foodItemId){
        try(PreparedStatement removeFoodItemsFromMenu = connection.prepareStatement(REMOVE_FOOD_ITEM_FROM_MENU)){
            removeFoodItemsFromMenu.setInt(1,foodMenuId);
            removeFoodItemsFromMenu.setInt(2,foodItemId);
            int rowsAffected = removeFoodItemsFromMenu.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean removeAllFoodItemsFromMenu(int foodMenuId){
        try(PreparedStatement removeAllFoodItemsFromMenu = connection.prepareStatement(REMOVE_ALL_FOOD_ITEMS_FROM_MENU)) {
            removeAllFoodItemsFromMenu.setInt(1, foodMenuId);
            return removeAllFoodItemsFromMenu.executeUpdate() > 0;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public boolean removeFoodMenu(String foodMenuName){
        try(PreparedStatement removeFoodMenu = connection.prepareStatement(REMOVE_FOOD_MENU)){
            removeFoodMenu.setString(1, foodMenuName);
            int rowsAffected = removeFoodMenu.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public FoodMenu updateFoodMenuName(String newFoodMenuName, String foodMenuName){
        try(PreparedStatement updateFoodMenuName = connection.prepareStatement(UPDATE_FOOD_MENU_NAME)){
            updateFoodMenuName.setString(1,newFoodMenuName);
            updateFoodMenuName.setString(2,foodMenuName);
            int rowsAffected = updateFoodMenuName.executeUpdate();
            if (rowsAffected > 0){
                return queryFoodMenu(newFoodMenuName);
            }
            return null;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public FoodMenu updateFoodMenuAvailableDay(String availableDays, String foodMenuName){
        try(PreparedStatement updateFoodMenuAvailableDay = connection.prepareStatement(UPDATE_FOOD_MENU_AVAILABLE_DAY)){
            updateFoodMenuAvailableDay.setString(1,availableDays);
            updateFoodMenuAvailableDay.setString(2,foodMenuName);
            int rowsAffected = updateFoodMenuAvailableDay.executeUpdate();
            if (rowsAffected > 0){
                return queryFoodMenu(foodMenuName);
            }
            return null;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private List<AvailableDay> convertStringTOList(String availableDay){
        List<String> availableDays = Arrays.asList(availableDay.split(","));
        return availableDays.stream()
                .map(String::trim)
                .map(AvailableDay::valueOf)
                .toList();
    }

}
