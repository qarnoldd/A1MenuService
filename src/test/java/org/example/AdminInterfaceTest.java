package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.HashMap;

class AdminInterfaceTest {
    Menu menu;
    MenuManager menuManager;
    AdminInterface adminInterface;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream stdout = System.out;
    InputStream stdin = System.in;

    @BeforeEach
    void setUp() {
        menuManager = new MenuManager();
        adminInterface = new AdminInterface(new Menu(), new MenuManager());
        System.setOut(new PrintStream(outputStream));
    }

    @BeforeEach
    public void initTest() {
        menu = new Menu();
        menu.addItem(new Item("Name1", "Category1", "Description1", 1));
        menu.addItem(new Item("Name2", "Category2", "Description2", 2));
        menu.addItem(new Item("Name3", "Category3", "Description3", 3));
        menu.addItem(new Item("Name4", "Category4", "Description4", 4));
        menuManager = new MenuManager();
        adminInterface = new AdminInterface(menu, menuManager);
        stdin = System.in;
        adminInterface.menuInput = -1;
    }

    @AfterEach
    public void finish() {
        System.setIn(stdin);
    }

    @Test
    public void testPrint() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        adminInterface.printItems();
        String output = outputStream.toString();
        StringWriter expectedStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(expectedStringWriter);

        printWriter.println("0. Name1");
        printWriter.println("1. Name2");
        printWriter.println("2. Name3");
        printWriter.println("3. Name4");
        printWriter.print("4. Return to previous Menu");
        printWriter.close();

        String expected = expectedStringWriter.toString();
        assertEquals(expected, output.trim());
    }

    @Test
    public void TestAddItem() {
        String input = "NameAdd\nCategoryAdd\nDescriptionAdd\n21\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        adminInterface.addItem();

        assertEquals("NameAdd", menu.getAllItems().get(4).getName());
        assertEquals("DescriptionAdd", menu.getAllItems().get(4).getDescription());
        assertEquals("CategoryAdd", menu.getAllItems().get(4).getCategory());
        assertEquals(Double.parseDouble("21"), menu.getAllItems().get(4).getPrice());
    }

    @Test
    public void testRegisterAdmin() {
        String input = "admin2\npassword2\npassword2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        adminInterface.registerAdmin();
    }

    @Test
    public void TestUpdateItem() {
        String input = "3\n0\nUpdatedItem\n1\nUpdatedCategory\n2\nUpdatedDescription\n3\n999\n5\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        adminInterface.updateItem();

        assertEquals("UpdatedItem", menu.getAllItems().get(3).getName());
        assertEquals("UpdatedCategory", menu.getAllItems().get(3).getCategory());
        assertEquals("UpdatedDescription", menu.getAllItems().get(3).getDescription());
        assertEquals(Double.parseDouble("999"), menu.getAllItems().get(3).getPrice());
    }

    @Test
    public void TestDeleteItem() {
        String input = "3\n0\nUpdatedItem\n4\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        adminInterface.updateItem();

        assertEquals(3, menu.getAllItems().size());
    }

/*    @Test
    public void testAdminMenu() {
        String input = "1\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        adminInterface.adminMenu();

        String output = outputStream.toString();
        StringWriter expectedStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(expectedStringWriter);

        printWriter.println("This is the admin dashboard\n");
        printWriter.println("Total orders processed: 0");
        printWriter.println("=----------------------=##=----------------------=");
        printWriter.println("1. Add new item");
        printWriter.println("2. Update existing item");
        printWriter.println("3. View order history");
        printWriter.println("4. Register new Admin");
        printWriter.println("5. Return to User Interface");
        printWriter.println("=----------------------=##=----------------------=");
        printWriter.print("Returning to User Interface...");
        printWriter.close();

        String expected = expectedStringWriter.toString();
        assertEquals(expected, output.trim());
    }*/
    @Test
    public void testRegisterAdmin_SuccessfulRegistration() {
        String input = "newAdmin\npassword\npassword\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        adminInterface.registerAdmin();

        Map<String, String> adminAccounts = menuManager.getAdminAccounts();
        assertTrue(adminAccounts.containsKey("newAdmin"));
        assertEquals("password", adminAccounts.get("newAdmin"));

        String expectedOutput = "=----------------------=##=----------------------=\n" +
                "Creating new Admin account.\n" +
                "Enter Admin username: Enter Admin password: Confirm Admin password: " +
                "Admin newAdmin has been registered.\n" +
                "=----------------------=##=----------------------=\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

/*   @Test
    public void testRegisterAdmin_PasswordMismatch() {
        String input = "newAdmin\npassword\ndifferentPassword\npassword\n"; // Provide different passwords and matching password
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        adminInterface.registerAdmin();

        Map<String, String> adminAccounts = menuManager.getAdminAccounts();
        assertFalse(adminAccounts.containsKey("newAdmin"));

        String expectedOutput = "=----------------------=##=----------------------=\n" +
                "Creating new Admin account.\n" +
                "Enter Admin username: Enter Admin password: Confirm Admin password: " +
                "Passwords do not match. Please retry: " +
                "=----------------------=##=----------------------=\n";
        assertEquals(expectedOutput, outputStream.toString());
    }
    */
    @Test
    public void testRegisterAdmin_DuplicateUsername() {
        Map<String, String> adminAccounts = new HashMap<>();
        adminAccounts.put("newAdmin", "dummyPassword");
        menuManager.setAdminAccounts(adminAccounts);

        String input = "newAdmin\npassword\npassword\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        adminInterface.registerAdmin();

        Map<String, String> updatedAdminAccounts = menuManager.getAdminAccounts();
        assertEquals(1, updatedAdminAccounts.size());
        assertEquals("dummyPassword", updatedAdminAccounts.get("newAdmin"));

        String expectedOutput = "=----------------------=##=----------------------=\n" +
                "Creating new Admin account.\n" +
                "Enter Admin username: Enter Admin password: Confirm Admin password: Username already exists. Please choose a different username.\n" +
                "=----------------------=##=----------------------=\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

/*    @Test
    public void testRegisterAdmin_NullUsername() {
        String input = "null\npassword\npassword\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        adminInterface.registerAdmin();
        Map<String, String> adminAccounts = menuManager.getAdminAccounts();
        assertFalse(adminAccounts.containsKey("null"));
        String expectedOutput = "=----------------------=##=----------------------=\n" +
                "Creating new Admin account.\n" +
                "Enter Admin username: Username already exists. Please choose a different username.\n" +
                "=----------------------=##=----------------------=\n";
        assertEquals(expectedOutput, outputStream.toString());
    }*/

/*    @Test
    public void testRegisterAdmin_NullPassword() {
        String input = "newAdmin\n\n\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        adminInterface.registerAdmin();
        Map<String, String> adminAccounts = menuManager.getAdminAccounts();
        assertFalse(adminAccounts.containsKey("newAdmin"));
    }*/
}
