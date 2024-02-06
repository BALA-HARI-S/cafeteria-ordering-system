package net.breezeware.service.api;

import net.breezeware.entity.Order;
import net.breezeware.exception.CustomException;

import java.util.HashMap;
import java.util.List;

public interface PlaceOrderManager {
    Order createOrder(HashMap<String, Integer> foodItemsQuantityMap, double totalCost,
                      String deliveryLocation, String deliveryDateTime) throws CustomException;
    Order retrieveOrder(int orderId) throws CustomException;
    List<Order> retrieveCartOrders() throws CustomException;
    List<Order> retrieveConfirmedOrders() throws CustomException;
    List<Order> retrieveCancelledOrders() throws CustomException;
    HashMap<String, Integer> retrieveOrderedFoodItems(int orderId) throws CustomException;
    void editFoodItemsInOrder(int orderId, String foodItemName, int foodItemQuantity) throws CustomException;
//    void editDeliveryLocation(Order order, String newDeliveryLocation);
//    void editDeliveryDateAndTime(Order order, String newDeliveryDateAndTime);
    boolean confirmOrder(int orderId) throws CustomException;
    boolean cancelOrder(int orderId) throws CustomException;
    boolean deleteCartOrderFoodItem(int orderId, String foodItemName) throws CustomException;
}
