package org.example;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest{
    private Order order;
    @Test
    public void testOrder(){
        Map<Item, Integer> itemList = new HashMap<Item, Integer>();
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        itemList.put(item, 2);
        this.order = new Order(itemList, "Iverson", 111);
        assert this.order.orderNum == 111;
        assert this.order.itemList == itemList;
        assert this.order.name == "Iverson";
    }

    @Test
    public void testStore(){
        Map<Item, Integer> itemList = new HashMap<Item, Integer>();
        Item item = new Item("Burger", "Main", "Delicious juicy beef burger", 25.00);
        itemList.put(item, 2);
        this.order = new Order(itemList, "Iverson", 111);
        this.order.store();
    }

    @Test
    public void testReset(){
        try {
            Order.resetHistory();
            String filePath = "history.txt";
            Scanner scan = new Scanner(new File(filePath));
            assertFalse(scan.hasNextLine());
        } catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}