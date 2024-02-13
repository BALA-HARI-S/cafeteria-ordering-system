package net.breezeware;

import net.breezeware.dataStore.FoodItemDataStore;
import net.breezeware.dataStore.FoodMenuDataStore;
import net.breezeware.dataStore.OrderDataStore;
import net.breezeware.entity.*;
import net.breezeware.exception.CustomException;
import net.breezeware.service.api.*;
import net.breezeware.service.impl.*;
import net.breezeware.utils.CosUtil;

import java.util.*;

public class Main {

    private static final FoodItemDataStore foodItemDataStore = new FoodItemDataStore();
    private static final FoodMenuDataStore foodMenuDataStore = new FoodMenuDataStore();
    private static final OrderDataStore orderDataStore = new OrderDataStore();

    private static final ManageFoodItem foodItemManager = new FoodItemManagerImplementation(foodItemDataStore);
    private static final ManageFoodMenu foodMenuManager = new FoodMenuManagerImplementation(foodMenuDataStore);
    private static final ManageOrder orderManager = new OrderManagerImplementation(orderDataStore);
    private static final ManageDelivery deliveryManger = new DeliveryManagerImplementation(orderDataStore);

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws CustomException {

        boolean isApplicationRunning = true;

        do{
            try{
                openConnections();
            } catch (CustomException e){
                System.out.println(e.getMessage());
            }
            cafeteriaAdminMenu();
            System.out.print("How can I help you (Choose any option): ");
            int option = scanner.nextInt();
            switch (option){
                case 0 ->{
                    try{
                        closeConnections();
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                    System.out.println("Exiting Application.....");
                    isApplicationRunning = false;
                }
                case 1 -> {
                    System.out.println("Cafeteria Admin Operation: Create Food Item");
                    scanner.nextLine();
                    System.out.print("Food Item Name: ");
                    String foodItemName = scanner.nextLine();
                    System.out.print("Food Item Price(₹): ");
                    double foodItemPrice = scanner.nextDouble();
                    try{
                        System.out.println("""
                                Food Item Already Exist
                                _id | name | quantity | price | created | modified
                                """+foodItemManager.retrieveFoodItem(foodItemName));
                    } catch (CustomException e){
                        try{
                            FoodItem foodItem = foodItemManager.createFoodItem(foodItemName, foodItemPrice);
                            System.out.println("""
                                Food Item Created
                                _id | name | quantity | price | created | modified
                                """+foodItem);
                        } catch (CustomException em){
                            System.out.println(em.getMessage());
                        }
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
                                String foodItem = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
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
                                String foodItem = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
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
                    try{
                        List<FoodItem> foodItems = foodItemManager.retrieveAllFoodItems(true, 1, "_id");
                        System.out.println("_id | name | quantity | price | created | modified");
                        foodItems.forEach(System.out::println);
                        scanner.nextLine();
                        System.out.print("Which food-item do you want to remove(Food Item Name): ");
                        String foodItemName = scanner.nextLine();

                        boolean result = foodItemManager.deleteFoodItem(foodItemName);
                        System.out.println(result? "Food Item successfully Removed!" : "No such Food Item");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 6 -> {
                    System.out.println("Cafeteria Admin Operation: Update Food Menu of the Day");
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
                                String foodItemNameFromMenu = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
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
                        try{
                            FoodMenu newFoodMenu = foodMenuManager.createFoodMenu(foodMenuName,
                                    MenuAvailability.validateAndConvertAvailableDaysToList(foodAvailableDays));
                            System.out.println("""
                                Food Item Created
                                _id | name | available_days | created | modified
                                """+newFoodMenu);
                        } catch (CustomException em){
                            System.out.println(em.getMessage());
                        }
                    }
                }
                case 8 -> {
                    System.out.println("Cafeteria Admin Operation: View Food Menu");
                    scanner.nextLine();
                    System.out.print("Food Menu Name: ");
                    String foodMenuName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
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
                                    String foodMenuName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                    foodMenuManager.retrieveFoodMenu(foodMenuName);
                                    System.out.print("Enter a New Food Menu Name: ");
                                    String newFoodMenuName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
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
                                    List<MenuAvailability> availableDays = MenuAvailability.validateAndConvertAvailableDaysToList(newFoodMenuAvailableDays);
                                    FoodMenu updatedMenu = foodMenuManager.updateFoodMenuAvailability(availableDays, foodMenuName);
                                    System.out.println("""
                                                Food Menu Available Day Updated!
                                                _id   |   name   |   available_day""");
                                    System.out.printf("%d   |   %s  |   %s%n",updatedMenu.getId(),updatedMenu.getName(),
                                            MenuAvailability.daysToString(updatedMenu.getAvailableDay()));
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
                        String foodMenuName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
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
                            System.out.println("\nFood Item  |  Quantity  |  Price");
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
                    int orderedFoodItemQuantity = 0;
                    int balanceFoodItem = 0;
                    double totalCost = 0.00;
                    FoodItem foodMenuItem = new FoodItem();
                    try{
                        System.out.println("Select Food Items to Add to Order");
                        List<FoodMenu> foodMenuOfTheDay = foodMenuManager.retrieveFoodMenuOfTheDay();
                        for(FoodMenu menu: foodMenuOfTheDay){
                            System.out.println("Menu : " + menu.getName());
                            System.out.println("\nFood Item  |  Quantity  |  Price");
                            for (FoodItem item: foodMenuManager.retrieveFoodMenuItems(menu.getId())){
                                System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                            }
                        }
                        HashMap<String, Integer> foodItemsQuantityMap = new HashMap<>();
                        boolean isAddingFoodItems = true;
                        while(isAddingFoodItems){
                            System.out.print("Food Item Name: ");
                            String foodItemName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                            System.out.print("Food Item Quantity: ");
                            int foodItemQuantity = scanner.nextInt();
                            orderedFoodItemQuantity = foodItemQuantity;
                            try{
                                FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                for(FoodMenu menu: foodMenuOfTheDay){
                                    foodMenuItem = foodMenuManager.retrieveFoodMenuItem(menu.getId(), foodItem.getId());
                                    if(foodItemQuantity > 0 && foodItemQuantity < foodMenuItem.getQuantity() && foodMenuItem.getQuantity() > 0){
                                        if(foodItemsQuantityMap.containsKey(foodMenuItem.getName())){
                                            System.out.print("You have this food item already in orders list" +
                                                    "\nDo you want to update its quantity?(Yes/No): ");
                                            scanner.nextLine();
                                            String updateFoodItemQuantity = scanner.nextLine();
                                            if (updateFoodItemQuantity.toLowerCase().charAt(0) == 'y') {
                                                foodItemsQuantityMap.put(foodItem.getName(), foodItemsQuantityMap.get(foodItem.getName()) + foodItemQuantity);
                                                totalCost += foodItem.getPrice() * foodItemQuantity;
                                                balanceFoodItem = foodMenuItem.getQuantity() - foodItemQuantity;
                                                foodItemManager.updateFoodItemQuantity(balanceFoodItem,foodMenuItem.getName());
                                                System.out.printf("\nFood Item | Quantity | Total Cost%n%s    %d    %.2f%nFood Item Quantity Updated%n", foodItem.getName()
                                                        ,foodItemsQuantityMap.get(foodItem.getName()), foodItemsQuantityMap.get(foodItem.getName()) * foodMenuItem.getPrice());
                                            }
                                        } else {
                                            foodItemsQuantityMap.put(foodMenuItem.getName(), foodItemQuantity);
                                            totalCost += foodMenuItem.getPrice() * foodItemQuantity;
                                            balanceFoodItem = foodMenuItem.getQuantity() - foodItemQuantity;
                                            foodItemManager.updateFoodItemQuantity(balanceFoodItem,foodMenuItem.getName());
                                            scanner.nextLine();
                                        }
                                    } else {
                                        System.out.println("Not Enough Food Items Left. Please Choose Different Quantity or Food Item!");
                                        scanner.nextLine();
                                    }

                                }
                            } catch (CustomException e){
                                System.out.println(e.getMessage());
                                scanner.nextLine();
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
                        String deliveryDateTime = CosUtil.isDateTimeFormatValid(scanner.nextLine());
                        orderManager.createOrder(foodItemsQuantityMap, totalCost, deliveryLocation, deliveryDateTime);
                        System.out.println("Order Created!");
                    } catch (CustomException e){
                        int restoreFoodItemQuantity = balanceFoodItem + orderedFoodItemQuantity;
                        foodItemManager.updateFoodItemQuantity(restoreFoodItemQuantity, foodMenuItem.getName());
                        System.out.println(e.getMessage());
                    }
                }
                case 16 -> {
                    System.out.println("Customer Operation : View Cart");
                    try{
                        List<Order> cartOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_CART);
                        if(cartOrders.isEmpty()){
                            System.out.println("Your Cart is Empty!");
                        } else {
                            printOrder(cartOrders);
                        }
                    } catch (CustomException | NullPointerException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 17 -> {
                    System.out.println("Customer Operation: Edit Order");
                    System.out.println("Editing Food Items in Cart");
                    try{
                        List<Order> cartOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_CART);
                        if(cartOrders.isEmpty()){
                            System.out.println("Your Cart is Empty!");
                        } else {
                            printOrder(cartOrders);
                            System.out.print("Choose Order From Cart to Edit (Order Id): ");
                            int cartOrderId = scanner.nextInt();
                            boolean isEditingOrder = true;
                            while (isEditingOrder) {
                                System.out.print("""
                                        
                                        1) Edit Food Items
                                        2) Edit Delivery Location
                                        3) Edit Delivery Date Time
                                        4) Exit
                                        
                                        Option :""");
                                int orderEditOption = scanner.nextInt();
                                switch (orderEditOption) {
                                    case 1 -> {
                                        System.out.println("Editing Food Items of Cart Order");
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
                                                    HashMap<String, Integer> cartFoodItems = orderManager.retrieveOrderedFoodItems(cartOrderId);
                                                    scanner.nextLine();
                                                    System.out.println("Select Food Items to Add to Cart Order");
                                                    List<FoodMenu> foodMenuOfTheDay = foodMenuManager.retrieveFoodMenuOfTheDay();
                                                    for (FoodMenu menu : foodMenuOfTheDay) {
                                                        System.out.println("Menu : " + menu.getName());
                                                        System.out.println("\nFood Item  |  Quantity  |  Price");
                                                        for (FoodItem item : foodMenuManager.retrieveFoodMenuItems(menu.getId())) {
                                                            System.out.printf("%s   |   %d  |   %.2f%n", item.getName(), item.getQuantity(), item.getPrice());
                                                        }
                                                    }

                                                    System.out.print("Food Item Name: ");
                                                    String foodItemName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                                    System.out.print("Food Item Quantity: ");
                                                    int foodItemQuantity = scanner.nextInt();
                                                    double totalCost = orderManager.retrieveOrder(cartOrderId).getTotalCost();
                                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                                    for (FoodMenu menu : foodMenuOfTheDay) {
                                                        FoodItem foodMenuItem = foodMenuManager.retrieveFoodMenuItem(menu.getId(), foodItem.getId());
                                                        if(foodItemQuantity > 0 && foodItemQuantity < foodMenuItem.getQuantity() && foodMenuItem.getQuantity() > 0) {
                                                            if (cartFoodItems.containsKey(foodMenuItem.getName())) {
                                                                System.out.print("You have this food item already in orders list" +
                                                                        "\nDo you want to update its quantity?(Yes/No): ");
                                                                scanner.nextLine();
                                                                String updateFoodItemQuantity = scanner.nextLine();
                                                                if (updateFoodItemQuantity.toLowerCase().charAt(0) == 'y') {
                                                                    cartFoodItems.put(foodItem.getName(), cartFoodItems.get(foodItem.getName()) + foodItemQuantity);
                                                                    totalCost += foodItem.getPrice() * foodItemQuantity;
                                                                    foodItemManager.updateFoodItemQuantity(foodMenuItem.getQuantity() - foodItemQuantity,foodMenuItem.getName());
                                                                    System.out.printf("\nFood Item | Quantity | Total Cost%n%s    %d    %.2f%nFood Item Quantity Updated%n", foodItem.getName()
                                                                            ,cartFoodItems.get(foodItem.getName()), cartFoodItems.get(foodItem.getName()) * foodMenuItem.getPrice());
                                                                }
                                                            } else {
                                                                cartFoodItems.put(foodMenuItem.getName(), foodItemQuantity);
                                                                totalCost += foodMenuItem.getPrice() * foodItemQuantity;
                                                                foodItemManager.updateFoodItemQuantity(foodMenuItem.getQuantity() - foodItemQuantity,foodMenuItem.getName());
                                                                System.out.println("Food Item Added to Orders List");
                                                                scanner.nextLine();
                                                            }
                                                            orderManager.updateFoodItemsInCartOrder(cartOrderId, foodItem.getName(), cartFoodItems.get(foodItem.getName()));
                                                            orderManager.updateCartOrderTotalCost(cartOrderId, totalCost);
                                                            System.out.println("Cart Order Updated!");
                                                            printOrder(cartOrders);
                                                        } else {
                                                            System.out.println("Not Enough Food Items Left. Please Choose Different Quantity or Food Item!");
                                                        }
                                                    }
                                                }
                                                case 2 -> {
                                                    HashMap<String, Integer> cartFoodItems = orderManager.retrieveOrderedFoodItems(cartOrderId);
                                                    scanner.nextLine();
                                                    System.out.println("Select Food Items to Remove from Cart Order");
                                                    System.out.print("Food Item Name to Remove from Cart Order: ");
                                                    String foodItemName = CosUtil.capitalizeFirstLetter(scanner.nextLine().toLowerCase());
                                                    System.out.print("Enter food Item Quantity : ");
                                                    int foodItemQuantity = scanner.nextInt();
                                                    FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                                                    if (cartFoodItems.containsKey(foodItem.getName())) {
                                                        if(foodItemQuantity <= 0){
                                                            System.out.println("Please provide valid quantity value");
                                                        } else if(foodItemQuantity == cartFoodItems.get(foodItem.getName())) {
                                                            foodItemManager.updateFoodItemQuantity(
                                                                    foodItem.getQuantity() + cartFoodItems.get(foodItem.getName()),
                                                                    foodItem.getName());
                                                            double removedFoodItemsCost = foodItem.getPrice() * foodItemQuantity;
                                                            orderManager.updateCartOrderTotalCost(cartOrderId,
                                                                    orderManager.retrieveOrder(cartOrderId).getTotalCost() - removedFoodItemsCost);
                                                            orderManager.deleteCartOrderFoodItem(cartOrderId, foodItemName);
                                                            System.out.println("Food Item Removed From this Cart Order");
                                                        } else {
                                                            orderManager.updateCartOrderFoodItemQuantity(cartOrderId, foodItem.getName(),
                                                                    cartFoodItems.get(foodItem.getName())-foodItemQuantity);
                                                            double removedFoodItemsCost = foodItem.getPrice() * foodItemQuantity;
                                                            orderManager.updateCartOrderTotalCost(cartOrderId,
                                                                    orderManager.retrieveOrder(cartOrderId).getTotalCost() - removedFoodItemsCost);
                                                            System.out.println("Food Item Quantity Updated");
                                                        }
                                                        printOrder(cartOrders);
                                                    } else {
                                                        System.out.println("Food Item Not in this Cart Order");
                                                    }
                                                }
                                                case 3 -> isEditingFoodItem = false;
                                            }
                                        }
                                    }
                                    case 2 -> {
                                        try{
                                            System.out.println("Edit Delivery Location");
                                            System.out.print("Enter New Delivery Location: ");
                                            scanner.nextLine();
                                            String newDeliveryLocation = scanner.nextLine();
                                            orderManager.updateDeliveryLocation(cartOrderId, newDeliveryLocation);
                                            System.out.println("Delivery Location Updated");
                                        } catch (CustomException e){
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                    case 3 -> {
                                        try{
                                            System.out.println("Edit New Delivery Date And Time");
                                            System.out.print("Enter Delivery Date And Time(dd-MM-yyyy HH-mm-ss am/pm): ");
                                            scanner.nextLine();
                                            String newDeliveryDateTime = CosUtil.isDateTimeFormatValid(scanner.nextLine());
                                            orderManager.updateDeliveryDateAndTime(cartOrderId, newDeliveryDateTime);
                                            System.out.println("Delivery Date And Time Updated");
                                        } catch (CustomException e){
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                    case 4 -> {
                                        isEditingOrder = false;
                                    }
                                }
                            }
                        }
                    } catch (CustomException  e){
                        System.out.println(e.getMessage());
                    }
                }
                case 18 -> {
                    System.out.println("Customer Operation : Confirm Order");
                    try{
                        List<Order> cartOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_CART);
                        if(cartOrders.isEmpty()){
                            System.out.println("Your Cart is Empty!");
                        } else {
                            printOrder(cartOrders);
                        }
                        System.out.print("Enter Order Id to Confirm Order: ");
                        int orderId = scanner.nextInt();
                        orderManager.confirmOrder(orderId);
                        System.out.println("Order Confirmed");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 19 -> {
                    System.out.println("Customer Operation  : View Confirmed Orders");
                    try{
                        List<Order> confirmedOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_RECEIVED);
                        if(confirmedOrders.isEmpty()){
                            System.out.println("Confirmed Orders is Empty!");
                        } else {
                            printOrder(confirmedOrders);
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 20 -> {
                    System.out.println("Customer Operation : Cancel the Order");
                    try{
                        List<Order> cartOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_RECEIVED);
                        if(cartOrders.isEmpty()){
                            System.out.println("There are no Orders Placed yet!");
                        } else {
                            printOrder(cartOrders);
                        }
                        System.out.print("Enter Order Id to Cancel Order: ");
                        int orderId = scanner.nextInt();
                        orderManager.cancelOrder(orderId);
                        System.out.println("Order Cancelled");
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 21 -> {
                    System.out.println("Customer Operation  : View Cancelled Orders");
                    try{
                        List<Order> cancelledOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_CANCELLED);
                        if(cancelledOrders.isEmpty()){
                            System.out.println("Cancelled Orders is Empty!");
                        } else {
                            printOrder(cancelledOrders);
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 22 -> {
                    try {
                        System.out.println("Cafeteria staff : List Active Orders");
                        List<Order> confirmedOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_PREPARED);
                        printOrder(confirmedOrders);
                    } catch (CustomException e){
                        System.out.println("Active " + e.getMessage());
                    }

                }
                case 23 -> {
                    System.out.println("Cafeteria staff : List Received Orders");
                    try {
                        List<Order> receivedOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_RECEIVED);
                        printOrder(receivedOrders);
                    } catch (CustomException e){
                        System.out.println("Received " + e.getMessage());
                    }

                }
                case 24 -> {
                    System.out.println("Cafeteria staff : List Cancelled Orders");
                    try {
                        List<Order> cancelledOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_CANCELLED);
                        printOrder(cancelledOrders);
                    } catch (CustomException e){
                        System.out.println("Cancelled " + e.getMessage());
                    }

                }
                case 25 -> {
                    System.out.println("Cafeteria staff : List Completed Orders");
                    try {
                        List<Order> completedOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_DELIVERED);
                        printOrder(completedOrders);
                    } catch (CustomException e){
                        System.out.println("Delivered " + e.getMessage());
                    }

                }
                case 26 -> {
                    System.out.println("Cafeteria staff : Prepare Order");
                    try {
                        List<Order> receivedOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_RECEIVED);
                        if(receivedOrders.isEmpty()){
                            System.out.println("Received Orders is Empty!");
                        } else {
                            printOrder(receivedOrders);
                            System.out.print("Select the Order to start preparation(Order id): ");
                            int orderId = scanner.nextInt();
                            boolean result = orderManager.prepareOrder(orderId);
                            System.out.println("""
                            Order Prepared!
                            _id  | total_cost |  order_status  |  created""");
                            orderManager.retrieveAllOrders(OrderStatus.ORDER_PREPARED).stream().filter(order -> order.getId() == orderId).forEach(System.out::println);
                        }

                    } catch (CustomException e){
                        System.out.println(e.getMessage());
                    }

                }
                case 27 -> {
                    System.out.println("Delivery Staff : Deliver Order");
                    try {
                        System.out.println("Cafeteria staff : List Active Orders");
                        List<Order> confirmedOrders = orderManager.retrieveAllOrders(OrderStatus.ORDER_PREPARED);
                        if(confirmedOrders.isEmpty()){
                            System.out.println("Active Orders is Empty!");
                        } else {
                            printOrder(confirmedOrders);
                            System.out.print("Select the Order to Deliver(Order id): ");
                            int orderId = scanner.nextInt();
                            boolean result = deliveryManger.deliverOrder(orderId);
                            System.out.println("""
                            Order Delivered!
                            _id  | total_cost |  order_status  |  created""");
                            orderManager.retrieveAllOrders(OrderStatus.ORDER_DELIVERED).stream().filter(order -> order.getId() == orderId).forEach(System.out::println);
                        }
                    } catch (CustomException e){
                        System.out.println(e.getMessage());
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
                6) Update Food Menu of the Day
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
                16) View Cart
                17) Edit Order
                18) Confirm Order
                19) View Confirmed Orders
                20) Cancel Order
                21) View Cancelled Orders
                
                CAFETERIA STAFF
                22) List Active Orders
                23) List Received Orders
                24) List Cancelled Orders
                25) List Completed Orders
                26) Prepare Order
                
