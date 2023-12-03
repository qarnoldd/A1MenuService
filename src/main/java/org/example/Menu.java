package org.example;
import java.util.*;

public class Menu {

    private List<Item> itemList;

    public Menu(){
        this.itemList = new ArrayList<>();
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public List<Item> getAllItems() {
        return itemList;
    }
    public void setItems(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void displayMenu() {
        System.out.println("\n------------------ MENU ------------------");

        for (int i = 0; i < itemList.size(); i++) {
            System.out.println((i + 1)
                    + ": " + itemList.get(i).getName()
                    + " - " + itemList.get(i).getCategory()
                    + " - " + itemList.get(i).getDescription()
                    + " $" + itemList.get(i).getPrice()
                    + "\n");
        }
        System.out.println("------------------------------------------");
    }

    public void setItemList(List<Item> newItemList)
    {
        this.itemList = newItemList;
    }
}
