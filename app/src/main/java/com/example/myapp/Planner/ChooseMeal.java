package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeDetail;
import com.example.myapp.Recipes.RecipeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseMeal extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipePlannerAdapter recipeAdapter;

    private DatabaseReference databaseReference, plannerDatabaseReference;
    private List<RecipeModel> recipes;

    String day, meal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_meal);

        recyclerView = findViewById(R.id.recycler_view_recipes_planner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");
        plannerDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        day = bundle.getString("day");
        meal = bundle.getString("meal");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipes = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RecipeModel recipeModel = postSnapshot.getValue(RecipeModel.class);
                    assert recipeModel != null;
                    recipeModel.setRecipeKey(postSnapshot.getKey());
                    recipes.add(recipeModel);
                }
                recipeAdapter = new RecipePlannerAdapter(ChooseMeal.this, recipes);
                recyclerView.setAdapter(recipeAdapter);

                recipeAdapter.setOnButtonClickListener(new RecipePlannerAdapter.OnButtonClickListener() {
                    @Override
                    public void onItemChoose(int position) {
                        Toast.makeText(ChooseMeal.this, "Choose", Toast.LENGTH_SHORT).show();
                        ChosenMeal chosenMeal = new ChosenMeal(recipes.get(position).getRecipeKey());
                        plannerDatabaseReference.child(day).child(meal).setValue(chosenMeal);
                        finish();

                    }

                    @Override
                    public void onItemShow(int position) {
                        Toast.makeText(ChooseMeal.this, "Details", Toast.LENGTH_SHORT).show();
                        Intent showIntent = new Intent(ChooseMeal.this, RecipeDetail.class);
                        Bundle showBundle = new Bundle();
                        showBundle.putString("name", recipes.get(position).getName());
                        showBundle.putString("image", recipes.get(position).getImageUrl());
                        showBundle.putStringArrayList("ingredients", recipes.get(position).getIngredients());
                        showBundle.putString("description", recipes.get(position).getDescription());
                        showBundle.putString("link", recipes.get(position).getLink());
                        showBundle.putString("key", recipes.get(position).getRecipeKey());
                        Toast.makeText(ChooseMeal.this, recipes.get(position).getDescription(), Toast.LENGTH_LONG).show();
                        showIntent.putExtras(showBundle);
                        startActivity(showIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}