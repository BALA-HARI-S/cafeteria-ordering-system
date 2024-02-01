package net.breezeware;

import net.breezeware.entity.AvailableDay;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.Order;
import net.breezeware.service.api.*;
import net.breezeware.service.impl.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    private static final FoodItemManager foodItemManager = new FoodItemManagerImplementation();
    private static final FoodMenuManager foodMenuManager = new FoodMenuManagerImplementation();
    private  static final PlaceOrderManager placeOrderManager = new PlaceOrderManagerImplementation();
    private static final DeliveryManager deliveryManger = new DeliveryManagerImplementation();
    private static final OrderManager orderManager = new OrderManagerImplementation();
    private static Order createdOrder;
    private static Order confirmedOrder;
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
                            name   |   quantity   |   price""");
                    for (FoodItem item: foodMenuManager.getFoodMenuItems(foodMenu.getId())){
                        System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
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
                case 13 -> {
                    System.out.println("Customer Operation : View Food Menu of the Day");
                    List<FoodMenu> foodMenuOfTheDay = placeOrderManager.viewFoodMenu();
                    for(FoodMenu menu: foodMenuOfTheDay){
                        System.out.println("Menu : " + menu.getName());
                        System.out.println("Food Item  |  Quantity  |  Price");
                        for (FoodItem item: foodMenuManager.getFoodMenuItems(menu.getId())){
                            System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                        }
                    }
                }
                case 14 -> {
                    System.out.println("Customer Operation : Create Order");
                    System.out.print("Provide Order Details \nEnter Your Username: ");
                    scanner.nextLine();
                    String customerName = scanner.nextLine();
                    System.out.print("Enter Delivery Location: ");
                    String deliveryLocation = scanner.nextLine();
                    System.out.print("Enter Delivery Date And Time(dd-MM-yyyy HH-mm-ss am/pm): ");
                    String deliveryDateTime = scanner.nextLine();
                    if (!isDateTimeFormatValid(deliveryDateTime, "dd-MM-yyyy hh-mm-ss a")) {
                        System.out.println("Input date-time does not match the pattern.");
                        return;
                    }
                    System.out.println("Select Food Items");
                    List<FoodMenu> foodMenuOfTheDay = placeOrderManager.viewFoodMenu();
                    for(FoodMenu menu: foodMenuOfTheDay){
                        System.out.println("Menu : " + menu.getName());
                        System.out.println("Food Item  |  Quantity  |  Price");
                        for (FoodItem item: foodMenuManager.getFoodMenuItems(menu.getId())){
                            System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                        }
                    }
                    System.out.print("Food Item name(item1,item2,...): ");
                    String[] selectedFoodItems = scanner.nextLine().split(",");
                    List<String> foodItems = new ArrayList<>();
                    for (String foodItemName: selectedFoodItems){
                        foodItems.add(foodItemManager.getFoodItem(foodItemName).getName());
                    }
                    double totalCost = 0.00;
                    for(String foodItem: selectedFoodItems){
                        totalCost += foodItemManager.getFoodItem(foodItem).getPrice();
                    }
                    createdOrder = placeOrderManager.createOrder(customerName,foodItems,deliveryLocation,deliveryDateTime,totalCost);
                    System.out.println(Objects.isNull(createdOrder)? "Something went wrong! Cannot Create Order" : "Order Created!");
                }
                case 15 -> {
                    System.out.println("Customer Operation : View Order");
                    if(Objects.isNull(createdOrder)){
                        System.out.println("Your Order is Empty!");
                    } else {
                        System.out.printf("Customer name: %s %nDelivery Location: %s " +
                                "%nDelivery Date And Time: %s %nTotal Cost: %.2f%n", createdOrder.getCustomerName(),
                                createdOrder.getDeliveryLocation(), formatDateTimeInstant(createdOrder.getDeliveryDateTime()), createdOrder.getTotalCost());
                        System.out.println("Selected Food Items");
                        System.out.println("Food Item  |  Quantity  |  Price");
                        for (String foodItemName: createdOrder.getOrderedFoodItems()){
                            FoodItem foodItem = foodItemManager.getFoodItem(foodItemName);
                            System.out.printf("%s  |   %d  |   %.2f%n",foodItem.getName(),foodItem.getQuantity(),foodItem.getPrice());
                        }
                    }
                }
                case 16 -> {
                    System.out.println("Customer Operation: Edit Order");
                    boolean isEditingOrder = true;
                    while (isEditingOrder){
                        System.out.print("""
                            1) Edit Food Items
                            2) Edit Delivery Location
                            3) Edit Delivery Date Time
                            4) Exit
                            Option :""");
                        int orderEditOptions = scanner.nextInt();
                        switch (orderEditOptions){
                            case 1 -> {
                                System.out.println("Select New Food Items");
                                List<FoodMenu> foodMenuOfTheDay = placeOrderManager.viewFoodMenu();
                                for(FoodMenu menu: foodMenuOfTheDay){
                                    System.out.println("Menu : " + menu.getName());
                                    System.out.println("Food Item  |  Quantity  |  Price");
                                    for (FoodItem item: foodMenuManager.getFoodMenuItems(menu.getId())){
                                        System.out.printf("%s   |   %d  |   %.2f%n",item.getName(),item.getQuantity(),item.getPrice());
                                    }
                                }
                                scanner.nextLine();
                                System.out.print("Food Item name(item1,item2,...): ");
                                String[] selectedFoodItems = scanner.nextLine().split(",");
                                List<String> foodItems = new ArrayList<>();
                                for (String foodItemName: selectedFoodItems){
                                    foodItems.add(foodItemManager.getFoodItem(foodItemName).getName());
                                }
                                placeOrderManager.editFoodItemsInOrder(createdOrder, foodItems);
                                System.out.println("Food Items Updated");
                                System.out.printf("Customer name: %s %nDelivery Location: %s " +
                                                "%nDelivery Date And Time: %s %nTotal Cost: %.2f%n", createdOrder.getCustomerName(),
                                        createdOrder.getDeliveryLocation(),formatDateTimeInstant(createdOrder.getDeliveryDateTime()), createdOrder.getTotalCost());
                                System.out.println("Selected Food Items");
                                System.out.println("Food Item  |  Quantity  |  Price");
                                for (String foodItemName: createdOrder.getOrderedFoodItems()){
                                    FoodItem foodItem = foodItemManager.getFoodItem(foodItemName);
                                    System.out.printf("%s  |   %d  |   %.2f%n",foodItem.getName(),foodItem.getQuantity(),foodItem.getPrice());
                                }
                            }
                            case 2 -> {
                                System.out.print("Enter New Delivery Location: ");
                                String newDeliveryLocation = scanner.nextLine();
                                placeOrderManager.editDeliveryLocation(createdOrder, newDeliveryLocation);
                                System.out.println("Order Delivery Location Updated");
                                System.out.printf("Customer name: %s %nDelivery Location: %s " +
                                                "%nDelivery Date And Time: %s %nTotal Cost: %.2f%n", createdOrder.getCustomerName(),
                                        createdOrder.getDeliveryLocation(),formatDateTimeInstant(createdOrder.getDeliveryDateTime()), createdOrder.getTotalCost());
                            }
                            case 3 -> {
                                System.out.print("Enter New Delivery Date And Time(dd-MM-yyyy HH-mm-ss am/pm): ");
                                String newDeliveryDateTime = scanner.nextLine();
                                if (!isDateTimeFormatValid(newDeliveryDateTime, "dd-MM-yyyy hh-mm-ss a")) {
                                    System.out.println("Input date-time does not match the pattern.");
                                    return;
                                }
                                placeOrderManager.editDeliveryDateAndTime(createdOrder, newDeliveryDateTime);
                                System.out.println("Order Delivery Date And Time Updated");
                                System.out.printf("Customer name: %s %nDelivery Location: %s " +
                                                "%nDelivery Date And Time: %s %nTotal Cost: %.2f%n", createdOrder.getCustomerName(),
                                        createdOrder.getDeliveryLocation(),formatDateTimeInstant(createdOrder.getDeliveryDateTime()), createdOrder.getTotalCost());
                            }
                            case 4 -> {
                                isEditingOrder = false;
                            }
                        }
                    }
                }
                case 17 -> {
                    System.out.println("Customer Operation : Confirm Order");
                    if(Objects.isNull(createdOrder)){
                        System.out.println("Your Order is Empty!");
                    } else {
                        System.out.printf("Customer name: %s %nDelivery Location: %s " +
                                        "%nDelivery Date And Time: %s %nTotal Cost: %.2f%n", createdOrder.getCustomerName(),
                                createdOrder.getDeliveryLocation(),formatDateTimeInstant(createdOrder.getDeliveryDateTime()), createdOrder.getTotalCost());
                        System.out.println("Selected Food Items");
                        System.out.println("Food Item  |  Quantity  |  Price");
                        for (String foodItemName: createdOrder.getOrderedFoodItems()){
                            FoodItem foodItem = foodItemManager.getFoodItem(foodItemName);
                            System.out.printf("%s  |   %d  |   %.2f%n",foodItem.getName(),foodItem.getQuantity(),foodItem.getPrice());
                        }
                    }
                    System.out.print("Confirm this Order?(Yes/No): ");
                    scanner.nextLine();
                    boolean isOrderConfirmed = scanner.nextLine().toUpperCase().charAt(0) == 'Y';
                    if(isOrderConfirmed){
                        confirmedOrder = placeOrderManager.confirmOrder(createdOrder);
                        if (!Objects.isNull(confirmedOrder)){
                            System.out.println("Order Received!\n"+
                                            "_id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created\n"
                                            +confirmedOrder);
                            System.out.println("Ordered Food Items");
                            System.out.println("Food Item  |  Quantity  |  Price");
                            for (String foodItemName: placeOrderManager.getOrder(confirmedOrder.getId()).getOrderedFoodItems()){
                                FoodItem foodItem = foodItemManager.getFoodItem(foodItemName);
                                System.out.printf("%s  |   %d  |   %.2f%n",foodItem.getName(),foodItem.getQuantity(),foodItem.getPrice());
                            }

                        } else {
                            System.out.println("Something wrong, Couldn't Place this Order,Retry Again!");
                        }
                    } else {
                        System.out.println("Aborting Order Confirmation!");
                    }
                }
                case 18 -> {
                    System.out.println("Customer Operation : Cancel the Order");
                    if(Objects.isNull(confirmedOrder)){
                        System.out.print("Order is Empty!\n Want to Cancel Order by it's Id(Yes/No): ");
                        scanner.nextLine();
                        boolean isCancelByOrderId =  scanner.nextLine().toUpperCase().charAt(0) == 'Y';
                        if(isCancelByOrderId){
                            System.out.print("Order Id: ");
                            int orderId = scanner.nextInt();
                            boolean cancelledOrder = placeOrderManager.cancelOrder(orderId);
                            System.out.println(cancelledOrder? "Order Cancelled!" : "Cannot cancel order!");
                            System.out.println("Details of cancelled order!\n"+
                                    "_id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created\n"
                                    +cancelledOrder);
                            return;
                        }
                    } else {
                        boolean cancelledOrder = placeOrderManager.cancelOrder(confirmedOrder.getId());
                        System.out.println(cancelledOrder? "Order Cancelled!" : "Cannot cancel order!");
                        System.out.println("Order Cancelled!\n"+
                                "_id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created\n"
                                +confirmedOrder);
                    }
                }
                case 19 -> {
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
                case 20 -> {
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
                case 21 -> {
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
                case 22 -> {
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
                case 23 -> {
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
                case 24 -> {
                    System.out.println("Delivery Staff : Deliver Order");
                    List<Order> receivedOrders = orderManager.getActiveOrders();
                    if(!Objects.isNull(receivedOrders)){
                        System.out.println("""
                                Active Orders!
                                _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                                """);
                        for(Order order: receivedOrders){
                            System.out.println(order);
                        }
                    } else {
                        System.out.println("Don't have any Received Orders");
                    }
                    System.out.print("Select the Order to Deliver(Order id: ");
                    int orderId = scanner.nextInt();
                    boolean result = orderManager.prepareOrder(orderId);
                    System.out.println("""
                            Order Prepared!
                            _id | customer_name | delivery_location | delivery_date_time  | total_cost |  order_status  |  created
                            """);
                    orderManager.getActiveOrders().stream().filter(order -> order.getId() == orderId).forEach(System.out::println);
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
                
                CUSTOMER : PLACE ORDER
                13) View Food Menu
                14) Create Order
                15) View Order
                16) Edit Order
                17) Confirm Order
                18) Cancel Order
                
                CAFETERIA STAFF
                19) List Active Orders
                20) List Received Orders
                21) List Cancelled Orders
                22) List Completed Orders
                23) Prepare Order
                
                DELIVERY STAFF
                24) Deliver Order
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

    private static boolean isDateTimeFormatValid(String inputDateTime, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime.parse(inputDateTime, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private  static String formatDateTimeInstant(Instant instant){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return  localDateTime.format(formatter);
    }
}