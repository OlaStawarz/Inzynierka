package com.example.myapp.ShoppingList;

import com.google.firebase.database.Exclude;

public class ItemModel {

    private String itemName;
    private double amount;
    private String unit;
    private String itemKey;

    public ItemModel() {

    }

    public ItemModel(String name, double amount, String unit) {
        this.itemName = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Exclude
    public String getItemKey() {
        return itemKey;
    }

    @Exclude
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
}
