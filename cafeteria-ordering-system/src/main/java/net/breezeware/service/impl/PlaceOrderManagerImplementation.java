package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.*;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.PlaceOrderManager;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class PlaceOrderManagerImplementation implements PlaceOrderManager {

    private final OrderDataStore orderDataStore;

    public PlaceOrderManagerImplementation(OrderDataStore orderDataStore) {
        this.orderDataStore = orderDataStore;
    }

    @Override
    public Order createOrder(HashMap<String, Integer> foodItemsQuantityMap, double totalCost,
                             String deliveryLocation, String deliveryDateTime) throws CustomException {
        Order order = new Order(totalCost, OrderStatus.ORDER_CART, Instant.now());
        LocalDateTime createdLocalDateTime = LocalDateTime.ofInstant(order.getCreated(), ZoneId.systemDefault());
        orderDataStore.openConnection();
        Order cartOrder = orderDataStore.insertIntoOrders(order.getTotalCost(), order.getOrderStatus().name(), createdLocalDateTime);
        for(String foodItemName: foodItemsQuantityMap.keySet()){
            orderDataStore.insertIntoOrderedFoodItems(cartOrder.getId(), foodItemName, foodItemsQuantityMap.get(foodItemName));
        }
        LocalDateTime localDateTime = LocalDateTime.parse(deliveryDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a"));
        orderDataStore.insertDeliveryDetails(cartOrder.getId(), deliveryLocation, localDateTime);
        orderDataStore.closeConnection();
        return cartOrder;
    }

    @Override
    public Order retrieveOrder(int orderId) throws CustomException {
        orderDataStore.openConnection();
        Order order = orderDataStore.queryOrder(orderId);
        orderDataStore.openConnection();
        return order;
    }

    @Override
    public HashMap<String, Integer> retrieveOrderedFoodItems(int orderId) throws CustomException {
        orderDataStore.openConnection();
        HashMap<String, Integer> foodItemsQuantityMap = orderDataStore.queryOrderedFoodItems(orderId);
        orderDataStore.closeConnection();
        return foodItemsQuantityMap;
    }

    @Override
    public Delivery retrieveDeliveryDetails(int orderId) throws CustomException {
        orderDataStore.openConnection();
        Delivery delivery = orderDataStore.queryDeliveryDetails(orderId);
        orderDataStore.closeConnection();
        return delivery;
    }

    @Override
    public boolean updateFoodItemsInOrder(int orderId, String foodItemName, int foodItemQuantity) throws CustomException {
        orderDataStore.openConnection();
        orderDataStore.insertIntoOrderedFoodItems(orderId, foodItemName, foodItemQuantity);
        orderDataStore.closeConnection();
        return true;
    }

    @Override
    public boolean updateDeliveryLocation(int orderId, String newDeliveryLocation) throws CustomException {
        orderDataStore.openConnection();
        orderDataStore.updateDeliveryLocation(orderId, newDeliveryLocation);
        orderDataStore.closeConnection();
        return true;
    }

    @Override
    public boolean updateDeliveryDateAndTime(int orderId, String newDeliveryDateTime) throws CustomException {
        orderDataStore.openConnection();
        LocalDateTime localDateTime = LocalDateTime.parse(newDeliveryDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a"));
        orderDataStore.updateDeliveryDateTime(orderId, localDateTime);
        orderDataStore.closeConnection();
        return true;
    }

    @Override
    public boolean updateCartOrderTotalCost(int orderId, double totalCost) throws CustomException {
        orderDataStore.openConnection();
        orderDataStore.updateCartOrderTotalCost(orderId, totalCost);
        orderDataStore.closeConnection();
        return true;
    }

    @Override
    public boolean updateCartOrderFoodItemQuantity(int orderId, String foodItemName, int quantity) throws CustomException {
        orderDataStore.openConnection();
        orderDataStore.updateCartOrderFoodItemQuantity(orderId, foodItemName, quantity);
        orderDataStore.closeConnection();
        return true;
    }

    @Override
    public boolean confirmOrder(int orderId) throws CustomException {
        orderDataStore.openConnection();
        String confirmOrder = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_RECEIVED.name());
        orderDataStore.closeConnection();
        return OrderStatus.ORDER_RECEIVED.name().equals(confirmOrder);
    }

    @Override
    public boolean cancelOrder(int orderId) throws CustomException {
        orderDataStore.openConnection();
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_CANCELLED.name());
        orderDataStore.closeConnection();
        return OrderStatus.ORDER_CANCELLED.name().equals(orderStatus);
    }

    @Override
    public boolean deleteCartOrderFoodItem(int orderId, String foodItemName) throws CustomException {
        orderDataStore.openConnection();
        boolean deleteCartOrderFoodItem = orderDataStore.deleteCartOrderFoodItem(orderId, foodItemName);
        orderDataStore.closeConnection();
        return  deleteCartOrderFoodItem;
    }

}
