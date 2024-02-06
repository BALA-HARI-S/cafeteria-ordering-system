package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.*;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.PlaceOrderManager;

import java.time.*;
import java.util.*;


public class PlaceOrderManagerImplementation implements PlaceOrderManager {
    private final OrderDataStore orderDataStore = new OrderDataStore();

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
    public List<Order> retrieveCartOrders() throws CustomException {
        orderDataStore.openConnection();
        List<Order> orders = orderDataStore.queryOrderByStatus(OrderStatus.ORDER_CART.name());
        orderDataStore.closeConnection();
        return orders;
    }

    @Override
    public List<Order> retrieveConfirmedOrders() throws CustomException {
        orderDataStore.openConnection();
        List<Order> orders = orderDataStore.queryOrderByStatus(OrderStatus.ORDER_RECEIVED.name());
        orderDataStore.closeConnection();
        return orders;
    }

    @Override
    public List<Order> retrieveCancelledOrders() throws CustomException {
        orderDataStore.openConnection();
        List<Order> orders = orderDataStore.queryOrderByStatus(OrderStatus.ORDER_CANCELLED.name());
        orderDataStore.closeConnection();
        return orders;
    }

    @Override
    public HashMap<String, Integer> retrieveOrderedFoodItems(int orderId) throws CustomException {
        orderDataStore.openConnection();
        HashMap<String, Integer> foodItemsQuantityMap = orderDataStore.queryOrderedFoodItems(orderId);
        orderDataStore.closeConnection();
        return foodItemsQuantityMap;
    }

    @Override
    public void editFoodItemsInOrder(int orderId, String foodItemName, int foodItemQuantity) throws CustomException {
        orderDataStore.openConnection();
        orderDataStore.insertIntoOrderedFoodItems(orderId, foodItemName, foodItemQuantity);
        orderDataStore.closeConnection();
    }
//
//    @Override
//    public void editDeliveryLocation(Order order, String newDeliveryLocation) {
//        order.setDeliveryLocation(newDeliveryLocation);
//    }
//
//    @Override
//    public void editDeliveryDateAndTime(Order order, String newDeliveryDateAndTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
//        LocalDateTime localDateTime = LocalDateTime.parse(newDeliveryDateAndTime, formatter);
//        Instant deliveryDateAndTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
//        order.setDeliveryDateTime(deliveryDateAndTime);
//    }

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


    private static String getCustomStringRepresentation(List<String> list) {
        StringBuilder result = new StringBuilder(String.valueOf(list.get(0)));
        for (int i = 1; i < list.size(); i++) {
            result.append(",").append(list.get(i));
        }
        return result.toString();
    }
}
