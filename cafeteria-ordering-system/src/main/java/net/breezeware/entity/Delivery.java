package net.breezeware.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

    private String formatDeliveryDateTimeInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(deliveryDateTime, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    @Override
    public String toString() {
        return  "%s   %s".formatted(deliveryLocation, formatDeliveryDateTimeInstant());
    }
}
