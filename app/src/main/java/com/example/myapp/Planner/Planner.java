package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapp.R;
import com.example.myapp.Recipes.Recipes;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Planner extends AppCompatActivity {

    private Button makePlan, showPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        makePlan = findViewById(R.id.buttonPlan);
        showPlan = findViewById(R.id.buttonSeePlan);

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

        makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Planner.this, AddPlan.class);
                startActivity(intent);
            }
        });

        showPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Planner.this, ShowPlan.class);
                startActivity(intent);
            }
        });
    }
}