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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class OrderManagerTest {

    @Mock
    private OrderDataStore orderDataStore;

    @InjectMocks
    private OrderManagerImplementation orderManager;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    // Retrieve Active Orders - When No Active Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveActiveOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveOrders(OrderStatus.ORDER_PREPARED))
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

        List<Order> activeOrders = orderManager.retrieveOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_PREPARED);
    }

    // Retrieve Received Orders - When No Received Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveReceivedOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveOrders(OrderStatus.ORDER_RECEIVED))
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

        List<Order> activeOrders = orderManager.retrieveOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_RECEIVED);
    }

    // Retrieve Cancelled Orders - When No Cancelled Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveCancelledOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveOrders(OrderStatus.ORDER_CANCELLED))
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

        List<Order> activeOrders = orderManager.retrieveOrders(orderStatus);
        Assertions.assertThat(activeOrders.size()).isEqualTo(1);

        Assertions.assertThat(activeOrders.get(0).getOrderStatus()).isEqualTo(OrderStatus.ORDER_CANCELLED);
    }

    // Retrieve Completed Orders - When No Completed Orders Available
    @Test
    void givenOrderStatus_WhenRetrieveCompletedOrders_ThenThrowsException() throws CustomException{
        when(orderDataStore.queryOrderByStatus(anyString())).thenReturn(new ArrayList<Order>());
        Assertions.assertThatThrownBy(() -> orderManager.retrieveOrders(OrderStatus.ORDER_DELIVERED))
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

        List<Order> activeOrders = orderManager.retrieveOrders(orderStatus);
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

}