package net.breezeware.service.api;

import net.breezeware.entity.Order;

import java.util.List;

public interface OrderManager {
    List<Order> getActiveOrders();
    List<Order> getReceivedOrders();
    List<Order> getCancelledOrders();
    List<Order> getCompletedOrders();
    boolean prepareOrder(int orderId);

}
