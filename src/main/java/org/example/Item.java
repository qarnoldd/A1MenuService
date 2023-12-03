package org.example;

public class Item {

    private String name;
    private String category;
    private String description;
    private double price;
    
    public Item(String name, String category, String description, double price) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
    }

    public String getName(){return name;}
    public void setItemName(String name){this.name = name ;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description ;}
    public double getPrice(){return price;}
    public void setPrice(int price){this.price = price ;}
    public String getCategory(){return category;}
    public void setCategory(String category){this.category = category ;}
}

