package net.breezeware.service.api;

import net.breezeware.exception.CustomException;

public interface ManageDelivery {
    /**
     * Change the order status to OrderStatus.ORDER_DELIVERED.
     * @param orderId - The ID of the prepared order (with Status OrderStatus.ORDER_PREPARED) to be changed.
     * @return {@code true} if the order's status changed OrderStatus.ORDER_PREPARED to OrderStatus.ORDER_DELIVERED
     *         {@code false} otherwise.
     * @throws CustomException Throws Exception if any case the given order doesn't exist or if any interruptions occurs during the process.
     */
    boolean deliverOrder(int orderId) throws CustomException;
}
