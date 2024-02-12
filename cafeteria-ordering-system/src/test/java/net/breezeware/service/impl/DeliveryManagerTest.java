package net.breezeware.service.impl;

import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.OrderStatus;
import net.breezeware.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class DeliveryManagerTest {

    @Mock
    private OrderDataStore orderDataStore;

    @InjectMocks
    private DeliveryManagerImplementation deliveryManager;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    // Deliver Order - Update Order Status when the order is not Available
    @Test
    void givenOrderId_WhenDeliverOrder_ThenThrowsException() throws CustomException {
        when(orderDataStore.updateOrderStatus(anyInt(),any())).thenThrow(new CustomException("Order not Found!"));
        Assertions.assertThatThrownBy(() -> deliveryManager.deliverOrder(1))
                .isInstanceOf(CustomException.class)
                .hasMessage("Order not Found!");

    }

    // Deliver Order - Update Order Status
    @Test
    void givenOrderId_WhenDeliverOrder_ThenReturnTrue() throws CustomException {
        when(orderDataStore.updateOrderStatus(anyInt(),any())).thenReturn(OrderStatus.ORDER_DELIVERED.name());
        boolean isOrderDelivered = deliveryManager.deliverOrder(1);
        Assertions.assertThat(isOrderDelivered).isTrue();
    }
}