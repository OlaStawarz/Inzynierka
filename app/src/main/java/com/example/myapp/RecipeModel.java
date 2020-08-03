package com.example.myapp;

import java.util.ArrayList;

public class RecipeModel {

    private String name;
    private ArrayList<String> category;
    private ArrayList<String> ingredients;
    private String description;
    private String link;
    private String imageUrl;

    public RecipeModel() {

    }

    public RecipeModel(String name, ArrayList<String> category, ArrayList<String> ingredients, String description, String link, String imageUrl) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
