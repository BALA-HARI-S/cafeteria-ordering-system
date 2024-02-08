package net.breezeware.dataStore;

import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.FoodItemManager;
import net.breezeware.service.impl.FoodItemManagerImplementation;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FoodMenuDataStore {
    private static final String DB_NAME = "cos";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String DB_USER = "cos_usr";
    private static final String DB_PASSWORD= "P@ssw0rd";
    private static final String TABLE_FOOD_MENU = "food_menu";
    private static final String COLUMN_MENU_ID = "_id";
    private static final String COLUMN_MENU_NAME = "name";
    private static final String COLUMN_MENU_AVAILABLE_DAY = "available_day";
    private static final String COLUMN_MENU_CREATED = "created";
    private static final String COLUMN_MENU_MODIFIED = "modified";

    private static final String TABLE_FOOD_MENU_ITEMS_MAP = "food_menu_items_map";
    private static final String COLUMN_FOOD_MENU_ID = "food_menu_id";
    private static final String COLUMN_FOOD_ITEM_ID = "food_item_id";

    private static final String TABLE_FOOD_ITEMS = "food_items";
    private static final String COLUMN_FOOD_ID = "_id";
    private static final String COLUMN_FOOD_ITEM_NAME = "name";


    private static final String INSERT_FOOD_MENU = "INSERT INTO " + TABLE_FOOD_MENU +
            "(" + COLUMN_MENU_NAME + "," + COLUMN_MENU_AVAILABLE_DAY + "," + COLUMN_MENU_CREATED + "," +
            COLUMN_MENU_MODIFIED + ") VALUES(?, ?, ?, ?)";
    private static final String UPDATE_FOOD_MENU_NAME = "UPDATE " + TABLE_FOOD_MENU + " SET " + COLUMN_MENU_NAME + " = ? WHERE " + COLUMN_MENU_NAME + " = ?" ;
    private static final String UPDATE_FOOD_MENU_AVAILABLE_DAY = "UPDATE " + TABLE_FOOD_MENU + " SET " + COLUMN_MENU_AVAILABLE_DAY + " = ? WHERE " + COLUMN_MENU_NAME + " = ?" ;
    private static final String QUERY_FOOD_MENU = "SELECT * FROM " + TABLE_FOOD_MENU + " WHERE " + COLUMN_MENU_NAME + " = ?";
    private static final String REMOVE_FOOD_MENU = "DELETE FROM " + TABLE_FOOD_MENU + " WHERE " + COLUMN_MENU_NAME + " = ?";


    private static final String QUERY_FOOD_MENU_ITEMS = "SELECT " + COLUMN_FOOD_ITEM_NAME + " FROM " + TABLE_FOOD_ITEMS +
            " INNER JOIN " + TABLE_FOOD_MENU_ITEMS_MAP + " ON " + TABLE_FOOD_MENU_ITEMS_MAP + "." + COLUMN_FOOD_ITEM_ID  + "="
            +  TABLE_FOOD_ITEMS + "." + COLUMN_FOOD_ID + " WHERE " + TABLE_FOOD_MENU_ITEMS_MAP + "." + COLUMN_FOOD_MENU_ID + "=?";

    private static final String QUERY_FOOD_MENU_ITEM = "SELECT " + COLUMN_FOOD_ITEM_NAME + " FROM " + TABLE_FOOD_ITEMS +
            " INNER JOIN " + TABLE_FOOD_MENU_ITEMS_MAP + " ON " + TABLE_FOOD_MENU_ITEMS_MAP + "." + COLUMN_FOOD_ITEM_ID  + "="
            +  TABLE_FOOD_ITEMS + "." + COLUMN_FOOD_ID + " WHERE " + TABLE_FOOD_MENU_ITEMS_MAP + "." + COLUMN_FOOD_MENU_ID
            + "=? AND " + TABLE_FOOD_MENU_ITEMS_MAP + "." + COLUMN_FOOD_ITEM_ID + " =?";
    private static final String INERT_FOOD_ITEM_TO_MENU = "INSERT INTO " + TABLE_FOOD_MENU_ITEMS_MAP + "(" + COLUMN_FOOD_MENU_ID
            + "," + COLUMN_FOOD_ITEM_ID + ") VALUES( ?, ?)";
    private static final String REMOVE_FOOD_ITEM_FROM_MENU = "DELETE FROM " + TABLE_FOOD_MENU_ITEMS_MAP + " WHERE " + COLUMN_FOOD_MENU_ID + " = ? AND " + COLUMN_FOOD_ITEM_ID + " = ?";
    private static final String REMOVE_ALL_FOOD_ITEMS_FROM_MENU = "DELETE FROM " + TABLE_FOOD_MENU_ITEMS_MAP + " WHERE " + COLUMN_FOOD_MENU_ID + " = ?";
    public static final String UPDATE_FOOD_MENU_MODIFIED_DATE = "UPDATE " + TABLE_FOOD_MENU + " SET " + COLUMN_MENU_MODIFIED + " = ? WHERE " + COLUMN_MENU_NAME + " = ?" ;
    private static final int ORDER_BY_ASC = 1;

    private Connection connection;
    private final FoodItemManager foodItemManager = new FoodItemManagerImplementation();

    public void openConnection() throws CustomException {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new CustomException("Couldn't Connect to Database. "+ e.getMessage());
        }
    }

    public void closeConnection() throws CustomException {
        try {
            if (!Objects.isNull(connection)){
                connection.close();
            }
        } catch (SQLException e) {
            throw new CustomException("Couldn't Close the Connection. "+ e.getMessage());
        }
    }

    public FoodMenu insertFoodMenu(String name, String availableDay, LocalDateTime created,
                                   LocalDateTime modified) throws CustomException {
        try (PreparedStatement insertFoodMenu = connection.prepareStatement(INSERT_FOOD_MENU)) {
            insertFoodMenu.setString(1, name);
            insertFoodMenu.setString(2, availableDay);
            insertFoodMenu.setTimestamp(3, Timestamp.valueOf(created));
            insertFoodMenu.setTimestamp(4, Timestamp.valueOf(modified));
            insertFoodMenu.executeUpdate();
            return queryFoodMenu(name);
        } catch (SQLException e) {
            throw new CustomException("Couldn't Create Food Menu. "+ e.getMessage());
        }
    }

    public FoodMenu queryFoodMenu(String foodMenuName) throws CustomException {
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
            }
            else {
                result.close();
                throw new CustomException("Food Menu not found for name: " + foodMenuName);
            }
        } catch (SQLException | RuntimeException e) {
            throw new CustomException("Couldn't Query Food Menu. " + e.getMessage());
        }
    }

    public List<FoodMenu> queryAllFoodMenu(boolean isOrderBy, int sortOrder, String columnName) throws CustomException {
        StringBuilder getFoodMenusQuery = new StringBuilder("SELECT * FROM ");
        getFoodMenusQuery.append(TABLE_FOOD_MENU);
        if (isOrderBy) {
            getFoodMenusQuery.append(" ORDER BY ");
            getFoodMenusQuery.append(columnName);
            if(sortOrder == ORDER_BY_ASC){
                getFoodMenusQuery.append(" ASC ");
            } else {
                getFoodMenusQuery.append(" DESC ");
            }
        }
        try (PreparedStatement queryAllFoodMenus = connection.prepareStatement(getFoodMenusQuery.toString())){
            ResultSet result = queryAllFoodMenus.executeQuery();
            List<FoodMenu> foodMenuList = new ArrayList<>();
            while(result.next()){
                foodMenuList.add(queryFoodMenu(result.getString(COLUMN_MENU_NAME)));
            }
            result.close();
            return foodMenuList;
        } catch (SQLException e){
            throw new CustomException("Couldn't Query Food Menus. " + e.getMessage());
        }
    }

    public List<FoodItem> queryFoodMenuItems(int foodMenuId) throws CustomException {
        try (PreparedStatement queryFoodMenuItems = connection.prepareStatement(QUERY_FOOD_MENU_ITEMS)){
            queryFoodMenuItems.setInt(1, foodMenuId);
            ResultSet result = queryFoodMenuItems.executeQuery();
            List<FoodItem> foodMenuItems = new ArrayList<>();
            while (result.next()){
                String foodItemName = result.getString(COLUMN_FOOD_ITEM_NAME);
                foodMenuItems.add(foodItemManager.retrieveFoodItem(foodItemName));
            }
            result.close();
            return foodMenuItems;
        } catch (SQLException e){
            throw new CustomException("Couldn't Query Food Menu Items. " + e.getMessage());
        }
    }

    public FoodItem queryFoodMenuItem(int foodMenuId, int foodItemId) throws CustomException {
        try (PreparedStatement queryFoodMenuItems = connection.prepareStatement(QUERY_FOOD_MENU_ITEM)){
            queryFoodMenuItems.setInt(1, foodMenuId);
            queryFoodMenuItems.setInt(2, foodItemId);
            ResultSet result = queryFoodMenuItems.executeQuery();
            if(result.next()){
                String foodItemName = result.getString(COLUMN_FOOD_ITEM_NAME);
                FoodItem foodMenuItem = foodItemManager.retrieveFoodItem(foodItemName);
                result.close();
                return foodMenuItem;
            } else {
                result.close();
                throw new CustomException("Food Item Not in the Menu.");
            }
        } catch (SQLException e){
            throw new CustomException("Couldn't Query Food Menu Item. " + e.getMessage());
        }
    }

    public boolean addFoodItemsToMenu(int foodMenuId, int foodItemId) throws CustomException {
        try(PreparedStatement addFoodItemsToMenu = connection.prepareStatement(INERT_FOOD_ITEM_TO_MENU)){
            addFoodItemsToMenu.setInt(1,foodMenuId);
            addFoodItemsToMenu.setInt(2,foodItemId);
            int rowsAffected = addFoodItemsToMenu.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new CustomException("Couldn't Add Food Item to Menu. " + e.getMessage());
        }
    }

    public FoodMenu updateFoodMenuName(String newFoodMenuName, String foodMenuName) throws CustomException {
        try(PreparedStatement updateFoodMenuName = connection.prepareStatement(UPDATE_FOOD_MENU_NAME)){
            updateFoodMenuName.setString(1,newFoodMenuName);
            updateFoodMenuName.setString(2,foodMenuName);
            updateFoodMenuName.executeUpdate();
            updateFoodMenuModifiedDate(newFoodMenuName);
            return queryFoodMenu(newFoodMenuName);
        } catch(SQLException e){
            throw new CustomException("Couldn't Update Food Menu Name. " + e.getMessage());
        }
    }

    public FoodMenu updateFoodMenuAvailableDay(String availableDays, String foodMenuName) throws CustomException {
        try(PreparedStatement updateFoodMenuAvailableDay = connection.prepareStatement(UPDATE_FOOD_MENU_AVAILABLE_DAY)){
            updateFoodMenuAvailableDay.setString(1,availableDays);
            updateFoodMenuAvailableDay.setString(2,foodMenuName);
            updateFoodMenuAvailableDay.executeUpdate();
            updateFoodMenuModifiedDate(foodMenuName);
            return queryFoodMenu(foodMenuName);
        } catch(SQLException e){
            throw new CustomException("Couldn't Update Food Menu Available Days. " + e.getMessage());
        }
    }

    public boolean deleteFoodItemFromMenu(int foodMenuId, int foodItemId) throws CustomException {
        try(PreparedStatement removeFoodItemsFromMenu = connection.prepareStatement(REMOVE_FOOD_ITEM_FROM_MENU)){
            removeFoodItemsFromMenu.setInt(1,foodMenuId);
            removeFoodItemsFromMenu.setInt(2,foodItemId);
            int rowsAffected = removeFoodItemsFromMenu.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new CustomException("Couldn't Remove Food Menu Item. " + e.getMessage());
        }
    }

    public boolean deleteAllFoodItemsFromMenu(int foodMenuId) throws CustomException {
        try(PreparedStatement removeAllFoodItemsFromMenu = connection.prepareStatement(REMOVE_ALL_FOOD_ITEMS_FROM_MENU)) {
            removeAllFoodItemsFromMenu.setInt(1, foodMenuId);
            return removeAllFoodItemsFromMenu.executeUpdate() > 0;
        } catch (SQLException e){
            throw new CustomException("Couldn't Remove Food Menu Items. " + e.getMessage());
        }
    }
    public boolean deleteFoodMenu(String foodMenuName) throws CustomException {
        try(PreparedStatement removeFoodMenu = connection.prepareStatement(REMOVE_FOOD_MENU)){
            removeFoodMenu.setString(1, foodMenuName);
            int rowsAffected = removeFoodMenu.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new CustomException("Couldn't Remove Food Menu. " + e.getMessage());
        }
    }


    private List<AvailableDay> convertStringTOList(String availableDay){
        List<String> availableDays = Arrays.asList(availableDay.split(","));
        return availableDays.stream()
                .map(String::trim)
                .map(AvailableDay::valueOf)
                .toList();
    }

    private void updateFoodMenuModifiedDate(String foodMenuName) throws CustomException {
        try(PreparedStatement updateFoodItem = connection.prepareStatement(UPDATE_FOOD_MENU_MODIFIED_DATE,
                Statement.RETURN_GENERATED_KEYS)){
            LocalDateTime modifiedTime = LocalDateTime.now();
            DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
            modifiedTime.format(dateTimePattern);
            updateFoodItem.setTimestamp(1, Timestamp.valueOf(modifiedTime));
            updateFoodItem.setString(2, foodMenuName);
            updateFoodItem.executeUpdate();
        } catch (SQLException e){
            throw new CustomException("Couldn't Modify Food Menu Modified Date. " + e.getMessage());
        }
    }
}
