package net.breezeware.service.api;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;

import java.time.Instant;
import java.util.List;

public interface PlaceOrderManager {
    Order createOrder(String customerId, List<String> orderedItems, String deliveryLocation, String deliveryDateTime,
                      double totalCost);
    Order getOrder(int orderId);
    void editFoodItemsInOrder(Order order, List<String> newFoodItems) throws CustomException;
    void editDeliveryLocation(Order order, String newDeliveryLocation);
    void editDeliveryDateAndTime(Order order, String newDeliveryDateAndTime);
    Order confirmOrder(Order order);
    boolean cancelOrder(int orderId);
}
