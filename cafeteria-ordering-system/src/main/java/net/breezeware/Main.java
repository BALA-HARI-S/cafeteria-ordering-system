package net.breezeware;

import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.Order;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.*;
import net.breezeware.service.impl.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

public class Main {

    private static final FoodItemManager foodItemManager = new FoodItemManagerImplementation();
    private static final FoodMenuManager foodMenuManager = new FoodMenuManagerImplementation();
    private  static final PlaceOrderManager placeOrderManager = new PlaceOrderManagerImplementation();
    private static final DeliveryManager deliveryManger = new DeliveryManagerImplementation();
    private static final OrderManager orderManager = new OrderManagerImplementation();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws CustomException {

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
                    try{
                        System.out.println("""
                                Food Item Already Exist
                                _id | name | quantity | price | created | modified
                                """+foodItemManager.retrieveFoodItem(foodItemName));
                    } catch (CustomException e){
                        FoodItem foodItem = foodItemManager.createFoodItem(foodItemName, foodItemQuantity, foodItemPrice);
                        System.out.println("""
                                Food Item Created
                                _id | name | quantity | price | created | modified
                                """+foodItem);
                    }
                }
                case 2 -> {
                    System.out.println("Cafeteria Admin Operation: Display Food Item");
                    scanner.nextLine();
                    System.out.print("Food Item Name: ");
                    String foodItemName = scanner.nextLine();
                    try{
                        System.out.println("""
                                _id | name | quantity | price | created | modified
                                """+foodItemManager.retrieveFoodItem(foodItemName));
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.println("Cafeteria Admin Operation: Display All Food Items");
                    scanner.nextLine();
                    System.out.print("Do you want arrange food-items in an order?(Yes/No): ");
                    String orderBy = scanner.nextLine();
                    boolean isOrderBy = false;
                    boolean isAscending = false;
                    String columnName = "";
                    if(orderBy.toLowerCase().charAt(0) == 'y'){
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
                    try{
                        List<FoodItem> foodItems = foodItemManager.retrieveAllFoodItems(isOrderBy, isAscending? 1: 2, columnName);
                        System.out.println("_id | name | quantity | price | created | modified");
                        foodItems.forEach(System.out::println);
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }

                }
                case 4 -> {
                    System.out.println("Cafeteria Admin Operation: Edit Food Item");
                    boolean isEditingFoodItem = true;
                    while(isEditingFoodItem){
                        System.out.print("""
                            1) Edit Food Item Name
                            2) Edit Food Item Price
                            3) Exit
                            Option :""");
                        int editOption = scanner.nextInt();
                        switch (editOption){
                            case 1 -> {
                                System.out.println("Cafeteria Admin Operation: Edit Food Item Name");
                                List<FoodItem> foodItems = foodItemManager.retrieveAllFoodItems(true, 1, "_id");
                                System.out.println("_id | name | quantity | price | created | modified");
                                foodItems.forEach(System.out::println);
                                scanner.nextLine();
                                System.out.print("Which Food Item Name do you want to change(Food Item Name): ");
                                String foodItem = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                try{
                                    System.out.println("Changing Name for Food Item : "+foodItemManager.retrieveFoodItem(foodItem).getName());
                                    System.out.print("Enter a New name for the Food Item: ");
                                    String newFoodItemName = scanner.nextLine();
                                    try{
                                        System.out.println("""
                                                Food Item Already Exist with that Name
                                                _id | name | quantity | price | created | modified
                                                """+foodItemManager.retrieveFoodItem(newFoodItemName));
                                    } catch (CustomException e){
                                        FoodItem updatedFoodItem = foodItemManager.updateFoodItemName(newFoodItemName, foodItem);
                                        System.out.println("""
                                                Food Item Updated
                                                _id | name | quantity | price | created | modified
                                                """+foodItemManager.retrieveFoodItem(updatedFoodItem.getName()));
                                    }
                                } catch (CustomException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            case 2 -> {
                                System.out.println("Cafeteria Admin Operation: Edit Food Item Price");
                                List<FoodItem> foodItems = foodItemManager.retrieveAllFoodItems(true, 1, "_id");
                                System.out.println("_id | name | quantity | price | created | modified");
                                foodItems.forEach(System.out::println);
                                scanner.nextLine();
                                System.out.print("Which food-item price do you want to change(Food Item Name): ");
                                String foodItem = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                try{
                                    System.out.println("Changing Price for Food Item : "+foodItemManager.retrieveFoodItem(foodItem).getName());
                                    System.out.print("Enter a New Price for the food-item(₹0.00): ");
                                    double newPrice = scanner.nextDouble();
                                    FoodItem updatedFoodItem = foodItemManager.updateFoodItemPrice(newPrice, foodItem);
                                    System.out.println("""
                                            Food Item Updated
                                            _id | name | quantity | price | created | modified
                                            """+foodItemManager.retrieveFoodItem(updatedFoodItem.getName()));
                                } catch (CustomException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            case 3 -> {
                                isEditingFoodItem = false;
                            }
                        }
                    }
                }
                case 5 ->{
                    System.out.println("Cafeteria Admin Operation: Delete Food Item");
                    List<FoodItem> foodItems = foodItemManager.retrieveAllFoodItems(true, 1, "_id");
                    System.out.println("_id | name | quantity | price | created | modified");
                    foodItems.forEach(System.out::println);
                    scanner.nextLine();
                    System.out.print("Which food-item do you want to remove(Food Item Name): ");
                    String foodItemName = scanner.nextLine();
                    try{
                        boolean result = foodItemManager.deleteFoodItem(foodItemName);
                        System.out.println(result? "Food Item successfully Removed!" : "No such Food Item");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 6 -> {
                    try{
                        boolean isUpdatingFoodItemQuantity = true;
                        while(isUpdatingFoodItemQuantity){
                            System.out.println("Today's Menu");
                            List<FoodMenu> todayFoodMenu = foodMenuManager.retrieveFoodMenuOfTheDay();

                            List<FoodItem> todayMenuFoodItems = new ArrayList<>();
                            for(FoodMenu menu: todayFoodMenu){
                                System.out.println("Food Menu : " + menu.getName());
                                todayMenuFoodItems = foodMenuManager.retrieveFoodMenuItems(menu.getId());
                                System.out.println("_id    |   name    |    Quantity    |   price");
                                todayMenuFoodItems.forEach(foodItem -> System.out.printf("%d    |   %s  |   %d  |   %.2f%n",
                                        foodItem.getId(), foodItem.getName(), foodItem.getQuantity(), foodItem.getPrice()));
                                scanner.nextLine();
                            }
                            System.out.print("Do you want to Update Today Food Menu's Food Item Quantity?(Yes/No): ");
                            String updateFoodItemQuantity = scanner.nextLine();
                            if(updateFoodItemQuantity.toLowerCase().charAt(0) == 'y'){
                                System.out.print("Choose Food Item from the above Menu to update it's quantity(Food Item Name): ");
                                String foodItemNameFromMenu = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                boolean isValidFoodItem = false;
                                for(FoodItem item: todayMenuFoodItems){
                                    if (foodItemNameFromMenu.equals(item.getName())) {
                                        isValidFoodItem = true;
                                        break;
                                    }
                                }
                                if(isValidFoodItem){
                                    System.out.println("Changing Quantity of " + foodItemNameFromMenu);
                                    System.out.print("Enter Quantity for this Food Item '" + foodItemNameFromMenu +"' : ");
                                    int foodItemQuantity = scanner.nextInt();
                                    foodItemManager.updateFoodItemQuantity(foodItemQuantity, foodItemNameFromMenu);
                                    System.out.println("Food Item Quantity Updated!");
                                } else {
                                    System.out.println("This Food Item is not in this Food Menu");
                                }
                            } else {
                                isUpdatingFoodItemQuantity = false;
                            }
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 7 -> {
                    System.out.println("Cafeteria Admin Operation: Create Food Menu");
                    scanner.nextLine();
                    System.out.print("Food Menu Name: ");
                    String foodMenuName = scanner.nextLine();
                    System.out.print("Enter Food Available Day(Day1,Day2,..): ");
                    String foodAvailableDays = scanner.nextLine().toUpperCase();
                    try{
                        System.out.println("""
                                Food Menu Already Exist
                                _id | name | available_days | created | modified
                                """+ foodMenuManager.retrieveFoodMenu(foodMenuName));
                    } catch (CustomException e){
                        FoodMenu newFoodMenu = foodMenuManager.createFoodMenu(foodMenuName, convertStringTOList(foodAvailableDays));
                        System.out.println("""
                                Food Item Created
                                _id | name | available_days | created | modified
                                """+newFoodMenu);
                    }
                }
                case 8 -> {
                    System.out.println("Cafeteria Admin Operation: View Food Menu");
                    scanner.nextLine();
                    System.out.print("Food Menu Name: ");
                    String foodMenuName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                    try{
                        FoodMenu foodMenu = foodMenuManager.retrieveFoodMenu(foodMenuName);
                        System.out.println("""
                                _id  |   name    |   available_day   |    created    |    modified
                                """+foodMenu);
                        System.out.println("""
                            Menu - Food Items:
                            name   |   quantity   |   price""");
                        for (FoodItem item: foodMenuManager.retrieveFoodMenuItems(foodMenu.getId())){
                            System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 9 -> {
                    System.out.println("Cafeteria Admin Operation: View All Food Menus");
                    scanner.nextLine();
                    System.out.print("Do you want arrange food-menus in an order?(Yes/No): ");
                    String orderBy = scanner.nextLine();
                    boolean isOrderBy = false;
                    boolean isAscending = false;
                    String columnName = "";
                    if(orderBy.toLowerCase().charAt(0) == 'y'){
                        isOrderBy = true;
                        System.out.print("""
                                Order by :
                                1) Id
                                2) Name
                                3) Created Date
                                4) Last Modified Date
                                Option :""");
                        int column = scanner.nextInt();
                        switch (column){
                            case 1 -> columnName = "_id";
                            case 2 -> columnName = "name";
                            case 3 -> columnName = "created";
                            case 4 -> columnName = "modified";
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
                    try{
                        List<FoodMenu> foodMenuList = foodMenuManager.retrieveAllFoodMenus(isOrderBy, isAscending? 1: 2, columnName);
                        if(Objects.isNull(foodMenuList)){
                            System.out.println("Empty Food Menu List");
                        } else {
                            System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                            foodMenuList.forEach(System.out::println);
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 10 ->{
                    System.out.println("Cafeteria Admin Operation: Add Food Item to Food Menu");
                    try{
                        System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                        foodMenuManager.retrieveAllFoodMenus(true,1, "_id").forEach(System.out::println);
                        scanner.nextLine();

                        System.out.print("Enter Food Menu Name to Add Food Item: ");
                        String foodMenuName = scanner.nextLine();
                        FoodMenu foodMenu = foodMenuManager.retrieveFoodMenu(foodMenuName);
                        int foodMenuId = foodMenu.getId();

                        foodItemManager.retrieveAllFoodItems(true,1,"_id").forEach(System.out::println);

                        boolean isAddingFoodItemsToMenu = true;
                        while(isAddingFoodItemsToMenu){
                            System.out.print("Enter Food Item Name to Add: ");
                            String foodItemName = scanner.nextLine();
                            FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                            int foodItemId = foodItem.getId();
                            foodMenuManager.addFoodItemsToMenu(foodMenuId, foodItemId);
                            System.out.print("Do you want to Add another Food Item?(Yes/No): ");
                            String addFodItem = scanner.nextLine();
                            if(addFodItem.toLowerCase().charAt(0) == 'n'){
                                isAddingFoodItemsToMenu = false;
                            }
                        }
                        System.out.println("Food Item Added to Menu!");

                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 11 -> {
                    System.out.println("Cafeteria Admin Operation: Delete Food Item From Food Menu");
                    try{
                        System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                        foodMenuManager.retrieveAllFoodMenus(true,1, "_id").forEach(System.out::println);
                        scanner.nextLine();
                        System.out.print("Enter Food Menu Name to Delete Food Item: ");
                        String foodMenuName = scanner.nextLine();
                        FoodMenu foodMenu = foodMenuManager.retrieveFoodMenu(foodMenuName);
                        int foodMenuId = foodMenu.getId();

                        System.out.println("_id | name | quantity | price | created | modified");
                        foodMenuManager.retrieveFoodMenuItems(foodMenuId).forEach(System.out::println);

                        System.out.print("Enter Food Item Name to Delete: ");
                        String foodItemName = scanner.nextLine();
                        FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                        int foodItemId = foodItem.getId();

                        boolean result = foodMenuManager.deleteFoodItemFromMenu(foodMenuId,foodItemId);
                        System.out.println(result? "Food Item Deleted From Menu!" : "Couldn't Delete Food Item from Menu!");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 12 -> {
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
                                try{
                                    System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                                    foodMenuManager.retrieveAllFoodMenus(true,1, "_id").forEach(System.out::println);
                                    System.out.print("Enter the Food Menu Name that you want to change: ");
                                    String foodMenuName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                    foodMenuManager.retrieveFoodMenu(foodMenuName);
                                    System.out.print("Enter a New Food Menu Name: ");
                                    String newFoodMenuName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                    FoodMenu updatedMenu = foodMenuManager.updateFoodMenuName(newFoodMenuName, foodMenuName);
                                    System.out.println("""
                                                Food Menu Name Updated!
                                                _id   |   name   |   available_day""");
                                    System.out.printf("%d   |   %s  |   %s%n",updatedMenu.getId(),updatedMenu.getName(),updatedMenu.getAvailableDay());
                                } catch (CustomException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            case 2 -> {
                                try{
                                    System.out.println("Which Food Menu Available Days Do you want to change?");
                                    System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                                    foodMenuManager.retrieveAllFoodMenus(true,1, "_id").forEach(System.out::println);
                                    System.out.print("Enter Food Menu Name: ");
                                    String foodMenuName = scanner.nextLine();
                                    foodMenuManager.retrieveFoodMenu(foodMenuName);

                                    System.out.print("Enter a New Food Menu Available Days(Day1,Day2,..): ");
                                    String newFoodMenuAvailableDays = scanner.nextLine();
                                    List<AvailableDay> availableDays = convertStringTOList(newFoodMenuAvailableDays);
                                    FoodMenu updatedMenu = foodMenuManager.updateFoodMenuAvailableDay(availableDays, foodMenuName);
                                    System.out.println("""
                                                Food Menu Available Day Updated!
                                                _id   |   name   |   available_day""");
                                    System.out.printf("%d   |   %s  |   %s%n",updatedMenu.getId(),updatedMenu.getName(),
                                            getCustomStringRepresentation(updatedMenu.getAvailableDay()));
                                } catch (CustomException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            case 3 -> isUpdatingFoodMenu = false;
                        }
                    }
                }
                case 13 -> {
                    System.out.println("Cafeteria Admin Operation: Delete Food Menu");
                    try{
                        System.out.println("_id  |   name    |   available_day   |    created    |    modified");
                        foodMenuManager.retrieveAllFoodMenus(true,1, "_id").forEach(System.out::println);
                        scanner.nextLine();
                        System.out.print("Enter Food Menu Name to Delete: ");
                        String foodMenuName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                        boolean result = foodMenuManager.deleteFoodMenu(foodMenuName);
                        System.out.println(result? "Food Menu Deleted!" : "Couldn't Delete Food Menu!");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 14 -> {
                    System.out.println("Customer Operation : View Food Menu of the Day");
                    try{
                        List<FoodMenu> foodMenuOfTheDay = foodMenuManager.retrieveFoodMenuOfTheDay();
                        for(FoodMenu menu: foodMenuOfTheDay){
                            System.out.println("Menu : " + menu.getName());
                            System.out.println("Food Item  |  Quantity  |  Price");
                            for (FoodItem item: foodMenuManager.retrieveFoodMenuItems(menu.getId())){
                                System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                            }
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 15 -> {
                    System.out.println("Customer Operation : Create Order \nProvide Order Details");
                    scanner.nextLine();
                    try{
                        System.out.println("Select Food Items to Add to Order");
                        List<FoodMenu> foodMenuOfTheDay = foodMenuManager.retrieveFoodMenuOfTheDay();
                        for(FoodMenu menu: foodMenuOfTheDay){
                            System.out.println("Menu : " + menu.getName());
                            System.out.println("Food Item  |  Quantity  |  Price");
                            for (FoodItem item: foodMenuManager.retrieveFoodMenuItems(menu.getId())){
                                System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                            }
                        }
                        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
                        double totalCost = 0.00;
                        boolean isAddingFoodItems = true;
                        while(isAddingFoodItems){
                            System.out.print("Food Item Name: ");
                            String foodItemName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                            System.out.print("Food Item Quantity: ");
                            int foodItemQuantity = scanner.nextInt();
                            try{
                                FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                for(FoodMenu menu: foodMenuOfTheDay){
                                    FoodItem foodMenuItem = foodMenuManager.retrieveFoodMenuItem(menu.getId(), foodItem.getId());
                                    if(foodItemsQuantityMap.containsKey(foodMenuItem.getName())){
                                        System.out.print("You have this food item already in orders list" +
                                                "\nDo you want to update its quantity?(Yes/No): ");
                                        scanner.nextLine();
                                        String updateFoodItemQuantity = scanner.nextLine();
                                        if (updateFoodItemQuantity.toLowerCase().charAt(0) == 'y') {
                                            foodItemsQuantityMap.put(foodItem.getName(), foodItemsQuantityMap.get(foodItem.getName()) + foodItemQuantity);
                                            totalCost += foodItem.getPrice() * foodItemQuantity;
                                            System.out.printf("Food Item | Quantity %n%s    %d%nFood Item Quantity Updated%n", foodItem.getName()
                                                    ,foodItemsQuantityMap.get(foodItem.getName()));
                                        }
                                    } else {
                                        foodItemsQuantityMap.put(foodMenuItem.getName(), foodItemQuantity);
                                        totalCost += foodMenuItem.getPrice() * foodItemQuantity;
                                        System.out.println("Food Item Added to Orders List");
                                        scanner.nextLine();
                                    }
                                }
                            } catch (CustomException e){
                                System.out.println(e.getMessage());
                            }
                            System.out.print("Want to Add Food Item?(Yes/No): ");
                            String addFoodItem = scanner.nextLine();
                            if(addFoodItem.toLowerCase().charAt(0) == 'n'){
                                isAddingFoodItems = false;
                            }
                        }
                        System.out.print("Enter Delivery Location: ");
                        String deliveryLocation = scanner.nextLine();
                        System.out.print("Enter Delivery Date And Time(dd-MM-yyyy HH-mm-ss am/pm): ");
                        String deliveryDateTime = scanner.nextLine();
                        if (isDateTimeFormatValid(deliveryDateTime)) {
                            System.out.println("Input date-time does not match the pattern.");
                            return;
                        }
                        placeOrderManager.createOrder(foodItemsQuantityMap, totalCost, deliveryLocation, deliveryDateTime);
                        System.out.println("Order Created!");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 16 -> {
                    System.out.println("Customer Operation : View Cart");
                    try{
                        List<Order> cartOrders = placeOrderManager.retrieveCartOrders();
                        if(cartOrders.isEmpty()){
                            System.out.println("Your Cart is Empty!");
                        } else {
                            System.out.println("Id   |   Total Cost   |    Order Status    |   Order Created ");
                            for(Order order: cartOrders){
                                System.out.println(placeOrderManager.retrieveOrder(order.getId()));
                                HashMap<String, Integer> foodItemsQuantityMap = placeOrderManager.retrieveOrderedFoodItems(order.getId());
                                System.out.println("Food Item  |  Quantity");
                                for(String foodItemName: foodItemsQuantityMap.keySet()){
                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                    System.out.printf("%s   %d%n", foodItem.getName(), foodItemsQuantityMap.get(foodItemName));
                                }
                            }
                        }
                    } catch (CustomException | NullPointerException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 17 -> {
                    System.out.println("Customer Operation: Edit Order");
                    boolean isEditingOrder = true;
                    while (isEditingOrder){
                        System.out.print("""
                            1) Edit Food Items
                            2) Edit Delivery Location
                            3) Edit Delivery Date Time
                            4) Exit
                            Option :""");
                        int orderEditOption = scanner.nextInt();
                        switch (orderEditOption){
                            case 1 -> {
                                System.out.println("Editing Food Items in Cart");
                                try{
                                    List<Order> cartOrders = placeOrderManager.retrieveCartOrders();
                                    if(cartOrders.isEmpty()){
                                        System.out.println("Your Cart is Empty!");
                                    } else {
                                        System.out.println("Id   |   Total Cost   |    Order Status    |   Order Created ");
                                        for(Order order: cartOrders){
                                            System.out.println(placeOrderManager.retrieveOrder(order.getId()));
                                            HashMap<String, Integer> foodItemsQuantityMap = placeOrderManager.retrieveOrderedFoodItems(order.getId());
                                            System.out.println("Food Item  |  Quantity");
                                            for(String foodItemName: foodItemsQuantityMap.keySet()){
                                                FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                                System.out.printf("%s   %d%n", foodItem.getName(), foodItemsQuantityMap.get(foodItemName));
                                            }
                                        }

                                        System.out.print("Choose Order From Cart to Edit Food Items(Order Id): ");
                                        int cartOrderId = scanner.nextInt();
                                        boolean isEditingFoodItem = true;
                                        while (isEditingFoodItem) {
                                            System.out.print("""
                                                    1) Add Food Item
                                                    2) Remove Food Item
                                                    3) Exit
                                                    Option :""");
                                            int foodItemEditOption = scanner.nextInt();
                                            switch (foodItemEditOption) {
                                                case 1 -> {
                                                    HashMap<String, Integer> cartFoodItems = placeOrderManager.retrieveOrderedFoodItems(cartOrderId);
                                                    scanner.nextLine();
                                                    System.out.println("Select Food Items to Add to Cart Order");
                                                    List<FoodMenu> foodMenuOfTheDay = foodMenuManager.retrieveFoodMenuOfTheDay();
                                                    for (FoodMenu menu : foodMenuOfTheDay) {
                                                        System.out.println("Menu : " + menu.getName());
                                                        System.out.println("Food Item  |  Quantity  |  Price");
                                                        for (FoodItem item : foodMenuManager.retrieveFoodMenuItems(menu.getId())) {
                                                            System.out.printf("%s   |   %d  |   %.2f%n", item.getName(), item.getQuantity(), item.getPrice());
                                                        }
                                                    }

                                                    System.out.print("Food Item Name: ");
                                                    String foodItemName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                                    System.out.print("Food Item Quantity: ");
                                                    int foodItemQuantity = scanner.nextInt();
                                                    double totalCost = placeOrderManager.retrieveOrder(cartOrderId).getTotalCost();
                                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                                    for (FoodMenu menu : foodMenuOfTheDay) {
                                                        FoodItem foodMenuItem = foodMenuManager.retrieveFoodMenuItem(menu.getId(), foodItem.getId());
                                                        if (cartFoodItems.containsKey(foodMenuItem.getName())) {
                                                            System.out.print("You have this food item already in orders list" +
                                                                    "\nDo you want to update its quantity?(Yes/No): ");
                                                            scanner.nextLine();
                                                            String updateFoodItemQuantity = scanner.nextLine();
                                                            if (updateFoodItemQuantity.toLowerCase().charAt(0) == 'y') {
                                                                cartFoodItems.put(foodItem.getName(), cartFoodItems.get(foodItem.getName()) + foodItemQuantity);
                                                                totalCost += foodItem.getPrice() * foodItemQuantity;
                                                                System.out.printf("Food Item | Quantity %n%s    %d%nFood Item Quantity Updated%n", foodItem.getName()
                                                                        , cartFoodItems.get(foodItem.getName()));
                                                            }
                                                        } else {
                                                            cartFoodItems.put(foodMenuItem.getName(), foodItemQuantity);
                                                            totalCost += foodMenuItem.getPrice() * foodItemQuantity;
                                                            System.out.println("Food Item Added to Orders List");
                                                            scanner.nextLine();
                                                        }
                                                    }
                                                    placeOrderManager.editFoodItemsInOrder(cartOrderId, foodItem.getName(), cartFoodItems.get(foodItem.getName()));
                                                    System.out.println("Cart Order Updated!");
                                                }
                                                case 2 -> {
                                                    HashMap<String, Integer> cartFoodItems = placeOrderManager.retrieveOrderedFoodItems(cartOrderId);
                                                    scanner.nextLine();
                                                    System.out.println("Select Food Items to Remove from Cart Order");
                                                    System.out.print("Food Item Name to Remove from Cart Order: ");
                                                    String foodItemName = capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                                    if(cartFoodItems.containsKey(foodItem.getName())){
                                                         placeOrderManager.deleteCartOrderFoodItem(cartOrderId, foodItemName);
                                                         System.out.println("Food Item deleted From this Cart Order");
                                                    } else {
                                                        System.out.println("Food Item Not in this Cart Order");
                                                    }


                                                }
                                                case 3 -> isEditingFoodItem = false;
                                            }
                                        }
                                    }
                                } catch (CustomException | NullPointerException e){
                                    System.out.println(e.getMessage());
                                }


                            }
                            case 4 -> {
                                isEditingOrder = false;
                            }
                        }
                    }
                }
                case 18 -> {
                    System.out.println("Customer Operation : Confirm Order");
                    try{
                        List<Order> cartOrders = placeOrderManager.retrieveCartOrders();
                        if(cartOrders.isEmpty()){
                            System.out.println("Your Cart is Empty!");
                        } else {
                            System.out.println("Id   |   Total Cost   |    Order Status    |   Order Created ");
                            for(Order order: cartOrders){
                                System.out.println(placeOrderManager.retrieveOrder(order.getId()));
                                HashMap<String, Integer> foodItemsQuantityMap = placeOrderManager.retrieveOrderedFoodItems(order.getId());
                                System.out.println("Food Item  |  Quantity");
                                for(String foodItemName: foodItemsQuantityMap.keySet()){
                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                    System.out.printf("%s   %d%n", foodItem.getName(), foodItemsQuantityMap.get(foodItemName));
                                }
                            }
                        }
                        System.out.print("Enter Order Id to Confirm Order: ");
                        int orderId = scanner.nextInt();
                        placeOrderManager.confirmOrder(orderId);
                        System.out.println("Order Confirmed");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 19 -> {
                    System.out.println("Customer Operation : Cancel the Order");
                    try{
                        List<Order> cartOrders = placeOrderManager.retrieveConfirmedOrders();
                        if(cartOrders.isEmpty()){
                            System.out.println("Your Order is Empty!");
                        } else {
                            System.out.println("Id   |   Total Cost   |    Order Status    |   Order Created ");
                            for(Order order: cartOrders){
                                System.out.println(placeOrderManager.retrieveOrder(order.getId()));
                                HashMap<String, Integer> foodItemsQuantityMap = placeOrderManager.retrieveOrderedFoodItems(order.getId());
                                System.out.println("Food Item  |  Quantity");
                                for(String foodItemName: foodItemsQuantityMap.keySet()){
                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                    System.out.printf("%s   %d%n", foodItem.getName(), foodItemsQuantityMap.get(foodItemName));
                                }
                            }
                        }
                        System.out.print("Enter Order Id to Cancel Order: ");
                        int orderId = scanner.nextInt();
                        placeOrderManager.confirmOrder(orderId);
                        System.out.println("Order Cancelled");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 20 -> {
                    System.out.println("Cafeteria staff : List Active Orders");
                    System.out.println("""
                            Active Orders!
                            _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                            """);
                    List<Order> activeOrders = orderManager.getActiveOrders();
                    if(!Objects.isNull(activeOrders)){
                        for(Order order: activeOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Active Orders");
                    }
                }
                case 21 -> {
                    System.out.println("Cafeteria staff : List Received Orders");
                    List<Order> receivedOrders = orderManager.getReceivedOrders();
                    if(!Objects.isNull(receivedOrders)){
                        System.out.println("""
                                Received Orders!
                                _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                                """);
                        for(Order order: receivedOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Received Orders");
                    }
                }
                case 22 -> {
                    System.out.println("Cafeteria staff : List Cancelled Orders");
                    List<Order> cancelledOrders = orderManager.getCancelledOrders();
                    if(!Objects.isNull(cancelledOrders)){
                        System.out.println("""
                                Cancelled Orders!
                                _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                                """);
                        for(Order order: cancelledOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Cancelled Orders");
                    }
                }
                case 23 -> {
                    System.out.println("Cafeteria staff : List Completed Orders");
                    List<Order> completedOrders = orderManager.getCompletedOrders();
                    if(!Objects.isNull(completedOrders)){
                        System.out.println("""
                                Completed Orders!
                                _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                                """);
                        for(Order order: completedOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Completed Orders");
                    }
                }
                case 24 -> {
                    System.out.println("Cafeteria staff : Prepare Order");
                    List<Order> receivedOrders = orderManager.getReceivedOrders();
                    if(!Objects.isNull(receivedOrders)){
                        System.out.println("""
                                Received Orders!
                                _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                                """);
                        for(Order order: receivedOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Received Orders");
                    }
                    System.out.print("Select the Order to start preparation(Order id: ");
                    int orderId = scanner.nextInt();
                    boolean result = orderManager.prepareOrder(orderId);
                    System.out.println("""
                            Order Prepared!
                            _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                            """);
                            orderManager.getActiveOrders().stream().filter(order -> order.getId() == orderId).forEach(System.out::println);
                }
                case 25 -> {
                    System.out.println("Delivery Staff : Deliver Order");
                    List<Order> activeOrders = orderManager.getActiveOrders();
                    if(!Objects.isNull(activeOrders)){
                        System.out.println("""
                                Active Orders!
                                _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                                """);
                        for(Order order: activeOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Active Orders");
                    }
                    System.out.print("Select the Order to Deliver(Order id): ");
                    int orderId = scanner.nextInt();
                    boolean result = deliveryManger.deliverOrder(orderId);
                    System.out.println("""
                            Order Delivered!
                            _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                            """);
                    orderManager.getCompletedOrders().stream().filter(order -> order.getId() == orderId).forEach(System.out::println);
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
                6) View Food Menu of the Day
                7) Create Food Menu
                8) View Food Menu
                9) View All Food Menus
                10) Add Food Item to Menu
                11) Delete Food Item from Menu
                12) Edit Food Menu
                13) Delete Food Menu
                
                CUSTOMER : PLACE ORDER
                14) View Food Menu
                15) Create Order
                16) View Card
                17) Edit Order
                18) Confirm Order
                    View Confirmed Orders
                19) Cancel Order
                    View Cancelled Orders
                
                CAFETERIA STAFF
                20) List Active Orders
                21) List Received Orders
                22) List Cancelled Orders
                23) List Completed Orders
                24) Prepare Order
                
                DELIVERY STAFF
                25) Deliver Order
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

    private static boolean isDateTimeFormatValid(String inputDateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss a");
            LocalDateTime parse = LocalDateTime.parse(inputDateTime, formatter);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private  static String formatDateTimeInstant(Instant instant){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }

    private static String capitalizeFirstLetter(String input) {
        StringBuilder result = new StringBuilder();
        String[] words = input.split("\\s");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
            }
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - 1);
        }
        return result.toString();
    }
}