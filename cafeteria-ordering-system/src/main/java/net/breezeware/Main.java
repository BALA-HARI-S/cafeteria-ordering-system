package net.breezeware;

import net.breezeware.entity.FoodItem;
import net.breezeware.service.api.FoodItemManager;
import net.breezeware.service.impl.FoodItemManagerImplementation;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final FoodItemManager foodItemManager = new FoodItemManagerImplementation();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean isApplicationRunning = true;

        do{
            cafeteriaAdminMenu();
            System.out.print("How can I help you (Choose any option): ");
            int option = scanner.nextInt();
            switch (option){
                case 0 ->{
                    System.out.println("Exiting Application.....");
                    isApplicationRunning = false;
                }
                case 1 -> {
                    System.out.println("Cafeteria Admin Operation: Creating Food Item");
                    scanner.nextLine();
                    System.out.print("Food Item Name: ");
                    String foodItemName = scanner.nextLine();
                    System.out.print("Food Item Quantity: ");
                    int foodItemQuantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Food Item Price(₹): ");
                    double foodItemPrice = scanner.nextDouble();
                    FoodItem foodItem = foodItemManager.createFoodItem(foodItemName, foodItemQuantity, foodItemPrice);
                    System.out.println("Food Item successfully Created!");
                    foodItemManager.viewFoodItem(foodItem.getName());
                }
                case 2 -> {
                    System.out.println("Cafeteria Admin Operation: Display Food Item");
                    scanner.nextLine();
                    System.out.print("Food Item Name: ");
                    String foodItemName = scanner.nextLine();
                    foodItemManager.viewFoodItem(foodItemName);
                }
                case 3 -> {
                    System.out.println("Cafeteria Admin Operation: Display All Food Items");
                    scanner.nextLine();
                    System.out.print("Do you want arrange food-items in an order?(Yes/No): ");
                    String orderBy = scanner.nextLine();
                    boolean isOrderBy = false;
                    boolean isAscending = false;
                    String columnName = "";
                    if(orderBy.charAt(0) == 'y'){
                        isOrderBy = true;
                        System.out.print("""
                                Order by :
                                1) Id
                                2) Name
                                3) Quantity
                                4) Price
                                5) Created Date
                                6) Last Modified Date
                                Option :""");
                        int column = scanner.nextInt();
                        switch (column){
                            case 1 -> columnName = "_id";
                            case 2 -> columnName = "name";
                            case 3 -> columnName = "quantity";
                            case 4 -> columnName = "price";
                            case 5 -> columnName = "created";
                            case 6 -> columnName = "modified";
                        }
                        System.out.print("""
                                1) Ascending Order
                                2) Descending Order
                                Option :""");
                        int orderType = scanner.nextInt();
                        if(orderType == 1){
                            isAscending = true;
                        }
                    }
                    foodItemManager.viewAllFoodItems(isOrderBy, isAscending? 1: 2, columnName);
                }
                case 4 -> {
                    System.out.println("Cafeteria Admin Operation: Edit Food Item");
                    System.out.print("""
                            1) Edit Food Item Name
                            2) Edit Food Item Quantity
                            3) Edit Food Item Price
                            Option :""");
                    int editOption = scanner.nextInt();
                    switch (editOption){
                        case 1 -> {
                            System.out.println("Cafeteria Admin Operation: Edit Food Item Name");
                            scanner.nextLine();
                            System.out.print("Which food-item name do you want to change: ");
                            String foodItem = scanner.nextLine();
                            System.out.print("Enter a New name for the food-item: ");
                            String newName = scanner.nextLine();
                            FoodItem updatedFoodItem = foodItemManager.editFoodItemName(newName, foodItem);
                            if(Objects.isNull(updatedFoodItem)){
                                System.out.println("There is no such Food Item, Enter a valid food-item name!");
                            } else {
                                foodItemManager.viewFoodItem(updatedFoodItem.getName());
                            }
                        }
                        case 2 -> {
                            System.out.println("Cafeteria Admin Operation: Edit Food Item Quantity");
                            scanner.nextLine();
                            System.out.print("Which food-item quantity do you want to change: ");
                            String foodItem = scanner.nextLine();
                            System.out.print("Enter a New quantity for the food-item: ");
                            int newQuantity = scanner.nextInt();
                            FoodItem updatedFoodItem = foodItemManager.editFoodItemQuantity(newQuantity, foodItem);
                            if(Objects.isNull(updatedFoodItem)){
                                System.out.println("There is no such Food Item, Enter a valid food-item name!");
                            } else {
                                foodItemManager.viewFoodItem(updatedFoodItem.getName());
                            }
                        }
                        case 3 -> {
                            System.out.println("Cafeteria Admin Operation: Edit Food Item Price");
                            scanner.nextLine();
                            System.out.print("Which food-item price do you want to change: ");
                            String foodItem = scanner.nextLine();
                            System.out.print("Enter a New price for the food-item: ");
                            double newPrice = scanner.nextDouble();
                            FoodItem updatedFoodItem = foodItemManager.editFoodItemPrice(newPrice, foodItem);
                            if(Objects.isNull(updatedFoodItem)){
                                System.out.println("There is no such Food Item, Enter a valid food-item name!");
                            } else {
                                foodItemManager.viewFoodItem(updatedFoodItem.getName());
                            }
                        }

                    }
                }
                case 5 ->{
                    System.out.println("Cafeteria Admin Operation: Remove Food Item");
                    scanner.nextLine();
                    System.out.print("Which food-item do you want to remove: ");
                    String foodItemName = scanner.nextLine();
                    boolean result = foodItemManager.removeFoodItem(foodItemName);
                    System.out.println(result? "Food Item successfully Removed!" : "Couldn't Remove the Food Item!");
                }

            }
        } while (isApplicationRunning);
    }

    private static void cafeteriaAdminMenu(){
        String cafeteriaOperations = """
                CAFETERIA ADMIN
                1) Create Food Item
                2) View Food Item
                3) View All Food Items
                4) Edit Food Item
                5) Delete Food Item
                
                Press 0 to exit() Application.
                """;
        System.out.println(cafeteriaOperations);
    }
}