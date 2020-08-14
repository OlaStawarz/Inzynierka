package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapp.Planner.Planner;
import com.example.myapp.R;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recipes extends AppCompatActivity implements HorizontalRecipeAdapter.OnHorizontalItemClickedListener{

    CardView breakfastCardView, dinnerCardView, supperCardView, snackCardView;
    Intent intent;
    Bundle bundle;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<RecipeModel> recipes;
    HorizontalRecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FloatingActionButton addRecipeButton = findViewById(R.id.addRecipeButton);

        breakfastCardView = findViewById(R.id.breakfast_card_view);
        dinnerCardView = findViewById(R.id.dinner_card_view);
        supperCardView = findViewById(R.id.supper_card_view);
        snackCardView = findViewById(R.id.snacks_card_view);
        recyclerView = findViewById(R.id.horizontal_recycler_view);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");


        LinearLayoutManager layoutManager = new LinearLayoutManager(Recipes.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Query query = databaseReference.limitToLast(5);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipes = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RecipeModel recipeModel = postSnapshot.getValue(RecipeModel.class);
                    assert recipeModel != null;
                    recipeModel.setRecipeKey(postSnapshot.getKey());
                    recipes.add(recipeModel);
                }
                recipeAdapter = new HorizontalRecipeAdapter(Recipes.this, recipes,
                        Recipes.this);
                recyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        intent = new Intent(Recipes.this, DisplayRecipes.class);
        bundle = new Bundle();

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
                bundle.putString("category", "breakfast");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        dinnerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("category", "dinner");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        supperCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("category", "supper");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        snackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("category", "snack");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public void horizontalItemClicked(int position) {
        Toast.makeText(Recipes.this, "Clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Recipes.this, RecipeDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", recipes.get(position).getName());
        bundle.putString("image", recipes.get(position).getImageUrl());
        bundle.putStringArrayList("ingredients", recipes.get(position).getIngredients());
        bundle.putString("description", recipes.get(position).getDescription());
        bundle.putString("link", recipes.get(position).getLink());
        bundle.putString("key", recipes.get(position).getRecipeKey());

        Toast.makeText(Recipes.this, recipes.get(position).getDescription(), Toast.LENGTH_LONG).show();
        intent.putExtras(bundle);
        startActivity(intent);
    }
}