                DELIVERY STAFF
                27) Deliver Order
                Press 0 to exit() Application.
                """;
        System.out.println(cafeteriaOperations);
    }

    private static void openConnections() throws CustomException {
        foodItemDataStore.openConnection();
        foodMenuDataStore.openConnection();
        orderDataStore.openConnection();
    }

    private static void closeConnections() throws CustomException {
        foodItemDataStore.closeConnection();
        foodMenuDataStore.closeConnection();
        orderDataStore.closeConnection();
    }

    private static void printOrder(List<Order> orders) throws CustomException {
        for(Order order: orders){
            System.out.println("\nId   |   Total Cost   |    Order Status    |   Order Created ");
            System.out.println(orderManager.retrieveOrder(order.getId()));
            System.out.println("\nDelivery Location   |   Delivery Date And Time");
            System.out.println(orderManager.retrieveDeliveryDetails(order.getId()));
            HashMap<String, Integer> foodItemsQuantityMap = orderManager.retrieveOrderedFoodItems(order.getId());
            System.out.println("\nFood Item  |  Quantity    |   Total Cost");
            for(String foodItemName: foodItemsQuantityMap.keySet()){
                FoodItem foodItem = foodItemManager.retrieveFoodItem(foodItemName);
                System.out.printf("%s   %d   %.2f%n", foodItem.getName(), foodItemsQuantityMap.get(foodItem.getName()),
                        foodItemsQuantityMap.get(foodItem.getName()) * foodItem.getPrice());

            }
        }
        System.out.println("_____________________________________________________");
    }
}