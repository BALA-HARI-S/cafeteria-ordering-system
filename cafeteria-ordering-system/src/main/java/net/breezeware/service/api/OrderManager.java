package net.breezeware.service.api;

import net.breezeware.entity.Order;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface OrderManager {
    List<Order> retrieveActiveOrders() throws CustomException;
    List<Order> retrieveReceivedOrders() throws CustomException;
    List<Order> retrieveCancelledOrders() throws CustomException;
    List<Order> retrieveCompletedOrders() throws CustomException;
    boolean prepareOrder(int orderId) throws CustomException;

}
