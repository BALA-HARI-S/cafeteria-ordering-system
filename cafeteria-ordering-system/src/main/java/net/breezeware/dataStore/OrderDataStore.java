package net.breezeware.dataStore;

import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrderDataStore {
    private static final String DB_NAME = "cos";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String DB_USER = "cos_usr";
    private static final String DB_PASSWORD = "P@ssw0rd";
    private static final String TABLE_ORDERS = "orders";

    private static final String COLUMN_ORDER_ID = "_id";
    private static final String COLUMN_ORDER_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_ORDER_ORDERED_FOOD_ITEMS = "ordered_food_items";
    private static final String COLUMN_ORDER_DELIVERY_LOCATION = "delivery_location";
    private static final String COLUMN_ORDER_DELIVERY_DATE_TIME = "delivery_date_time";
    private static final String COLUMN_ORDER_TOTAL_COST = "total_cost";
    private static final String COLUMN_ORDER_STATUS = "order_status";
    private static final String COLUMN_ORDER_CREATED = "created";

    private static final String INSERT_INTO_ORDERS = "INSERT INTO " + TABLE_ORDERS + "(" + COLUMN_ORDER_CUSTOMER_NAME + "," +
            COLUMN_ORDER_ORDERED_FOOD_ITEMS + "," + COLUMN_ORDER_DELIVERY_LOCATION + "," + COLUMN_ORDER_DELIVERY_DATE_TIME
            + "," + COLUMN_ORDER_TOTAL_COST + "," + COLUMN_ORDER_STATUS + "," + COLUMN_ORDER_CREATED
            + ") VALUES(?,?,?,?,?,?,?)";
    private static final String QUERY_ORDER_ORDER_ID = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_ID + " =?";
    private static final String UPDATE_ORDER_STATUS = "UPDATE " + TABLE_ORDERS + " SET "+ COLUMN_ORDER_STATUS + " =? WHERE " + COLUMN_ORDER_ID + " =?";
    private static final String QUERY_ORDER_BY_ORDER_STATUS = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_STATUS + " =?";
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

    public Order insertOrder(String customer_name, String orderedFoodItems, String deliveryLocation, LocalDateTime deliveryDateTime,
                                  double totalCost, String orderStatus, LocalDateTime created) {
        try (PreparedStatement insertIntoOrders = connection.prepareStatement(INSERT_INTO_ORDERS, Statement.RETURN_GENERATED_KEYS)) {
            insertIntoOrders.setString(1,customer_name);
            insertIntoOrders.setString(2,orderedFoodItems);
            insertIntoOrders.setString(3,deliveryLocation);
            insertIntoOrders.setTimestamp(4, Timestamp.valueOf(deliveryDateTime));
            insertIntoOrders.setDouble(5,totalCost);
            insertIntoOrders.setString(6,orderStatus);
            insertIntoOrders.setTimestamp(7, Timestamp.valueOf(created));
            int rowsAffected = insertIntoOrders.executeUpdate();
            if(rowsAffected > 0){
                ResultSet resultSet = insertIntoOrders.getGeneratedKeys();
                if (resultSet.next()){
                    return queryOrder(resultSet.getInt(COLUMN_ORDER_ID));
                }
                resultSet.close();
            }
            return null;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Order queryOrder(int orderId){
        try(PreparedStatement queryOrder = connection.prepareStatement(QUERY_ORDER_ORDER_ID)){
            queryOrder.setInt(1,orderId);
            ResultSet resultSet = queryOrder.executeQuery();
            if(resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getInt(COLUMN_ORDER_ID));
                order.setCustomerName(resultSet.getString(COLUMN_ORDER_CUSTOMER_NAME));
                order.setOrderedFoodItems(Arrays.asList(resultSet.getString(COLUMN_ORDER_ORDERED_FOOD_ITEMS).split(",")));
                order.setDeliveryLocation(resultSet.getString(COLUMN_ORDER_DELIVERY_LOCATION));
                order.setDeliveryDateTime(resultSet.getTimestamp(COLUMN_ORDER_DELIVERY_DATE_TIME).toInstant());
                order.setTotalCost(resultSet.getDouble(COLUMN_ORDER_TOTAL_COST));
                order.setOrderStatus(OrderStatus.valueOf(resultSet.getString(COLUMN_ORDER_STATUS)));
                order.setCreated(resultSet.getTimestamp(COLUMN_ORDER_CREATED).toInstant());
                resultSet.close();
                return order;
            }
            resultSet.close();
            return null;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Order> queryOrderByStatus(String orderStatus){
        try(PreparedStatement queryOrder = connection.prepareStatement(QUERY_ORDER_BY_ORDER_STATUS)){
            queryOrder.setString(1,orderStatus);
            ResultSet resultSet = queryOrder.executeQuery();
            List<Order> orders = new ArrayList<>();
            while(resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getInt(COLUMN_ORDER_ID));
                order.setCustomerName(resultSet.getString(COLUMN_ORDER_CUSTOMER_NAME));
                order.setOrderedFoodItems(Arrays.asList(resultSet.getString(COLUMN_ORDER_ORDERED_FOOD_ITEMS).split(",")));
                order.setDeliveryLocation(resultSet.getString(COLUMN_ORDER_DELIVERY_LOCATION));
                order.setDeliveryDateTime(resultSet.getTimestamp(COLUMN_ORDER_DELIVERY_DATE_TIME).toInstant());
                order.setTotalCost(resultSet.getDouble(COLUMN_ORDER_TOTAL_COST));
                order.setOrderStatus(OrderStatus.valueOf(resultSet.getString(COLUMN_ORDER_STATUS)));
                order.setCreated(resultSet.getTimestamp(COLUMN_ORDER_CREATED).toInstant());
                orders.add(order);
            }
            resultSet.close();
            return orders;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String updateOrderStatus(int orderId, String orderStatus){
        try(PreparedStatement updateOrderStatus = connection.prepareStatement(UPDATE_ORDER_STATUS,
                Statement.RETURN_GENERATED_KEYS)) {
            updateOrderStatus.setString(1, orderStatus);
            updateOrderStatus.setInt(2, orderId);
            int rowsAffected = updateOrderStatus.executeUpdate();
            if(rowsAffected > 0){
                ResultSet resultSet = updateOrderStatus.getGeneratedKeys();
                if (resultSet.next()){
                    return resultSet.getString(COLUMN_ORDER_STATUS);
                }
                resultSet.close();
            }
            return null;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
