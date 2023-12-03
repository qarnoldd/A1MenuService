package org.example;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminInterface {

    Menu menu;
    MenuManager menuManager;
    List<Item> itemList;

    Scanner scan = new Scanner(System.in);
    int menuInput;
    public AdminInterface(Menu menu, MenuManager menuManager){
        this.menu = menu;
        this.menuManager = menuManager;
        this.itemList = menu.getAllItems();
    }
    public void run(){
        adminMenu();
    }
    public void adminMenu()
    {
        scan = new Scanner(System.in);
        System.out.println("This is the admin dashboard\n");
        while (menuInput != 5) {
            System.out.println("Total orders processed: " + menuManager.getNumOrders());
            System.out.println("=----------------------=##=----------------------=");
            System.out.println("1. Add new item");
            System.out.println("2. Update existing item");
            System.out.println("3. View order history");
            System.out.println("4. Register new Admin");
            System.out.println("5. Return to User Interface");
            System.out.println("=----------------------=##=----------------------=");
            menuInput = Integer.parseInt(scan.nextLine());
            if (menuInput == 1) {
                addItem();
            } else if (menuInput == 2) {
                updateItem();
            } else if (menuInput == 3) {
                viewHistory();
            } else if (menuInput == 4) {
                registerAdmin();
            } else if (menuInput == 5) {
                leaveAdminInterface();
            }
        }
    }

    public void leaveAdminInterface() {
        System.out.println("Returning to User Interface...");
        menuManager.userInterface();
    }
    public void addItem() {
        scan = new Scanner(System.in);
        String name;
        String category;
        String description;
        int price;
        System.out.println("=----------------------=##=----------------------=");
        System.out.println("Enter name:");
        name = scan.nextLine();
        System.out.println("Enter Category:");
        category = scan.nextLine();
        System.out.println("Enter Description:");
        description = scan.nextLine();
        System.out.println("Enter Price:");
        price = Integer.parseInt(scan.nextLine());
        while (price < 0){
            System.out.println("Invalid price.");
            System.out.println("Enter Price: ");
            price = Integer.parseInt(scan.nextLine());
        }
        System.out.println("=----------------------=##=----------------------=");
        itemList.add(new Item(name, category, description, price));
        System.out.println("Item " + name + " added to menu.");
        menuInput = -1;
        menu.setItems(itemList);
    }

    public void updateItem() {
        scan = new Scanner(System.in);
        while(menuInput != itemList.size())
        {
            System.out.println("Select an item to update:");
            printItems();
            menuInput = Integer.parseInt(scan.nextLine());
            if(menuInput != itemList.size()) {
                int itemChosen = menuInput;
                menuInput = -1;
                while (menuInput != 5 && menuInput != 4) {
                    System.out.println("Select a field to update:\n");
                    System.out.println("0. " + itemList.get(itemChosen).getName());
                    System.out.println("1. " + itemList.get(itemChosen).getCategory());
                    System.out.println("2. " + itemList.get(itemChosen).getDescription());
                    System.out.println("3. " + itemList.get(itemChosen).getPrice());
                    System.out.println("4. Delete Item");
                    System.out.println("5. Return");
                    menuInput = Integer.parseInt(scan.nextLine());
                    String newString;
                    if (menuInput == 0) {

                        System.out.println("New name: ");
                        newString = scan.nextLine();
                        itemList.get(itemChosen).setItemName(newString);
                    }
                    else if (menuInput == 1) {

                        System.out.println("New Category: ");
                        newString = scan.nextLine();
                        itemList.get(itemChosen).setCategory(newString);
                    }
                    else if (menuInput == 2) {
                        System.out.println("New Description: ");
                        newString = scan.nextLine();
                        itemList.get(itemChosen).setDescription(newString);
                    }

                    if (menuInput == 3) {
                        System.out.println("New Price: ");
                        newString = scan.nextLine();
                        while (Integer.parseInt(newString) < 0){
                            System.out.println("Invalid price.");
                            System.out.println("New Price: ");
                            newString = scan.nextLine();
                        }
                        itemList.get(itemChosen).setPrice(Integer.parseInt(newString));

                    } else if (menuInput == 4) {
                        System.out.println("Deleting " + itemList.get(itemChosen).getName());
                        itemList.remove(itemChosen);
                    }
                }
            }
        }
        menuInput = -1;
        menu.setItems(itemList);
    }

    public void printItems() {
        for(int i = 0; i < itemList.size(); i++)
        {
            System.out.println(i + ". " + itemList.get(i).getName());
        }
        System.out.println(itemList.size() + ". Return to previous Menu");
    }

    public void viewHistory(){
        if(menuManager.ordersProcessed == 0){
            System.out.println("No orders have been processed.");
            return;
        }
        System.out.println("\n-----------ORDER HISTORY-----------");
        try (BufferedReader buffer = new BufferedReader(new FileReader("history.txt"))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println();
    }

    public void registerAdmin() {
        scan = new Scanner(System.in);
        String newAdminUsername = null;
        String newAdminPassword = null;
        String confirmation = null;

        System.out.println("=----------------------=##=----------------------=");
        System.out.println("Creating new Admin account.");

        while (newAdminUsername == null || newAdminUsername.isEmpty()) {
            System.out.print("Enter Admin username: ");
            newAdminUsername = scan.nextLine().trim();
            if (newAdminUsername == null || newAdminUsername.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
            }
        }

        while (newAdminPassword == null || newAdminPassword.isEmpty()) {
            System.out.print("Enter Admin password: ");
            newAdminPassword = scan.nextLine();
            if (newAdminPassword == null || newAdminPassword.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
            }
        }

        while (newAdminPassword != null && !newAdminPassword.equals(confirmation)) {
            System.out.print("Confirm Admin password: ");
            confirmation = scan.nextLine();
            if (!newAdminPassword.equals(confirmation)) {
                System.out.println("Passwords do not match. Please retry.");
            }
        }

        if (menuManager.adminAccounts.containsKey(newAdminUsername)) {
            System.out.println("Username already exists. Please choose a different username.");
        } else {
            menuManager.adminAccounts.put(newAdminUsername, newAdminPassword);
            System.out.println("Admin " + newAdminUsername + " has been registered.");
        }
        System.out.println("=----------------------=##=----------------------=");
    }
}




