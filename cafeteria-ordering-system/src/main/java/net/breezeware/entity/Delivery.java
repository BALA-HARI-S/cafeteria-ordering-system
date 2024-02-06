package net.breezeware.entity;

import java.time.Instant;

public class Delivery {
    private int order_id;
    private String orderLocation;
    private Instant orderDateTime;

    public Delivery(){}

    public Delivery(int order_id, String orderLocation, Instant orderDateTime) {
        this.order_id = order_id;
        this.orderLocation = orderLocation;
        this.orderDateTime = orderDateTime;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrderLocation() {
        return orderLocation;
    }

    public void setOrderLocation(String orderLocation) {
        this.orderLocation = orderLocation;
    }

    public Instant getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Instant orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "order_id=" + order_id +
                ", orderLocation='" + orderLocation + '\'' +
                ", orderDateTime='" + orderDateTime + '\'' +
                '}';
    }
}
