package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.service.api.OrderManager;

import java.util.List;
import java.util.Objects;

public class OrderManagerImplementation implements OrderManager {
    private final OrderDataStore orderDataStore = new OrderDataStore();
    @Override
    public List<Order> getActiveOrders() {
        orderDataStore.openConnection();
        List<Order> orders = orderDataStore.queryOrderByStatus(OrderStatus.ORDER_PREPARED.name());
        orderDataStore.closeConnection();
        return orders.isEmpty() ? null : orders;
    }

    @Override
    public List<Order> getReceivedOrders() {
        orderDataStore.openConnection();
        List<Order> orders =  orderDataStore.queryOrderByStatus(OrderStatus.ORDER_RECEIVED.name());
        orderDataStore.closeConnection();
        return orders.isEmpty() ? null : orders;
    }

    @Override
    public List<Order> getCancelledOrders() {
        orderDataStore.openConnection();
        List<Order> orders =  orderDataStore.queryOrderByStatus(OrderStatus.ORDER_CANCELLED.name());
        orderDataStore.closeConnection();
        return orders.isEmpty() ? null : orders;
    }

    @Override
    public List<Order> getCompletedOrders() {
        orderDataStore.openConnection();
        List<Order> orders =  orderDataStore.queryOrderByStatus(OrderStatus.ORDER_DELIVERED.name());
        orderDataStore.closeConnection();
        return orders.isEmpty() ? null : orders;
    }

    @Override
    public boolean prepareOrder(int orderId) {
        orderDataStore.openConnection();
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_PREPARED.name());
        orderDataStore.closeConnection();
        return OrderStatus.ORDER_PREPARED.name().equals(orderStatus);
    }
}