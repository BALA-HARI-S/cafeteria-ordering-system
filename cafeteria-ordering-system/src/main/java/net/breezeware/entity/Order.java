package net.breezeware.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
    private String customerName;
    private List<String> orderedFoodItems;
    private String deliveryLocation;
    private Instant deliveryDateTime;
    private double totalCost;
    private OrderStatus orderStatus;
    private Instant created;

    public Order(){}
    public Order(String customerName, List<String> orderedItems, String deliveryLocation, Instant deliveryDateAndTime,
                 double totalCost) {
        this.customerName = customerName;
        this.orderedFoodItems = orderedItems;
        this.deliveryLocation = deliveryLocation;
        this.deliveryDateTime = deliveryDateAndTime;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getOrderedFoodItems() {
        return orderedFoodItems;
    }

    public void setOrderedFoodItems(List<String> orderedFoodItems) {
        this.orderedFoodItems = orderedFoodItems;
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

    private String formatDeliveryInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(deliveryDateTime,ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    @Override
    public String toString(){
        return "%d  |   %s  |   %s  |   %s  |   %.2f    |   %s  |   %s"
                .formatted(id, customerName, deliveryLocation,
                formatDeliveryInstant(), totalCost, orderStatus, formatCreatedInstant());
    }
}
