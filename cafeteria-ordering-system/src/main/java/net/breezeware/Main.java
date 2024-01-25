package net.breezeware;

import net.breezeware.service.api.FoodItemManager;
import net.breezeware.service.impl.FoodItemManagerImplementation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
                    System.out.println("Cafeteria Admin Operation : Creating Food Item");
                    scanner.nextLine();
                    System.out.print("Food Item Name: ");
                    String foodItemName = scanner.nextLine();
                    System.out.print("Food Item Quantity: ");
                    int foodItemQuantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Food Item Price(₹): ");
                    double foodItemPrice = scanner.nextDouble();
                    boolean isFoodItemCreated = foodItemManager.createFoodItem(foodItemName, foodItemQuantity, foodItemPrice);
                    System.out.println(isFoodItemCreated? "Food Item Created Successfully" : "Couldn't Create Food Item");
                }
                case 2 -> {
                    System.out.println("Cafeteria Admin Operation : Display Food Item");
                    scanner.nextLine();
                    System.out.print("Food Item Name: ");
                    String foodItemName = scanner.nextLine();
                    foodItemManager.displayFoodItem(foodItemName);
                }
                case 3 -> {
                    System.out.println("Cafeteria Admin Operation : Display All Food Items");
                    scanner.nextLine();
                    System.out.print("Do you want arrange Food Items in an order?(Yes/No): ");
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
                        System.out.print("1) Ascending Order \n2) Descending Order \nOption : ");
                        int orderType = scanner.nextInt();
                        if(orderType == 1){
                            isAscending = true;
                        }
                    }
                    foodItemManager.displayAllFoodItems(isOrderBy, isAscending? 1: 2, columnName);
                }

            }
        } while (isApplicationRunning);
    }

    private static void cafeteriaAdminMenu(){
        String cafeteriaAdminOperations = """
                1) Create Food Item
                2) Display Food Item
                3) Display All Food Items
                4) Update Food Item
                5) Remove Food Item
                Press 0 to exit() Application.
                """;
        System.out.println(cafeteriaAdminOperations);
    }
}