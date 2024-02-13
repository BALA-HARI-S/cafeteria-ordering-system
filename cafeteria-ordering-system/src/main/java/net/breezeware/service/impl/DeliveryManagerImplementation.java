package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.DeliveryManager;

public class DeliveryManagerImplementation implements DeliveryManager {
    private final OrderDataStore orderDataStore;

    public DeliveryManagerImplementation(OrderDataStore orderDataStore) {
        this.orderDataStore = orderDataStore;
    }

    @Override
    public boolean deliverOrder(int orderId) throws CustomException {
        String orderStatus = orderDataStore.updateOrderStatus(orderId, OrderStatus.ORDER_DELIVERED.name());
        return OrderStatus.ORDER_DELIVERED.name().equals(orderStatus);
    }
}
