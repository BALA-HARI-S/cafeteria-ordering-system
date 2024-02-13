package net.breezeware.entity;

import net.breezeware.utility.CosUtil;

import java.time.Instant;

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

    @Override
    public String toString() {
        return "%d | %s | %d | %.2f | %s | %s"
                .formatted(id, name, quantity, price,
                        CosUtil.formatInstantToString(created,"dd-MM-yyyy hh:mm:ss a"),
                        CosUtil.formatInstantToString(modified,"dd-MM-yyyy hh:mm:ss a"));
    }
}
