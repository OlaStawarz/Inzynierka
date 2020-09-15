package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.Recipes.Recipes;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Planner extends AppCompatActivity {

    public Button makePlan, showPlan;
    private DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;
    ArrayList<String> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        makePlan = findViewById(R.id.buttonPlan);
        showPlan = findViewById(R.id.buttonSeePlan);
        databaseReference = FirebaseDatabase.getInstance().getReference("Planner");
       // databaseReference.child("cokolwiek").setValue("cokolwiek");

        //TODO: sprawdzić dlaczego się wywala - NIE MOŻE BYC "cokolwiek"!!!!!
        days = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(Planner.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    if (!dataSnapshot.getKey().equals("cokolwiek"))
                        days.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                databaseReference.removeValue();
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