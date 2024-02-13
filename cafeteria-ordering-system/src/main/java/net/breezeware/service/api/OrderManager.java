package net.breezeware.service.api;

import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;

import java.util.List;

public interface OrderManager {
    List<Order> retrieveOrders(OrderStatus orderStatus) throws CustomException;
    boolean prepareOrder(int orderId) throws CustomException;

}
