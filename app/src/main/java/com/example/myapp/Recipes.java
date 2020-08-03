package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Recipes extends AppCompatActivity {

    CardView breakfastCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FloatingActionButton addRecipeButton = findViewById(R.id.addRecipeButton);

        breakfastCardView = findViewById(R.id.breakfast_card_view);

        bottomNavigationView.setSelectedItemId(R.id.recipes);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shopping_list:
                        startActivity(new Intent(getApplicationContext(), ShoppingList.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.meal_planner:
                        startActivity(new Intent(getApplicationContext(), Planner.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.recipes:
                        return true;
                }
                return false;
            }

        });

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Recipes.this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Recipes.this, RecipesStep1.class);
                startActivity(intent);
            }
        });

        breakfastCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recipes.this, DisplayRecipesBreakfast.class);
                startActivity(intent);
            }
        });
    }
}