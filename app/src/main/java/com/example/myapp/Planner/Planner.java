package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapp.R;
import com.example.myapp.Recipes.Recipes;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Planner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.meal_planner);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shopping_list:
                        startActivity(new Intent(getApplicationContext(), ShoppingList.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.meal_planner:
                        return true;
                    case R.id.recipes:
                        startActivity(new Intent(getApplicationContext(), Recipes.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }

        });
    }
}