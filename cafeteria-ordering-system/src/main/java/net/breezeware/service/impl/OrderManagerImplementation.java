package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.OrderManager;

import java.util.List;

public class OrderManagerImplementation implements OrderManager {
    private final OrderDataStore orderDataStore;
    public OrderManagerImplementation(OrderDataStore orderDataStore) {
        this.orderDataStore = orderDataStore;
    }


    @Override
    public List<Order> retrieveOrders(OrderStatus orderStatus) throws CustomException {
        orderDataStore.openConnection();
        List<Order> orders = orderDataStore.queryOrderByStatus(orderStatus.name());
        orderDataStore.closeConnection();
        if(orders.isEmpty()){
           throw new CustomException("Orders List is Empty");
        } else {
            return orders;
        }
    }

    @Override
    public boolean prepareOrder(int orderId) throws CustomException {
        orderDataStore.openConnection();
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_PREPARED.name());
        orderDataStore.closeConnection();
        return OrderStatus.ORDER_PREPARED.name().equals(orderStatus);
    }
}
