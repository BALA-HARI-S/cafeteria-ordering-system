package net.breezeware.service.api;

import net.breezeware.entity.Delivery;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;

import java.util.HashMap;
import java.util.List;

public interface OrderManager {
    /**
     * Creates an order with the provided details.
     *
     * @param foodItemsQuantityMap A {@code HashMap} containing food item names as keys and their quantities as values.
     * @param totalCost The total cost of the order.
     * @param deliveryLocation The delivery location for the order.
     * @param deliveryDateTime The delivery date and time for the order.
     * @return An {@code Order} object representing the created order.
     * @throws CustomException if any interruptions occur during the creation process.
     */
    Order createOrder(HashMap<String, Integer> foodItemsQuantityMap, double totalCost,
                      String deliveryLocation, String deliveryDateTime) throws CustomException;

    /**
     * Retrieves the order with the specified orderId.
     *
     * @param orderId The ID of the order to retrieve.
     * @return An {@code Order} object representing the retrieved order.
     * @throws CustomException if any interruptions occur during the retrieval process.
     */
    Order retrieveOrder(int orderId) throws CustomException;

    /**
     * Retrieves all orders with the specified status.
     *
     * @param orderStatus The status of the orders to retrieve.
     * @return A list of {@code Order} objects representing the retrieved orders.
     * @throws CustomException if any interruptions occur during the retrieval process.
     */
    List<Order> retrieveAllOrders(OrderStatus orderStatus) throws CustomException;

    /**
     * Retrieves the food items and their quantities for the specified orderId.
     *
     * @param orderId The ID of the order to retrieve the food items for.
     * @return A {@code HashMap} containing food item names as keys and their quantities as values.
     * @throws CustomException if any interruptions occur during the retrieval process.
     */
    HashMap<String, Integer> retrieveOrderedFoodItems(int orderId) throws CustomException;

    /**
     * Retrieves the delivery details for the specified orderId.
     *
     * @param orderId The ID of the order to retrieve the delivery details for.
     * @return A {@code Delivery} object representing the delivery details.
     * @throws CustomException if any interruptions occur during the retrieval process.
     */
    Delivery retrieveDeliveryDetails(int orderId) throws CustomException;

    /**
     * Updates the quantity of a specific food item in the order's cart.
     *
     * @param orderId The ID of the order to update.
     * @param foodItemName The name of the food item to update.
     * @param foodItemQuantity The new quantity of the food item.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the update process.
     */
    boolean updateFoodItemsInCartOrder(int orderId, String foodItemName, int foodItemQuantity) throws CustomException;

    /**
     * Updates the delivery location for the specified orderId.
     *
     * @param orderId The ID of the order to update the delivery location for.
     * @param newDeliveryLocation The new delivery location.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the update process.
     */
    boolean updateDeliveryLocation(int orderId, String newDeliveryLocation) throws CustomException;

    /**
     * Updates the delivery date and time for the specified orderId.
     *
     * @param orderId The ID of the order to update the delivery date and time for.
     * @param newDeliveryDateTime The new delivery date and time.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the update process.
     */
    boolean updateDeliveryDateAndTime(int orderId, String newDeliveryDateTime) throws CustomException;

    /**
     * Updates the total cost of the order in the cart.
     *
     * @param orderId The ID of the order to update the total cost for.
     * @param totalCost The new total cost.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the update process.
     */
    boolean updateCartOrderTotalCost(int orderId, double totalCost) throws CustomException;

    /**
     * Updates the quantity of a specific food item in the order's cart.
     *
     * @param orderId The ID of the order to update.
     * @param foodItemName The name of the food item to update.
     * @param quantity The new quantity of the food item.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the update process.
     */
    boolean updateCartOrderFoodItemQuantity(int orderId, String foodItemName, int quantity) throws CustomException;

    /**
     * Confirms the order with the specified orderId.
     *
     * @param orderId The ID of the order to confirm.
     * @return {@code true} if the order was successfully confirmed; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the confirmation process.
     */
    boolean confirmOrder(int orderId) throws CustomException;

    /**
     * Cancels the order with the specified orderId.
     *
     * @param orderId The ID of the order to cancel.
     * @return {@code true} if the order was successfully canceled; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the cancellation process.
     */
    boolean cancelOrder(int orderId) throws CustomException;

    /**
     * Prepares the order with the specified orderId.
     *
     * @param orderId The ID of the order to prepare.
     * @return {@code true} if the order was successfully prepared; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the preparation process.
     */
    boolean prepareOrder(int orderId) throws CustomException;

    /**
     * Deletes a specific food item from the order's cart.
     *
     * @param orderId The ID of the order to delete the food item from.
     * @param foodItemName The name of the food item to delete.
     * @return {@code true} if the food item was successfully deleted; {@code false} otherwise.
     * @throws CustomException if any interruptions occur during the deletion process.
     */
    boolean deleteCartOrderFoodItem(int orderId, String foodItemName) throws CustomException;

}
