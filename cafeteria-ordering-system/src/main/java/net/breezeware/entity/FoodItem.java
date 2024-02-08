package net.breezeware.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FoodItem {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private Instant created;
    private Instant modified;

    public FoodItem(){}
    public FoodItem(int id, String name, int quantity){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
    public FoodItem(String name, int quantity, double price, Instant created, Instant modified){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.created = created;
        this.modified = modified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    private String formatCreatedInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(created,ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    private String formatModifiedInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(modified,ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    @Override
    public String toString() {
        return "%d | %s | %d | %.2f | %s | %s"
                .formatted(id, name, quantity, price, formatCreatedInstant(), formatModifiedInstant());
    }
}
