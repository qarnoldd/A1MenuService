package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

public class MenuTest {

    Menu menu;
    Menu shortMenu;
    Item item1;
    Item item2;
    Item item3;
    Item item4;
    InputStream stdin = System.in;
    @AfterEach
    public void finish()
    {
        System.setIn(stdin);
    }
    @BeforeEach
    public void menuInit() {
        menu = new Menu();
        shortMenu = new Menu();
        Item item1 = new Item("Name1", "Category1", "Description1", 1);
        Item item2 = new Item("Name2", "Category2", "Description2", 2);
        Item item3 = new Item("Name3", "Category3", "Description3", 3);
        Item item4 = new Item("Name4", "Category4", "Description4", 4);
        menu.addItem(item1);
        menu.addItem(item2);
        menu.addItem(item3);
        menu.addItem(item4);
        shortMenu.addItem(item1);
        stdin = System.in;
    }
    @Test
    public void testAddItem() {
        assertEquals(menu.getAllItems().size(), 4);
        menu.addItem(new Item("Name5", "Category5", "Description5", 5));
        assertEquals(menu.getAllItems().size(), 5);
    }

    @Test
    public void testGetAllItems() {
        List<Item> ItemList = new ArrayList<>();
        ItemList.add(new Item("Name", "Category", "Description", 5));
        menu.setItems(ItemList);
        assertArrayEquals(ItemList.toArray(), menu.getAllItems().toArray());
    }

    @Test
    public void testSetItems() {
        List<Item> newItemList = new ArrayList<>();
        newItemList.add(new Item("Name", "Category", "Description", 5));
        menu.setItems(newItemList);
        assertArrayEquals(newItemList.toArray(), menu.getAllItems().toArray());

    }

    @Test
    public void displayMenu() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        shortMenu.displayMenu();
        String output = outputStream.toString();
        StringWriter expectedStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(expectedStringWriter);
        printWriter.println("------------------ MENU ------------------");
        printWriter.println("1: Name1 - Category1 - Description1 $1\n");
        printWriter.println("------------------------------------------\n");
        printWriter.close();
        String expected = "------------------ MENU ------------------\n1: Name1 - Category1 - Description1 $1.0\n\n------------------------------------------";

        assertEquals(expected, output.trim());
    }

    @Test
    public void testSetItemList() {
        List<Item> newItemList = Arrays.asList(item1, item2, item3, item4);
        menu.setItemList(newItemList);
        assertArrayEquals(newItemList.toArray(), menu.getAllItems().toArray());
    }
}