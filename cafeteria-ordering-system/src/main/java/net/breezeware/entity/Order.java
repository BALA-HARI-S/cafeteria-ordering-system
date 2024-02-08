package net.breezeware.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private double totalCost;
    private OrderStatus orderStatus;
    private Instant created;

    public Order(){}
    public Order(double totalCost, OrderStatus orderStatus, Instant created) {
        this.totalCost = totalCost;
        this.orderStatus = orderStatus;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    private String formatCreatedInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(created, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    @Override
    public String toString(){
        return "%d  |   %.2f    |   %s  |   %s"
                .formatted(id , totalCost, orderStatus, formatCreatedInstant());
    }
}
