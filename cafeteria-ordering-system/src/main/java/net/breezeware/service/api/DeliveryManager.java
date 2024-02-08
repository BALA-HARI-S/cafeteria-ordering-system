package net.breezeware.service.api;

import net.breezeware.entity.Order;
import net.breezeware.exception.CustomException;

public interface DeliveryManager {
    boolean deliverOrder(int orderId) throws CustomException;
}
