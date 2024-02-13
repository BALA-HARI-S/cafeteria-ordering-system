package net.breezeware.entity;

import net.breezeware.utility.CosUtil;

import java.time.Instant;

public class Delivery {
    private int order_id;
    private String deliveryLocation;
    private Instant deliveryDateTime;

    public Delivery(){}

    public Delivery(int order_id, String deliveryLocation, Instant deliveryDateTime) {
        this.order_id = order_id;
        this.deliveryLocation = deliveryLocation;
        this.deliveryDateTime = deliveryDateTime;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public Instant getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(Instant deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    @Override
    public String toString() {
        return  "%s   %s".formatted(deliveryLocation,
                CosUtil.formatInstantToString(deliveryDateTime,"dd-MM-yyyy hh:mm:ss a"));
    }
}
