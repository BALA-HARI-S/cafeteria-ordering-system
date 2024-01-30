package net.breezeware.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FoodMenu {
    private int id;
    private String name;
    private List<FoodItem> foodItems;
    private List<AvailableDay> availableDay;
    private Instant created;
    private Instant modified;

    public FoodMenu(){}
    public FoodMenu(String name,List<AvailableDay> availableDay, Instant created, Instant modified){
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

    public List<AvailableDay> getAvailableDay() {
        return availableDay;
    }

    public void setAvailableDay(List<AvailableDay> availableDay) {
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
    private String formatCreatedInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(created, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    private String formatModifiedInstant(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(modified,ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    private static String getCustomStringRepresentation(List<AvailableDay> list) {
        StringBuilder result = new StringBuilder(String.valueOf(list.get(0)));
        for (int i = 1; i < list.size(); i++) {
            result.append(",").append(list.get(i));
        }
        return result.toString();
    }
    @Override
    public String toString(){
        return "%d  |   %s  |   %s  |  %s   | %s".formatted(id, name, getCustomStringRepresentation(availableDay), formatCreatedInstant(), formatModifiedInstant());
    }
}
