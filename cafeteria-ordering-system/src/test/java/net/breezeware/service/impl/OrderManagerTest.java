package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.Delivery;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class OrderManagerTest {

    @Mock
    private OrderDataStore orderDataStore;

    @InjectMocks
    private OrderManagerImplementation orderManager;

    private final double totalCost = 100.00;
    private final OrderStatus orderStatusCartOrder = OrderStatus.ORDER_CART;
    private final Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
    private final Order order = new Order(1,totalCost, orderStatusCartOrder,fixedInstant);

    private final String deliveryLocation = "Some Location";
    private final String deliveryDateTime = "12-02-2024 06-30-30 am";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    // Retrieve Active Orders - When No Active Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveActiveOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveAllOrders(OrderStatus.ORDER_PREPARED))
                .isInstanceOf(CustomException.class)
                .hasMessage("Orders List is Empty");
    }

    // Retrieve Active Orders - OrderStatus ORDER_PREPARED
    @Test
    void givenOrderStatus_WhenRetrieveActiveOrders_ThenReturnActiveOrdersList() throws CustomException{
        double totalCost = 100.00;
        OrderStatus orderStatus = OrderStatus.ORDER_PREPARED;
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        List<Order> Orders = new ArrayList<>();
        Orders.add(new Order(totalCost,orderStatus,fixedInstant));

        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(Orders);

        List<Order> activeOrders = orderManager.retrieveAllOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_PREPARED);
    }

    // Retrieve Received Orders - When No Received Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveReceivedOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveAllOrders(OrderStatus.ORDER_RECEIVED))
                .isInstanceOf(CustomException.class)
                .hasMessage("Orders List is Empty");
    }

    // Retrieve Received Orders - OrderStatus ORDER_RECEIVED
    @Test
    void givenOrderStatus_WhenRetrieveReceivedOrders_ThenReturnReceivedOrdersList() throws CustomException{
        double totalCost = 100.00;
        OrderStatus orderStatus = OrderStatus.ORDER_RECEIVED;
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        List<Order> Orders = new ArrayList<>();
        Orders.add(new Order(totalCost,orderStatus,fixedInstant));

        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(Orders);

        List<Order> activeOrders = orderManager.retrieveAllOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_RECEIVED);
    }

    // Retrieve Cancelled Orders - When No Cancelled Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveCancelledOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveAllOrders(OrderStatus.ORDER_CANCELLED))
                .isInstanceOf(CustomException.class)
                .hasMessage("Orders List is Empty");
    }

    // Retrieve Cancelled Orders - OrderStatus ORDER_CANCELLED
    @Test
    void givenOrderStatus_WhenRetrieveCancelledOrders_ThenReturnCancelledOrdersList() throws CustomException{
        double totalCost = 100.00;
        OrderStatus orderStatus = OrderStatus.ORDER_CANCELLED;
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        List<Order> Orders = new ArrayList<>();
        Orders.add(new Order(totalCost,orderStatus,fixedInstant));

        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(Orders);

        List<Order> activeOrders = orderManager.retrieveAllOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_CANCELLED);
    }

    // Retrieve Completed Orders - When No Completed Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveCompletedOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveAllOrders(OrderStatus.ORDER_DELIVERED))
                .isInstanceOf(CustomException.class)
                .hasMessage("Orders List is Empty");
    }

    // Retrieve Completed Orders - OrderStatus ORDER_DELIVERED
    @Test
    void giveOrderStatus_WhenRetrieveCompletedOrders_ThenReturnCompletedOrdersList() throws CustomException{
        double totalCost = 100.00;
        OrderStatus orderStatus = OrderStatus.ORDER_DELIVERED;
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        List<Order> Orders = new ArrayList<>();
        Orders.add(new Order(totalCost,orderStatus,fixedInstant));

        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(Orders);

        List<Order> activeOrders = orderManager.retrieveAllOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_DELIVERED);
    }

    // Prepare Order - Update Order Status when the order is not Available
    @Test
    void givenOrderId_WhenPrepareOrder_ThenThrowsException() throws CustomException {
        when(orderDataStore.updateOrderStatus(anyInt(),any())).thenThrow(new CustomException("Order not Found!"));
        Assertions.assertThatThrownBy(() -> orderManager.prepareOrder(1))
                .isInstanceOf(CustomException.class)
                .hasMessage("Order not Found!");

    }

    // Prepare Order - Update Order Status to OrderStatus.ORDER_PREPARED
    @Test
    void givenOrderId_WhenPrepareOrder_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateOrderStatus(anyInt(),any())).thenReturn(OrderStatus.ORDER_PREPARED.name());

        boolean isOrderPrepared = orderManager.prepareOrder(1);
        Assertions.assertThat(isOrderPrepared).isTrue();
    }

    @Test
    void givenValidOrderDetails_WhenCreateOrder_ThenReturnOrder() throws CustomException{
        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put("Dosa",2);
        when(orderDataStore.insertIntoOrders(anyDouble(),anyString(),any())).thenReturn(order);
        when(orderDataStore.insertIntoOrderFoodItemsMap(anyInt(), anyString(), anyInt())).thenReturn(order);
        doNothing().when(orderDataStore).insertDeliveryDetails(anyInt(), anyString(), any());

        Order createdOrder = orderManager.createOrder(foodItemsQuantityMap,totalCost,deliveryLocation,deliveryDateTime);
        Assertions.assertThat(createdOrder.getId()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getTotalCost()).isEqualTo(totalCost);
    }

    @Test
    void givenValidOrderId_WhenRetrieveOrder_ThenReturnOrder() throws CustomException{
        when(orderDataStore.queryOrder(anyInt())).thenReturn(order);
        Order retrievedOrder = orderManager.retrieveOrder(1);
        Assertions.assertThat(retrievedOrder.getId()).isEqualTo(1);
    }

    @Test
    void givenValidOrderId_WhenRetrieveOrderedFoodItems_ThenReturnFoodItemsQuantityMap() throws CustomException{
        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put("Dosa",2);
        when(orderDataStore.queryOrderedFoodItems(anyInt())).thenReturn(foodItemsQuantityMap);
        HashMap<String, Integer> orderedFoodItemAndQuantity = orderManager.retrieveOrderedFoodItems(1);
        Assertions.assertThat(orderedFoodItemAndQuantity.size()).isEqualTo(1);
        Assertions.assertThat(orderedFoodItemAndQuantity.get("Dosa")).isEqualTo(2);
    }

    @Test
    void givenValidOrderId_WhenRetrieveDeliveryDetails_TheReturnDelivery() throws CustomException{
        Delivery delivery = new Delivery(1,deliveryLocation,fixedInstant);
        when(orderDataStore.queryDeliveryDetails(anyInt())).thenReturn(delivery);
        Delivery retrievedDelivery = orderManager.retrieveDeliveryDetails(1);
        Assertions.assertThat(retrievedDelivery.getDeliveryLocation()).isEqualTo(deliveryLocation);
    }

    @Test
    void givenValidOrderIdFoodItemsAndQuantity_WhenUpdateFoodItemsInOrder_ThenReturnTrue() throws CustomException{
        when(orderDataStore.insertIntoOrderFoodItemsMap(anyInt(),anyString(),anyInt())).thenReturn(order);
        boolean isCartOrderFoodItemsUpdated = orderManager.updateFoodItemsInCartOrder(1,"Food Item",1);
        Assertions.assertThat(isCartOrderFoodItemsUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdDeliveryLocation_WhenUpdateDeliveryLocation_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateDeliveryDateTime(anyInt(),any())).thenReturn(order);
        boolean isDeliveryDateTimeUpdated = orderManager.updateDeliveryDateAndTime(1,deliveryDateTime);
        Assertions.assertThat(isDeliveryDateTimeUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdDeliveryDateTime_WhenUpdateDeliveryDateTime_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateDeliveryLocation(anyInt(),anyString())).thenReturn(order);
        boolean isDeliveryDateTimeUpdated = orderManager.updateDeliveryLocation(1,deliveryLocation);
        Assertions.assertThat(isDeliveryDateTimeUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdTotalCost_WhenUpdateCartOrderTotalCost_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateCartOrderTotalCost(anyInt(),anyDouble())).thenReturn(order);
        boolean isDeliveryDateTimeUpdated = orderManager.updateCartOrderTotalCost(1,100.00);
        Assertions.assertThat(isDeliveryDateTimeUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdFoodItemNameQuantity_WhenUpdateCartOrderFoodItemQuantity_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateCartOrderFoodItemQuantity(anyInt(),anyString(),anyInt())).thenReturn(order);
        boolean isFoodItemQuantityUpdated = orderManager.updateCartOrderFoodItemQuantity(1,"Food Item",1);
        Assertions.assertThat(isFoodItemQuantityUpdated).isTrue();
    }

    @Test
    void givenValidOrderId_WhenConfirmOrder_ThenReturnTrue() throws CustomException{
        OrderStatus confirmOrderStatus = OrderStatus.ORDER_RECEIVED;
        when(orderDataStore.updateOrderStatus(anyInt(),anyString())).thenReturn(confirmOrderStatus.name());
        boolean isOrderConfirmed = orderManager.confirmOrder(1);
        Assertions.assertThat(isOrderConfirmed).isTrue();
    }

    @Test
    void givenValidOrderId_WhenCancelOrder_ThenReturnTrue() throws CustomException{
        OrderStatus cancelOrderStatus = OrderStatus.ORDER_CANCELLED;
        when(orderDataStore.updateOrderStatus(anyInt(),anyString())).thenReturn(cancelOrderStatus.name());
        boolean isOrderConfirmed = orderManager.cancelOrder(1);
        Assertions.assertThat(isOrderConfirmed).isTrue();
    }

    @Test
    void givenValidOrderIdFoodItemName_WhenDeleteCartOrderFoodItem_ThenReturnTrue() throws CustomException{
        when(orderDataStore.deleteCartOrderFoodItem(anyInt(),anyString())).thenReturn(true);
        boolean isCartOrderDeleted = orderManager.deleteCartOrderFoodItem(1,"Food Item");
        Assertions.assertThat(isCartOrderDeleted).isTrue();
    }

}