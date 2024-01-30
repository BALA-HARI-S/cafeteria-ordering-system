package net.breezeware;

import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.service.api.FoodItemManager;
import net.breezeware.service.api.FoodMenuManager;
import net.breezeware.service.impl.FoodItemManagerImplementation;
import net.breezeware.service.impl.FoodMenuManagerImplementation;

import java.util.*;

public class Main {

    private static final FoodItemManager foodItemManager = new FoodItemManagerImplementation();
    private static final FoodMenuManager foodMenuManager = new FoodMenuManagerImplementation();
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
                    System.out.println("Cafeteria Admin Operation: Create Food Item");
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
                    System.out.println("Cafeteria Admin Operation: Delete Food Item");
                    scanner.nextLine();
                    System.out.print("Which food-item do you want to remove: ");
                    String foodItemName = scanner.nextLine();
                    boolean result = foodItemManager.removeFoodItem(foodItemName);
                    System.out.println(result? "Food Item successfully Removed!" : "Couldn't Remove the Food Item!");
                }
                case 6 -> {
                    System.out.println("Cafeteria Admin Operation: Create Food Menu");
                    scanner.nextLine();
                    System.out.print("Food Menu Name: ");
                    String foodMenuName = scanner.nextLine();
                    System.out.print("Enter Food Available Day(Day1,Day2,..): ");
                    String foodAvailableDays = scanner.nextLine().toUpperCase();
                    FoodMenu foodMenu = foodMenuManager.createFoodMenu(foodMenuName, convertStringTOList(foodAvailableDays));
                    System.out.println(foodMenuManager.getFoodMenu(foodMenu.getName()));
                }
                case 7 -> {
                    System.out.println("Cafeteria Admin Operation: View Food Menu");
                    scanner.nextLine();
                    System.out.print("Food Menu Name: ");
                    String foodMenuName = scanner.nextLine();
                    FoodMenu foodMenu = foodMenuManager.getFoodMenu(foodMenuName);
                    System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                    System.out.println(foodMenu+"\n");
                    System.out.println("""
                            Menu - Food Items:
                            name   |   quantity   |   price
                            """);
                    for (FoodItem item: foodMenuManager.getFoodMenuItems(foodMenu.getId())){
                        System.out.printf("%s   |   %d  |   %.2f",item.getName(),item.getQuantity(),item.getPrice());
                    }
                }
                case 8 -> {
                    System.out.println("Cafeteria Admin Operation: View All Food Menu");
                    List<FoodMenu> foodMenuList = foodMenuManager.getAllFoodMenus();
                    if(Objects.isNull(foodMenuList)){
                        System.out.println("Empty Food Menu List");
                    } else {
                        System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                        foodMenuList.forEach(System.out::println);
                    }
                }
                case 9 ->{
                    System.out.println("Cafeteria Admin Operation: Add Food Item to Food Menu");
                    scanner.nextLine();
                    System.out.print("Enter Food Menu Name to Add Food Item: ");
                    String foodMenuName = scanner.nextLine();
                    FoodMenu foodMenu = foodMenuManager.getFoodMenu(foodMenuName);
                    if(!Objects.isNull(foodMenu)){
                        int foodMenuId = foodMenu.getId();
                        System.out.print("Enter Food Item Name to Add: ");
                        String foodItemName = scanner.nextLine();
                        FoodItem foodItem = foodItemManager.getFoodItem(foodItemName);
                        if (!Objects.isNull(foodItem)){
                            int foodItemId = foodItem.getId();
                            boolean result = foodMenuManager.addFoodItemsToMenu(foodMenuId, foodItemId);
                            System.out.println(result? "Food Item Added to Menu!\n" : "Couldn't Add Food Item to Menu\n");
                        } else {
                            System.out.println("There is no such Food Item, Enter correct Food Item Name!\n");
                        }
                    } else {
                        System.out.println("There is no such Food Menu, Enter correct Menu Name!\n");
                    }
                }
                case 10 -> {
                    System.out.println("Cafeteria Admin Operation: Delete Food Item From Food Menu");
                    scanner.nextLine();
                    System.out.print("Enter Food Menu Name to Delete Food Item: ");
                    String foodMenuName = scanner.nextLine();
                    FoodMenu foodMenu = foodMenuManager.getFoodMenu(foodMenuName);
                    if(!Objects.isNull(foodMenu)){
                        int foodMenuId = foodMenu.getId();
                        System.out.print("Enter Food Item Name to Delete: ");
                        String foodItemName = scanner.nextLine();
                        FoodItem foodItem = foodItemManager.getFoodItem(foodItemName);
                        if (!Objects.isNull(foodItem)){
                            int foodItemId = foodItem.getId();
                            boolean result = foodMenuManager.removeFoodItemFromMenu(foodMenuId,foodItemId);
                            System.out.println(result? "Food Item Deleted From Menu!\n" : "Couldn't Delete Food Item from Menu\n");
                        } else {
                            System.out.println("There is no such Food Item, Enter correct Food Item Name!\n");
                        }
                    } else {
                        System.out.println("There is no such Food Menu, Enter correct Menu Name!\n");
                    }
                }
                case 11 -> {
                    System.out.println("Cafeteria Admin Operation: Edit Food Menu");
                    boolean isUpdatingFoodMenu = true;
                    while (isUpdatingFoodMenu){
                        System.out.print("""
                            1)Edit Food Menu Name
                            2)Edit Food Menu Available Day
                            3)Exit
                            Option :""");
                        int foodMenuEditOption = scanner.nextInt();
                        scanner.nextLine();
                        switch (foodMenuEditOption){

                            case 1 -> {
                                scanner.nextLine();
                                System.out.print("Which Food Menu Name Do you want to change?: ");
                                String foodMenuName = scanner.nextLine();
                                FoodMenu foodMenu = foodMenuManager.getFoodMenu(foodMenuName);
                                if(!Objects.isNull(foodMenu)){
                                    System.out.print("Enter a New Food Menu Name: ");
                                    String newFoodMenuName = scanner.nextLine();
                                    FoodMenu updatedMenu = foodMenuManager.editFoodMenuName(newFoodMenuName, foodMenuName);
                                    if (!Objects.isNull(updatedMenu)){
                                        System.out.println("""
                                                Food Menu Name Updated!
                                                _id   |   name   |   available_day""");
                                        System.out.printf("%d   |   %s  |   %s%n",updatedMenu.getId(),updatedMenu.getName(),updatedMenu.getAvailableDay());


                                    } else {
                                        System.out.println("Couldn't Update Food Menu Name");
                                    }
                                } else {
                                    System.out.println("There is no such Food Menu, Enter correct Menu Name!\n");
                                }
                            }

                            case 2 -> {
                                System.out.print("Which Food Menu Available Days Do you want to change?: ");
                                String foodMenuName = scanner.nextLine();
                                FoodMenu foodMenu = foodMenuManager.getFoodMenu(foodMenuName);
                                if(!Objects.isNull(foodMenu)){
                                    System.out.print("Enter a New Food Menu Available Days(Day1,Day2,..): ");
                                    String newFoodMenuAvailableDays = scanner.nextLine();
                                    List<AvailableDay> availableDays = convertStringTOList(newFoodMenuAvailableDays);
                                    FoodMenu updatedMenu = foodMenuManager.editFoodMenuAvailableDay(availableDays, foodMenuName);
                                    if (!Objects.isNull(updatedMenu)){
                                        System.out.println("""
                                                Food Menu Available Day Updated!
                                                _id   |   name   |   available_day""");
                                        System.out.printf("%d   |   %s  |   %s%n",updatedMenu.getId(),updatedMenu.getName(),
                                                getCustomStringRepresentation(updatedMenu.getAvailableDay()));
                                    } else {
                                        System.out.println("Couldn't Update Food Menu!");
                                    }
                                } else {
                                    System.out.println("There is no such Food Menu, Enter correct Menu Name!\n");
                                }
                            }

                            case 3 -> isUpdatingFoodMenu = false;
                        }
                    }
                }
                case 12 -> {
                    System.out.println("Cafeteria Admin Operation: Delete Food Menu");
                    scanner.nextLine();
                    System.out.print("Enter Food Menu Name to Delete: ");
                    String foodMenuName = scanner.nextLine();
                    FoodMenu foodMenu = foodMenuManager.getFoodMenu(foodMenuName);
                    if(!Objects.isNull(foodMenu)){
                        if(foodMenuManager.removeAllFoodItemsFromMenu(foodMenu.getId())){
                            boolean result = foodMenuManager.removeFoodMenu(foodMenu.getName());
                            System.out.println(result? "Food Menu Deleted!\n" : "Couldn't Delete Food Menu\n");
                        } else {
                            System.out.println("couldn't remove food items\n");
                        }
                    } else {
                        System.out.println("There is no such Food Menu, Enter correct Menu Name!\n");
                    }
                }
            }
        } while (isApplicationRunning);
    }

    private static void cafeteriaAdminMenu(){
        String cafeteriaOperations = """
                CAFETERIA ADMIN: FOOD ITEM
                1) Create Food Item
                2) View Food Item
                3) View All Food Items
                4) Edit Food Item
                5) Delete Food Item
                
                CAFETERIA ADMIN: FOOD MENU
                6) Create Food Menu
                7) View Food Menu
                8) View All Food Menus
                9) Add Food Item to Menu
                10) Delete Food Item from Menu
                11) Edit Food Menu
                12) Delete Food Menu
                Press 0 to exit() Application.
                """;
        System.out.println(cafeteriaOperations);
    }
    private static List<AvailableDay> convertStringTOList(String availableDay){
        List<String> availableDays = Arrays.asList(availableDay.toUpperCase().split(","));
        return availableDays.stream()
                .map(String::trim)
                .map(AvailableDay::valueOf)
                .toList();
    }
    private static String getCustomStringRepresentation(List<AvailableDay> list) {
        StringBuilder result = new StringBuilder(String.valueOf(list.get(0)));
        for (int i = 1; i < list.size(); i++) {
            result.append(",").append(list.get(i));
        }
        return result.toString();
    }
}