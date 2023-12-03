package org.example;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.PrintWriter;

public class MenuManager {

    public static Menu menu;
    private Map<Item, Integer> userOrder;
    public Map<String, String> adminAccounts;

    public int ordersProcessed;
    int menuInput = -1;
    
    boolean isAdminLoggedIn;
    public MenuManager() {
        menu = new Menu();
        userOrder = new HashMap<>();
        this.adminAccounts = new HashMap<>();
        this.ordersProcessed = 0;
        this.isAdminLoggedIn = false;
        // items in menu
        menu.addItem(new Item("Pad Thai", "Main Course", "Stir-fry boof noodle", 10));
        menu.addItem(new Item("Deluxe Cheese Burger", "Main Course", "Delicious beef burger with cheese, tomato, onion and lettuce", 10));
        menu.addItem(new Item("Spring Roll", "Side", "Crispy prawn and pork spring roll", 5));
        menu.addItem(new Item("Chips", "Side", "Crispy potato chips", 5));
        adminAccounts.put("adminUser", "adminPassword");
    }

    public void run(){
        System.out.println("\nWelcome to our store!");
        userInterface();
    }

    public void userInterface() {

        Scanner scan = new Scanner(System.in);

        while (menuInput != 3) {
            System.out.println("1. View Menu");
            System.out.println("2. Add Item to Order");
            System.out.println("3. Exit");

            if (isAdminLoggedIn) {
                System.out.println("4. Go to Admin Interface");
                System.out.println("5. Log Out from Admin");
            } else {
                System.out.println("4. Admin Login");
            }

            if (!userOrder.isEmpty()) {
                System.out.println("6. View Order");
                System.out.println("7. Finalize Order");
                System.out.println("8. Adjust Quantity");
                System.out.println("9. Remove Item from Order");
            }

            menuInput = scan.nextInt();
            scan.nextLine();

            switch (menuInput) {
                case 1:
                    menu.displayMenu();
                    break;
                case 2:
                    addItemToOrder();
                    break;
                case 3:
                    System.out.println("Cancelling order.");
                    userInterface();
                    break;
                case 4:
                    if (isAdminLoggedIn) {
                        adminInterface();
                        menuInput = -1;
                    } else {
                        int loggedIn = 0;
                        while (loggedIn == 0) {
                            System.out.print("Please enter your username: ");
                            String username = scan.nextLine();
                            System.out.print("Please enter your password: ");
                            String password = scan.nextLine();
                            if (verifyLogin(username, password)) {
                                System.out.println("\nWelcome " + username + "!");

                                loggedIn = 1;
                                isAdminLoggedIn = true;
                            } else {
                                System.out.println("Incorrect username/password combination. Please retry.\n");
                            }
                        }
                    }
                    break;
                case 5:
                    if (isAdminLoggedIn) {
                        isAdminLoggedIn = false;
                        System.out.println("Logged out from admin.");
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 6:
                    if (!userOrder.isEmpty()) {
                        viewOrder();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 7:
                    if (!userOrder.isEmpty()) {
                        finalizeOrder();
                        System.out.println("Would you like to place another order? [Y/N]");
                        String answer = scan.nextLine();
                        System.out.println(answer);
                        if (answer.toLowerCase().equals("y")) {
                            System.out.println("Starting a new order");
                        } else if (answer.toLowerCase().equals("n")) {
                            System.out.println("Thank you for shopping with us!\n");
                            run();
                        }
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 8:
                    if (!userOrder.isEmpty()) {
                        adjustQuantity();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 9:
                    if (!userOrder.isEmpty()) {
                        removeItemFromOrder();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }


    public void adminInterface(){
        AdminInterface adminInterface = new AdminInterface(menu, this);
        adminInterface.run();
    }

    public boolean verifyLogin(String providedUserNm, String providedPwd) {
        if (providedUserNm == null || providedPwd == null || providedUserNm.isEmpty() || providedPwd.isEmpty()) {
            return false;
        }
        if (adminAccounts.containsKey(providedUserNm)) {
            if (adminAccounts.get(providedUserNm).equals(providedPwd)) {
                return true;
            }
        }
        return false;
    }

    public void addItemToOrder() {
        Scanner scan = new Scanner(System.in);

        System.out.println("\n---------Select an item number to add to your order:---------");
        menu.displayMenu();
        int itemNumber = scan.nextInt();

        if(itemNumber > 0 && itemNumber <= menu.getAllItems().size()) {
            Item selectedItem = menu.getAllItems().get(itemNumber - 1);
            userOrder.put(selectedItem, userOrder.getOrDefault(selectedItem, 0));
            System.out.println("Enter the quantity for " + selectedItem.getName() + ":");
            int quantity = scan.nextInt();

            userOrder.put(selectedItem, userOrder.getOrDefault(selectedItem, 0) + quantity);
            System.out.println("\n" + quantity + " x " + selectedItem.getName() + " added to your order.");
            System.out.println("------------------------------------------");

        } else {
            System.out.println("Invalid item number.");
        }
    }

    public void viewOrder() {
        if (userOrder.isEmpty()) {
            System.out.println("Your order is currently empty.");
            return;
        }

        System.out.println("\n----------- YOUR ORDER -----------");
        double total = 0;
        int itemNo = 1;
        for (Map.Entry<Item, Integer> entry : userOrder.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(itemNo
            + ". " + item.getName()
            + " - " + item.getCategory() 
            + " - " + item.getDescription() 
            + " $" + item.getPrice()
            + " x " + quantity);
            total += item.getPrice() * quantity;
            itemNo++;
        }

        System.out.println("\nTotal price: $" + total + "\n");
        System.out.println("------------------------------------");

    }

    public void finalizeOrder() {
        if (!userOrder.isEmpty()) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Please enter your name.");
            String name = scan.nextLine();
            Order order = new Order(userOrder, name, ordersProcessed);

            System.out.println("\n---------Choose an option:---------");
            System.out.println("1. Delivery");
            System.out.println("2. Pickup");
            System.out.println("3. Cancel");
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("\nYour order will be delivered to your address.\n");
                    order.store();
                    userOrder.clear();
                    ordersProcessed++;
                    break;
                case 2:
                    System.out.println("\nYou can pick up your order at our restaurant location.\n");
                    order.store();
                    userOrder.clear();
                    ordersProcessed++;
                    break;
                case 3:
                    System.out.println("\nOrder cancelled.\n");
                    userOrder.clear();
                    ordersProcessed++;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void adjustQuantity() {
        Scanner scan = new Scanner(System.in);
        viewOrder();
        
        System.out.println("Enter the item number you want to adjust:");
        int itemNumber = scan.nextInt();
        Item selectedItem = new ArrayList<>(userOrder.keySet()).get(itemNumber - 1);

        System.out.println("Enter the new quantity for " + selectedItem.getName() + ":");
        int newQuantity = scan.nextInt();

        if(newQuantity <= 0) {
            userOrder.remove(selectedItem);
        } else {
            userOrder.put(selectedItem, newQuantity);
        }
    }

    public void removeItemFromOrder() {
        Scanner scan = new Scanner(System.in);
        viewOrder();
        
        System.out.println("Enter the item number you want to remove:");
        int itemNumber = scan.nextInt();
        Item selectedItem = new ArrayList<>(userOrder.keySet()).get(itemNumber - 1);

        userOrder.remove(selectedItem);
        System.out.println(selectedItem.getName() + " has been removed from your order.");
    }

    public int getNumOrders(){
        return this.ordersProcessed;
    }

    public Map<Item, Integer> getUserOrder() {
        return userOrder;
    }


    public Map<String, String> getAdminAccounts() {
        return adminAccounts;
    }

    public void setAdminAccounts(Map<String, String> adminAccounts) {
        this.adminAccounts = adminAccounts;
    }
}
