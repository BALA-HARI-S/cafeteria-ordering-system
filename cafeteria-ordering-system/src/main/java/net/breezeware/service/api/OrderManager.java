package net.breezeware.service.api;

import net.breezeware.entity.Order;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface OrderManager {
    List<Order> getActiveOrders() throws CustomException;
    List<Order> getReceivedOrders() throws CustomException;
    List<Order> getCancelledOrders() throws CustomException;
    List<Order> getCompletedOrders() throws CustomException;
    boolean prepareOrder(int orderId) throws CustomException;

}
