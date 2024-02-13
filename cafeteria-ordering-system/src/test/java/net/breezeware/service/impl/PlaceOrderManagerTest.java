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
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PlaceOrderManagerTest {

    @Mock
    private OrderDataStore orderDataStore;

    @InjectMocks
    private PlaceOrderManagerImplementation placeOrderManager;

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

    @Test
    void givenValidOrderDetails_WhenCreateOrder_ThenReturnOrder() throws CustomException{
        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put("Dosa",2);
        when(orderDataStore.insertIntoOrders(anyDouble(),anyString(),any())).thenReturn(order);
        when(orderDataStore.insertIntoOrderFoodItemsMap(anyInt(), anyString(), anyInt())).thenReturn(order);
        doNothing().when(orderDataStore).insertDeliveryDetails(anyInt(), anyString(), any());

        Order createdOrder = placeOrderManager.createOrder(foodItemsQuantityMap,totalCost,deliveryLocation,deliveryDateTime);
        Assertions.assertThat(createdOrder.getId()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getTotalCost()).isEqualTo(totalCost);
    }

    @Test
    void givenValidOrderId_WhenRetrieveOrder_ThenReturnOrder() throws CustomException{
        when(orderDataStore.queryOrder(anyInt())).thenReturn(order);
        Order retrievedOrder = placeOrderManager.retrieveOrder(1);
        Assertions.assertThat(retrievedOrder.getId()).isEqualTo(1);
    }

    @Test
    void givenValidOrderId_WhenRetrieveOrderedFoodItems_ThenReturnFoodItemsQuantityMap() throws CustomException{
        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put("Dosa",2);
        when(orderDataStore.queryOrderedFoodItems(anyInt())).thenReturn(foodItemsQuantityMap);
        HashMap<String, Integer> orderedFoodItemAndQuantity = placeOrderManager.retrieveOrderedFoodItems(1);
        Assertions.assertThat(orderedFoodItemAndQuantity.size()).isEqualTo(1);
        Assertions.assertThat(orderedFoodItemAndQuantity.get("Dosa")).isEqualTo(2);
    }

    @Test
    void givenValidOrderId_WhenRetrieveDeliveryDetails_TheReturnDelivery() throws CustomException{
        Delivery delivery = new Delivery(1,deliveryLocation,fixedInstant);
        when(orderDataStore.queryDeliveryDetails(anyInt())).thenReturn(delivery);
        Delivery retrievedDelivery = placeOrderManager.retrieveDeliveryDetails(1);
        Assertions.assertThat(retrievedDelivery.getDeliveryLocation()).isEqualTo(deliveryLocation);
    }

    @Test
    void givenValidOrderIdFoodItemsAndQuantity_WhenUpdateFoodItemsInOrder_ThenReturnTrue() throws CustomException{
        when(orderDataStore.insertIntoOrderFoodItemsMap(anyInt(),anyString(),anyInt())).thenReturn(order);
        boolean isCartOrderFoodItemsUpdated = placeOrderManager.updateFoodItemsInCartOrder(1,"Food Item",1);
        Assertions.assertThat(isCartOrderFoodItemsUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdDeliveryLocation_WhenUpdateDeliveryLocation_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateDeliveryDateTime(anyInt(),any())).thenReturn(order);
        boolean isDeliveryDateTimeUpdated = placeOrderManager.updateDeliveryDateAndTime(1,deliveryDateTime);
        Assertions.assertThat(isDeliveryDateTimeUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdDeliveryDateTime_WhenUpdateDeliveryDateTime_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateDeliveryLocation(anyInt(),anyString())).thenReturn(order);
        boolean isDeliveryDateTimeUpdated = placeOrderManager.updateDeliveryLocation(1,deliveryLocation);
        Assertions.assertThat(isDeliveryDateTimeUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdTotalCost_WhenUpdateCartOrderTotalCost_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateCartOrderTotalCost(anyInt(),anyDouble())).thenReturn(order);
        boolean isDeliveryDateTimeUpdated = placeOrderManager.updateCartOrderTotalCost(1,100.00);
        Assertions.assertThat(isDeliveryDateTimeUpdated).isTrue();
    }

    @Test
    void givenValidOrderIdFoodItemNameQuantity_WhenUpdateCartOrderFoodItemQuantity_ThenReturnTrue() throws CustomException{
        when(orderDataStore.updateCartOrderFoodItemQuantity(anyInt(),anyString(),anyInt())).thenReturn(order);
        boolean isFoodItemQuantityUpdated = placeOrderManager.updateCartOrderFoodItemQuantity(1,"Food Item",1);
        Assertions.assertThat(isFoodItemQuantityUpdated).isTrue();
    }

    @Test
    void givenValidOrderId_WhenConfirmOrder_ThenReturnTrue() throws CustomException{
        OrderStatus confirmOrderStatus = OrderStatus.ORDER_RECEIVED;
        when(orderDataStore.updateOrderStatus(anyInt(),anyString())).thenReturn(confirmOrderStatus.name());
        boolean isOrderConfirmed = placeOrderManager.confirmOrder(1);
        Assertions.assertThat(isOrderConfirmed).isTrue();
    }

    @Test
    void givenValidOrderId_WhenCancelOrder_ThenReturnTrue() throws CustomException{
        OrderStatus cancelOrderStatus = OrderStatus.ORDER_CANCELLED;
        when(orderDataStore.updateOrderStatus(anyInt(),anyString())).thenReturn(cancelOrderStatus.name());
        boolean isOrderConfirmed = placeOrderManager.cancelOrder(1);
        Assertions.assertThat(isOrderConfirmed).isTrue();
    }

    @Test
    void givenValidOrderIdFoodItemName_WhenDeleteCartOrderFoodItem_ThenReturnTrue() throws CustomException{
        when(orderDataStore.deleteCartOrderFoodItem(anyInt(),anyString())).thenReturn(true);
        boolean isCartOrderDeleted = placeOrderManager.deleteCartOrderFoodItem(1,"Food Item");
        Assertions.assertThat(isCartOrderDeleted).isTrue();
    }
}