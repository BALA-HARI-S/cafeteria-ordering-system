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
        Order cartOrder = orderDataStore.insertIntoOrders(order.getTotalCost(), order.getOrderStatus().name(), createdLocalDateTime);
        for(String foodItemName: foodItemsQuantityMap.keySet()){
            orderDataStore.insertIntoOrderFoodItemsMap(cartOrder.getId(), foodItemName, foodItemsQuantityMap.get(foodItemName));
        }
        LocalDateTime localDateTime = LocalDateTime.parse(deliveryDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a"));
        orderDataStore.insertDeliveryDetails(cartOrder.getId(), deliveryLocation, localDateTime);
        return cartOrder;
    }

    @Override
    public Order retrieveOrder(int orderId) throws CustomException {
        return orderDataStore.queryOrder(orderId);
    }

    @Override
    public HashMap<String, Integer> retrieveOrderedFoodItems(int orderId) throws CustomException {
        return orderDataStore.queryOrderedFoodItems(orderId);
    }

    @Override
    public Delivery retrieveDeliveryDetails(int orderId) throws CustomException {
        return orderDataStore.queryDeliveryDetails(orderId);
    }

    @Override
    public boolean updateFoodItemsInCartOrder(int orderId, String foodItemName, int foodItemQuantity) throws CustomException {
        Order order = orderDataStore.insertIntoOrderFoodItemsMap(orderId, foodItemName, foodItemQuantity);
        return !Objects.isNull(order);
    }

    @Override
    public boolean updateDeliveryLocation(int orderId, String newDeliveryLocation) throws CustomException {
        Order order = orderDataStore.updateDeliveryLocation(orderId, newDeliveryLocation);
        return !Objects.isNull(order);
    }

    @Override
    public boolean updateDeliveryDateAndTime(int orderId, String newDeliveryDateTime) throws CustomException {
        LocalDateTime localDateTime = LocalDateTime.parse(newDeliveryDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a"));
        Order order = orderDataStore.updateDeliveryDateTime(orderId, localDateTime);
        return !Objects.isNull(order);
    }

    @Override
    public boolean updateCartOrderTotalCost(int orderId, double totalCost) throws CustomException {
        Order order = orderDataStore.updateCartOrderTotalCost(orderId, totalCost);
        return !Objects.isNull(order);
    }

    @Override
    public boolean updateCartOrderFoodItemQuantity(int orderId, String foodItemName, int quantity) throws CustomException {
        Order order = orderDataStore.updateCartOrderFoodItemQuantity(orderId, foodItemName, quantity);
        return !Objects.isNull(order);
    }

    @Override
    public boolean confirmOrder(int orderId) throws CustomException {
        String confirmOrder = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_RECEIVED.name());
        return OrderStatus.ORDER_RECEIVED.name().equals(confirmOrder);
    }

    @Override
    public boolean cancelOrder(int orderId) throws CustomException {
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_CANCELLED.name());
        return OrderStatus.ORDER_CANCELLED.name().equals(orderStatus);
    }

    @Override
    public boolean deleteCartOrderFoodItem(int orderId, String foodItemName) throws CustomException {
        return orderDataStore.deleteCartOrderFoodItem(orderId, foodItemName);
    }

}
