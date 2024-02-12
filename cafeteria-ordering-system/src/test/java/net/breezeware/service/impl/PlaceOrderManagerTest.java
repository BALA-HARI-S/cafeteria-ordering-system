package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
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

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenOrderDetails_WhenCreateOrder_ThenReturnOrder() throws CustomException{
        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put("Dosa",2);
        double totalCost = 100.00;
        OrderStatus orderStatus = OrderStatus.ORDER_CART;
        Instant fixedInstant = Instant.parse("2024-02-12T10:15:30.00Z");
        String deliveryLocation = "Some Location";
        String deliveryDateTime = "12-02-2024 06-30-30 am";

        Order order = new Order(1,totalCost,orderStatus,fixedInstant);

        when(orderDataStore.insertIntoOrders(anyDouble(),anyString(),any())).thenReturn(order);
        doNothing().when(orderDataStore).insertIntoOrderedFoodItems(anyInt(), anyString(), anyInt());
        doNothing().when(orderDataStore).insertDeliveryDetails(anyInt(), anyString(), any());

        Order createdOrder = placeOrderManager.createOrder(foodItemsQuantityMap,totalCost,deliveryLocation,deliveryDateTime);
        Assertions.assertThat(createdOrder.getId()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getTotalCost()).isEqualTo(totalCost);
    }

    @Test
    void givenOrderId_WhenRetrieveOrder_ThenReturnOrder() throws CustomException{

    }

    @Test
    void givenOrderId_WhenRetrieveOrderedFoodItems_ThenReturnFoodItemsQuantityMap() throws CustomException{

    }

    @Test
    void givenOrderId_WhenRetrieveDeliveryDetails_TheReturnDelivery() throws CustomException{

    }

    @Test
    void givenOrderIdFoodItemsAndQuantity_WhenUpdateFoodItemsInOrder_ThenReturnTrue() throws CustomException{

    }

    @Test
    void givenOrderIdDeliveryDateTime_WhenUpdateDeliveryDateTime_ThenReturnTrue() throws CustomException{

    }

    @Test
    void givenOrderIdTotalCost_WhenUpdateCartOrderTotalCost_ThenReturnTrue() throws CustomException{

    }

    @Test
    void givenOrderIdDeliveryLocation_WhenUpdateCartOrderTotalCost_ThenReturnTrue() throws CustomException{

    }

    @Test
    void updateCartOrderFoodItemQuantity() throws CustomException{

    }

    @Test
    void confirmOrder() throws CustomException{

    }

    @Test
    void cancelOrder() throws CustomException{

    }

    @Test
    void deleteCartOrderFoodItem() throws CustomException{

    }
}