package net.breezeware.service.api;

import net.breezeware.entity.Delivery;
import net.breezeware.entity.Order;
import net.breezeware.exception.CustomException;

import java.util.HashMap;

public interface PlaceOrderManager {
    Order createOrder(HashMap<String, Integer> foodItemsQuantityMap, double totalCost,
                      String deliveryLocation, String deliveryDateTime) throws CustomException;
    Order retrieveOrder(int orderId) throws CustomException;
    HashMap<String, Integer> retrieveOrderedFoodItems(int orderId) throws CustomException;
    Delivery retrieveDeliveryDetails(int orderId) throws CustomException;
    boolean updateFoodItemsInOrder(int orderId, String foodItemName, int foodItemQuantity) throws CustomException;
    boolean updateDeliveryLocation(int orderId, String newDeliveryLocation) throws CustomException;
    boolean updateDeliveryDateAndTime(int orderId, String newDeliveryDateTime) throws CustomException;
    boolean updateCartOrderTotalCost(int orderId, double totalCost) throws CustomException;
    boolean updateCartOrderFoodItemQuantity(int orderId,String foodItemName, int quantity) throws CustomException;
    boolean confirmOrder(int orderId) throws CustomException;
    boolean cancelOrder(int orderId) throws CustomException;
    boolean deleteCartOrderFoodItem(int orderId, String foodItemName) throws CustomException;
}
