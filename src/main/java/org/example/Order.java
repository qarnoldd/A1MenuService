package org.example;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class Order {
    Map<Item, Integer> itemList;
    String name;
    Integer orderNum;
    private final String filePath = "history.txt";

    public Order(Map<Item, Integer> i, String name, int orderNum)
    {
        itemList = i;
        this.name = name;
        this.orderNum = orderNum;
    }

    public void store(){
        try {
            File file = new File(filePath);

            ArrayList<String> hist = new ArrayList<String>();
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("Date: " + java.time.LocalDateTime.now().getDayOfMonth() + "-" + java.time.LocalDateTime.now().getMonthValue() + "-" + java.time.LocalDateTime.now().getYear() + " " + java.time.LocalDateTime.now().getHour() + ":" + java.time.LocalDateTime.now().getMinute() + ":" + java.time.LocalDateTime.now().getSecond() + "\n");
            bufferedWriter.write("Order Number: " + this.orderNum + "\n");
            bufferedWriter.write("Name: " + this.name + "\n");
            bufferedWriter.write("Items: \n");
            double total = 0;

            for(Item item: itemList.keySet()){
                bufferedWriter.write(item.getName() + " $" + item.getPrice() + " x " + itemList.get(item) + "\n");
                total += (item.getPrice() * itemList.get(item));
            }
            bufferedWriter.write("Total: $" + total + "\n");
            bufferedWriter.write("=------------------------------------------------=\n");
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void resetHistory(){
        try{
            FileWriter writer = new FileWriter("history.txt", false);
            writer.write("");
        } catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
