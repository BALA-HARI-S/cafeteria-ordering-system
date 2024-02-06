package net.breezeware.dataStore;

import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OrderDataStore {
    private static final String DB_NAME = "cos";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String DB_USER = "cos_usr";
    private static final String DB_PASSWORD = "P@ssw0rd";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDERED_FOOD_ITEMS = "ordered_food_items";

    private static final String COLUMN_ORDER_ID = "_id";
    private static final String COLUMN_ORDER_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_ORDER_TOTAL_COST = "total_cost";
    private static final String COLUMN_ORDER_STATUS = "order_status";
    private static final String COLUMN_ORDER_CREATED = "created";

    private static final String COLUMN_ORDERED_FOOD_ITEMS_ORDER_ID = "order_id";
    private static final String COLUMN_ORDERED_FOOD_ITEMS_FOOD_ITEM_NAME = "food_item_name";
    private static final String COLUMN_ORDERED_FOOD_ITEMS_QUANTITY = "quantity";


    private static final String INSERT_INTO_ORDERS = "INSERT INTO " + TABLE_ORDERS + "(" +COLUMN_ORDER_TOTAL_COST + ","
            + COLUMN_ORDER_STATUS + "," + COLUMN_ORDER_CREATED + ") VALUES(?,?,?)";
    private static final String QUERY_ORDER_ORDER_ID = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_ID + " =?";
    private static final String UPDATE_ORDER_STATUS = "UPDATE " + TABLE_ORDERS + " SET "+ COLUMN_ORDER_STATUS + " =? WHERE " + COLUMN_ORDER_ID + " =?";
    private static final String QUERY_ORDER_BY_ORDER_STATUS = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_STATUS + " =?";

    private static final String INSERT_INTO_ORDERED_FOOD_ITEMS = "INSERT INTO " + TABLE_ORDERED_FOOD_ITEMS + "(" +
            COLUMN_ORDERED_FOOD_ITEMS_ORDER_ID + "," + COLUMN_ORDERED_FOOD_ITEMS_FOOD_ITEM_NAME + "," +
            COLUMN_ORDERED_FOOD_ITEMS_QUANTITY + ") VALUES(?,?,?)";
    private static final String QUERY_ORDERED_FOOD_ITEMS = "SELECT " + COLUMN_ORDERED_FOOD_ITEMS_FOOD_ITEM_NAME +
            "," + COLUMN_ORDERED_FOOD_ITEMS_QUANTITY + " FROM " + TABLE_ORDERED_FOOD_ITEMS + " WHERE " +
            COLUMN_ORDERED_FOOD_ITEMS_ORDER_ID + " =?";

    private static final String DELETE_FROM_ORDERED_FOOD_ITEMS = "DELETE FROM " + TABLE_ORDERED_FOOD_ITEMS + " WHERE " +
            COLUMN_ORDERED_FOOD_ITEMS_ORDER_ID + " = ? AND " + COLUMN_ORDERED_FOOD_ITEMS_FOOD_ITEM_NAME + " = ?";
    private Connection connection;
    public void openConnection() throws CustomException {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new CustomException("Couldn't Connect to Database: " + e.getMessage());
        }
    }

    public void closeConnection() throws CustomException {
        try {
            if (!Objects.isNull(connection)){
                connection.close();
            }
        } catch (SQLException e) {
            throw new CustomException("Couldn't Close Connection: " + e.getMessage());
        }
    }

    public Order insertIntoOrders(double totalCost, String orderStatus, LocalDateTime created) throws CustomException {
        try (PreparedStatement insertIntoOrders = connection.prepareStatement(INSERT_INTO_ORDERS, Statement.RETURN_GENERATED_KEYS)) {
            insertIntoOrders.setDouble(1,totalCost);
            insertIntoOrders.setString(2,orderStatus);
            insertIntoOrders.setTimestamp(3, Timestamp.valueOf(created));
            insertIntoOrders.executeUpdate();
            ResultSet resultSet = insertIntoOrders.getGeneratedKeys();
            if(resultSet.next()){
                int orderId = resultSet.getInt(COLUMN_ORDER_ID);
                Order order = queryOrder(orderId);
                resultSet.close();
                return order;
            } else {
                resultSet.close();
                throw new CustomException("couldn't Query Cart Order");
            }

        } catch (SQLException e){
            throw new CustomException("Couldn't Create Order. " + e.getMessage());
        }
    }

    public void insertIntoOrderedFoodItems(int orderId, String foodItemName, int foodItemQuantity) throws CustomException {
            try(PreparedStatement insertOrderedFoodItems = connection.prepareStatement(INSERT_INTO_ORDERED_FOOD_ITEMS)){
                insertOrderedFoodItems.setInt(1, orderId);
                insertOrderedFoodItems.setString(2,foodItemName);
                insertOrderedFoodItems.setInt(3, foodItemQuantity);
                insertOrderedFoodItems.executeUpdate();
            }catch (SQLException e){
                throw new CustomException("Couldn't Insert Food Items to Ordered Food Items." + e.getMessage());
            }
    }

    public Order queryOrder(int orderId) throws CustomException{
        try(PreparedStatement queryOrder = connection.prepareStatement(QUERY_ORDER_ORDER_ID)){
            queryOrder.setInt(1,orderId);
            ResultSet resultSet = queryOrder.executeQuery();
            if(resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getInt(COLUMN_ORDER_ID));
                order.setTotalCost(resultSet.getDouble(COLUMN_ORDER_TOTAL_COST));
                order.setOrderStatus(OrderStatus.valueOf(resultSet.getString(COLUMN_ORDER_STATUS)));
                order.setCreated(resultSet.getTimestamp(COLUMN_ORDER_CREATED).toInstant());
                resultSet.close();
                return order;
            } else {
                resultSet.close();
                throw new CustomException("Order not found for Order Id: " + orderId);
            }
        } catch (SQLException e){
            throw new CustomException("Couldn't Retrieve the Order Record. " + e.getMessage());
        }
    }

    public List<Order> queryOrderByStatus(String orderStatus) throws CustomException {
        try(PreparedStatement queryOrder = connection.prepareStatement(QUERY_ORDER_BY_ORDER_STATUS)){
            queryOrder.setString(1,orderStatus);
            ResultSet resultSet = queryOrder.executeQuery();
            List<Order> orders = new ArrayList<>();
            while(resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getInt(COLUMN_ORDER_ID));
                order.setTotalCost(resultSet.getDouble(COLUMN_ORDER_TOTAL_COST));
                order.setOrderStatus(OrderStatus.valueOf(resultSet.getString(COLUMN_ORDER_STATUS)));
                order.setCreated(resultSet.getTimestamp(COLUMN_ORDER_CREATED).toInstant());
                orders.add(order);
            }
            resultSet.close();
            return orders;
        } catch (SQLException e){
            throw new CustomException("Couldn't Retrieve the Order Records. " + e.getMessage());
        }
    }

    public HashMap<String, Integer> queryOrderedFoodItems(int orderId) throws CustomException {
        try(PreparedStatement queryOrderedFoodItems = connection.prepareStatement(QUERY_ORDERED_FOOD_ITEMS)){
            queryOrderedFoodItems.setInt(1, orderId);
            ResultSet resultSet = queryOrderedFoodItems.executeQuery();
            HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
            while(resultSet.next()){
                foodItemsQuantityMap.put(resultSet.getString(COLUMN_ORDERED_FOOD_ITEMS_FOOD_ITEM_NAME),
                        resultSet.getInt(COLUMN_ORDERED_FOOD_ITEMS_QUANTITY));
            }
            resultSet.close();
            return foodItemsQuantityMap;
        } catch (SQLException e){
            throw new CustomException("Couldn't Retrieve the Ordered FoodItems. " + e.getMessage());
        }
    }

    public String updateOrderStatus(int orderId, String orderStatus) throws CustomException {
        try(PreparedStatement updateOrderStatus = connection.prepareStatement(UPDATE_ORDER_STATUS,
                Statement.RETURN_GENERATED_KEYS)) {
            updateOrderStatus.setString(1, orderStatus);
            updateOrderStatus.setInt(2, orderId);
            updateOrderStatus.executeUpdate();
            ResultSet resultSet = updateOrderStatus.getGeneratedKeys();
            if(resultSet.next()){
                String updatedOrderStatus = resultSet.getString(COLUMN_ORDER_STATUS);
                resultSet.close();
                return updatedOrderStatus;
            } else {
                resultSet.close();
                throw new CustomException("Order not Found!. ");
            }
        } catch (SQLException e){
            throw new CustomException("Couldn't Update the Order Status. " + e.getMessage());
        }
    }

    public boolean deleteCartOrderFoodItem(int orderId, String foodItemName) throws CustomException {
        try(PreparedStatement deleteCartOrderFoodItem= connection.prepareStatement(DELETE_FROM_ORDERED_FOOD_ITEMS)){
            deleteCartOrderFoodItem.setInt(1,orderId);
            deleteCartOrderFoodItem.setString(2,foodItemName);
            return deleteCartOrderFoodItem.executeUpdate() > 0;
        } catch (SQLException e){
            throw new CustomException("Couldn't Delete Cart Order Food Item. " + e.getMessage());
        }
    }
}
