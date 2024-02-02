package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.service.api.DeliveryManager;

import java.util.Objects;

public class DeliveryManagerImplementation implements DeliveryManager {
    private final OrderDataStore orderDataStore = new OrderDataStore();
    @Override
    public boolean deliverOrder(int orderId) {
        orderDataStore.openConnection();
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_DELIVERED.name());
        orderDataStore.closeConnection();
        return OrderStatus.ORDER_PREPARED.name().equals(orderStatus);
    }
}
