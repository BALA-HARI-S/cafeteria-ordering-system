package net.breezeware.service.api;

import net.breezeware.entity.Order;

public interface DeliveryManager {
    boolean deliverOrder(int orderId);
}
