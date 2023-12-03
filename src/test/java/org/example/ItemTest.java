package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {


    @Test
    public void getName() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        assertEquals(item.getName(), "Burger");
    }

    @Test
    public void setItemName() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        item.setItemName("Cheeseburger");
        assertEquals(item.getName(), "Cheeseburger");
    }

    @Test
    public void getDescription() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        assertEquals(item.getDescription(), "Delicious juicy beef burger");
    }

    @Test
    public void setDescription() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        item.setDescription("Succulent Chicken Burger");
        assertEquals(item.getDescription(), "Succulent Chicken Burger");
    }

    @Test
    public void getPrice() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        assertEquals(item.getPrice(), 25);
    }

    @Test
    public void setPrice() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        item.setPrice(100);
        assertEquals(item.getPrice(), 100);
    }

    @Test
    public void getCategory() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        assertEquals(item.getCategory(), "Main");
    }

    @Test
    public void setCategory() {
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        item.setCategory("Entree");
        assertEquals(item.getCategory(), "Entree");
    }
}