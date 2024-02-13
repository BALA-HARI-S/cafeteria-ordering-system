package net.breezeware.entity;

import net.breezeware.utility.CosUtil;

import java.time.Instant;

public class Order {
    private int id;
    private int customerId;
    private double totalCost;
    private OrderStatus orderStatus;
    private Instant created;

    public Order(){}

    public Order(int orderId, double totalCost, OrderStatus orderStatus, Instant created) {
        this.id = orderId;
        this.totalCost = totalCost;
        this.orderStatus = orderStatus;
        this.created = created;
    }
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

    @Override
    public String toString(){
        return "%d  |   %.2f    |   %s  |   %s"
                .formatted(id , totalCost, orderStatus,
                        CosUtil.formatInstantToString(created,"dd-MM-yyyy hh:mm:ss a"));
    }
}
