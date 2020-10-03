package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapp.R;
import com.example.myapp.Recipes.Recipes;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Planner extends AppCompatActivity implements DeletePlannerDialog.DeletePlannerListener{

    public Button makePlan, showPlan;
    private DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;
    ArrayList<String> days;

    private FirebaseUser user;
    String uid;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        makePlan = findViewById(R.id.buttonPlan);
        showPlan = findViewById(R.id.buttonSeePlan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid);

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
                        finish();
                        startActivity(new Intent(getApplicationContext(), ShoppingList.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.meal_planner:
                        return true;
                    case R.id.recipes:
                        finish();
                        startActivity(new Intent(getApplicationContext(), Recipes.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }

        });

        makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

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

    public void openDialog() {
        DeletePlannerDialog dialog = new DeletePlannerDialog();
        dialog.show(getSupportFragmentManager(), "planner dialog");
    }

    @Override
    public void confirmChoice() {
        databaseReference.removeValue();
        Intent intent = new Intent(Planner.this, AddPlan.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signout) {
            auth.signOut();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}