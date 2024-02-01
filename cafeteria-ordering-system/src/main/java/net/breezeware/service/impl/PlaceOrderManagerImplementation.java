package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.*;
import net.breezeware.service.api.FoodItemManager;
import net.breezeware.service.api.FoodMenuManager;
import net.breezeware.service.api.PlaceOrderManager;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;


public class PlaceOrderManagerImplementation implements PlaceOrderManager {

    private final FoodItemManager foodItemManager = new FoodItemManagerImplementation();
    private final FoodMenuManager foodMenuManager = new FoodMenuManagerImplementation();
    private final OrderDataStore orderDataStore = new OrderDataStore();
    @Override
    public List<FoodMenu> viewFoodMenu() {
        List<FoodMenu> foodMenuList = foodMenuManager.getAllFoodMenus();
        List<FoodMenu> foodMenuOfTheDay = new ArrayList<>();
        for(FoodMenu menu: foodMenuList){
            LocalDate currentDate = LocalDate.now();
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String today = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            for(AvailableDay day: menu.getAvailableDay()){
                if(day.name().equals(today.toUpperCase())){
                    foodMenuOfTheDay.add(menu);
                }
            }
        }
        return foodMenuOfTheDay;
    }

    @Override
    public Order createOrder(String customerId, List<String> orderedItems, String deliveryLocation,
                             String deliveryDateTime, double totalCost) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
        LocalDateTime localDateTime = LocalDateTime.parse(deliveryDateTime, formatter);
        Instant deliveryDateAndTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new Order(customerId, orderedItems, deliveryLocation, deliveryDateAndTime, totalCost);
    }

    @Override
    public Order getOrder(int orderId) {
        orderDataStore.openConnection();
        Order order = orderDataStore.queryOrder(orderId);
        orderDataStore.openConnection();
        return Objects.isNull(order)? null: order;
    }

    @Override
    public void editFoodItemsInOrder(Order order, List<String> newFoodItems) {
         order.setOrderedFoodItems(newFoodItems);
        double totalCost = 0.00;
        for(String foodItem: order.getOrderedFoodItems()){
            totalCost += foodItemManager.getFoodItem(foodItem).getPrice();
        }
        order.setTotalCost(totalCost);
    }

    @Override
    public void editDeliveryLocation(Order order, String newDeliveryLocation) {
        order.setDeliveryLocation(newDeliveryLocation);
    }

    @Override
    public void editDeliveryDateAndTime(Order order, String newDeliveryDateAndTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
        LocalDateTime localDateTime = LocalDateTime.parse(newDeliveryDateAndTime, formatter);
        Instant deliveryDateAndTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        order.setDeliveryDateTime(deliveryDateAndTime);
    }

    @Override
    public Order confirmOrder(Order order) {
        order.setCreated(Instant.now());
        order.setOrderStatus(OrderStatus.ORDER_RECEIVED);
        orderDataStore.openConnection();
         Order confirmedOrder = orderDataStore.insertOrder(order.getCustomerName(), getCustomStringRepresentation(order.getOrderedFoodItems()),
                order.getDeliveryLocation(), LocalDateTime.ofInstant(order.getDeliveryDateTime(), ZoneId.systemDefault()),
                order.getTotalCost(), order.getOrderStatus().name(),
                LocalDateTime.ofInstant(order.getCreated(), ZoneId.systemDefault()));
         orderDataStore.closeConnection();
         return confirmedOrder;
    }

    @Override
    public boolean cancelOrder(int orderId) {
        orderDataStore.openConnection();
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_CANCELLED.name());
        orderDataStore.closeConnection();
        return OrderStatus.ORDER_CANCELLED.name().equals(orderStatus);
    }


    private static String getCustomStringRepresentation(List<String> list) {
        StringBuilder result = new StringBuilder(String.valueOf(list.get(0)));
        for (int i = 1; i < list.size(); i++) {
            result.append(",").append(list.get(i));
        }
        return result.toString();
    }
}
