package org.example;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class MenuManagerTest {

    private MenuManager menuManager;
    private Menu mockMenu;
    private Scanner mockScanner;
    private Order mockOrder;

    @BeforeEach
    public void setUp() {
        MenuManager.menu = mockMenu;
        menuManager = new MenuManager();
    }

    @Test
    public void testMenuInitialization() {
        assertNotNull(menuManager.menu, "Menu should be initialized");
        assertEquals(4, menuManager.menu.getAllItems().size(), "Menu should have 4 items");
    }

    @Test
    public void testSpecificItemInitialization() {
        testItemProperties("Pad Thai", "Main Course", "Stir-fry boof noodle", 10);
        testItemProperties("Deluxe Cheese Burger", "Main Course", "Delicious beef burger with cheese, tomato, onion and lettuce", 10);
        testItemProperties("Spring Roll", "Side", "Crispy prawn and pork spring roll", 5);
        testItemProperties("Chips", "Side", "Crispy potato chips", 5);
    }

    private void testItemProperties(String name, String category, String description, int price) {
        Item searchedItem = null;

        // search for the item by name in the menu items
        for (Item item : menuManager.menu.getAllItems()) {
            if (item.getName().equals(name)) {
                searchedItem = item;
                break;
            }
        }

        assertNotNull(searchedItem, name + " should be in the menu");
        assertEquals(name, searchedItem.getName(), "Item name should be '" + name + "'");
        assertEquals(category, searchedItem.getCategory(), "Category should be '" + category + "'");
        assertEquals(description, searchedItem.getDescription(), "Description should match for " + name);
        assertEquals(price, searchedItem.getPrice(), "Price should be " + price + " for " + name);
    }

    @Test
    public void testVerifyLogin_SuccessfulLogin() {
        menuManager.adminAccounts.put("testUser", "testPassword");
        assertTrue(menuManager.verifyLogin("testUser", "testPassword"));
    }
    @Test
    public void testVerifyLogin_IncorrectPassword() {
        menuManager.adminAccounts.put("testUser", "testPassword");
        assertFalse(menuManager.verifyLogin("testUser", "wrongPassword"));
    }
    @Test
    public void testVerifyLogin_UserNotFound() {
        assertFalse(menuManager.verifyLogin("nonExistentUser", "password"));
    }
    @Test
    public void testVerifyLogin_NullUsername() {
        assertFalse(menuManager.verifyLogin(null, "password"));
    }
    @Test
    public void testVerifyLogin_NullPassword() {
        menuManager.adminAccounts.put("nullUser", null);
        assertFalse(menuManager.verifyLogin("nullUser", null));
    }

    @Test
    public void testVerifyLogin_EmptyPassword() {
        menuManager.adminAccounts.put("emptyPasswordUser", "");
        assertFalse(menuManager.verifyLogin("emptyPasswordUser", ""));
    }
    @Test
    public void testOrdersProcessedInitialization() {
        assertEquals(0, menuManager.ordersProcessed, "ordersProcessed should be initialized to zero");
    }

    @Test
    public void testAdminAccountsInitialization() {
        assertNotNull(menuManager.adminAccounts, "adminAccounts map should be initialized");
        assertEquals("adminPassword", menuManager.adminAccounts.get("adminUser"), "admin account should be initialized with 'admin' password");
    }

     @Test
     public void testAddItemToOrder() {
         String input = "1\n2\n";
         System.setIn(new ByteArrayInputStream(input.getBytes()));

         menuManager.addItemToOrder();
         Item item = menuManager.menu.getAllItems().get(0);

         assertTrue(menuManager.getUserOrder().containsKey(item));
         assertEquals(2, menuManager.getUserOrder().get(item));
     }
    @Test
    public void testAddInvalidItemToOrder() {
        String input = "10\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        menuManager.addItemToOrder();
        Item item = menuManager.menu.getAllItems().get(0);

        assertTrue(menuManager.getUserOrder().isEmpty());
    }

    @Test
    public void testViewOrder() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        menuManager.getUserOrder().put(new Item("Test Item", "Test", "Test Description", 10), 1);
        menuManager.viewOrder();

        String expectedOutput = "----------- YOUR ORDER -----------";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    @Test
    public void testViewOrderEmpty() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        menuManager.viewOrder();

        String expectedOutput = "Your order is currently empty.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    public void testFinalizeOrderDelivery() {
        String input = "Test User\n1\n"; // Name and delivery option
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        menuManager.getUserOrder().put(new Item("Test Item", "Test", "Test Description", 10), 1);
        menuManager.finalizeOrder();

        assertTrue(menuManager.getUserOrder().isEmpty());
    }

    @Test
    public void testFinalizeOrderPickup() {
        String input = "Test User\n2\n"; // Name and delivery option
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        menuManager.getUserOrder().put(new Item("Test Item", "Test", "Test Description", 10), 1);
        menuManager.finalizeOrder();

        assertTrue(menuManager.getUserOrder().isEmpty());
    }
    @Test
    public void testFinalizeOrderCancel() {
        String input = "Test User\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        menuManager.getUserOrder().put(new Item("Test Item", "Test", "Test Description", 10), 1);
        menuManager.finalizeOrder();

        assertTrue(menuManager.getUserOrder().isEmpty());
    }

    @Test
    public void testAdjustQuantity() {
        String input = "1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Item item = new Item("Test Item", "Test", "Test Description", 10);
        menuManager.getUserOrder().put(item, 1);
        menuManager.adjustQuantity();

        assertEquals(3, menuManager.getUserOrder().get(item));
    }

    @Test
    public void testRemoveItemFromOrder() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Item item = new Item("Test Item", "Test", "Test Description", 10);
        menuManager.getUserOrder().put(item, 1);
        menuManager.removeItemFromOrder();

        assertFalse(menuManager.getUserOrder().containsKey(item));
    }

    @Test
    public void testGetNumOrders() {
        assertEquals(0, menuManager.getNumOrders()); // Initially 0 orders

        menuManager.ordersProcessed++;
        assertEquals(1, menuManager.getNumOrders());
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

}
