package com.example.myapp.Recipes;

import com.example.myapp.ShoppingList.IngredientModel;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class RecipeModel {

    private String name;
    private ArrayList<String> category;
    private ArrayList<String> ingredients;
    private String description;
    private String link;
    private String imageUrl;
    private ArrayList<IngredientModel> ingredientModels;
    private String recipeKey;

    public RecipeModel() {

    }

    public RecipeModel(String name, ArrayList<String> category, ArrayList<String> ingredients,
                       String description, String link, String imageUrl,
                       ArrayList<IngredientModel> ingredientModels) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
        this.ingredientModels = ingredientModels;
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

    public ArrayList<IngredientModel> getIngredientModels() {
        return ingredientModels;
    }

    public void setIngredientModels(ArrayList<IngredientModel> ingredientModels) {
        this.ingredientModels = ingredientModels;
    }

    @Exclude
    public String getRecipeKey() {
        return recipeKey;
    }

    @Exclude
    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }
}
