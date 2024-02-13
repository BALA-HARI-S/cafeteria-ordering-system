package net.breezeware.entity;

import net.breezeware.utils.CosUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FoodMenu {
    private int id;
    private String name;
    private List<FoodItem> foodItems;
    private List<MenuAvailability> availableDay;
    private Instant created;
    private Instant modified;

    public FoodMenu(){}
    public FoodMenu(String name, List<MenuAvailability> availableDay, Instant created, Instant modified){
        this.name = name;
        this.availableDay = new ArrayList<>(availableDay);
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

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public List<MenuAvailability> getAvailableDay() {
        return availableDay;
    }

    public void setAvailableDay(List<MenuAvailability> availableDay) {
        this.availableDay = availableDay;
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
    public String toString(){
        return "%d  |   %s  |   %s  |  %s   | %s".formatted(id, name, MenuAvailability.daysToString(availableDay),
                CosUtil.formatInstantToString(created,"dd-MM-yyyy hh:mm:ss a"),
                CosUtil.formatInstantToString(modified,"dd-MM-yyyy hh:mm:ss a"));
    }
}
