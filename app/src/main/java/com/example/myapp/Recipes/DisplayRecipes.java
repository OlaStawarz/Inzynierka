package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayRecipes extends AppCompatActivity implements RecipeAdapter.OnItemClickedListener {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;

    private DatabaseReference databaseReference;
    private List<RecipeModel> recipes;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipes);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        category = bundle.getString("category");
        Toast.makeText(DisplayRecipes.this, category, Toast.LENGTH_LONG).show();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipes = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RecipeModel recipeModel = postSnapshot.getValue(RecipeModel.class);
                    assert recipeModel != null;
                    recipeModel.setRecipeKey(postSnapshot.getKey());
                    switch (category) {
                        case "breakfast":
                            for (int i = 0; i < recipeModel.getCategory().size(); i++) {
                                if (recipeModel.getCategory().get(i).equals("Śniadanie")) {
                                    recipes.add(recipeModel);
                                }
                            }
                            break;
                        case "dinner":
                            for (int i = 0; i < recipeModel.getCategory().size(); i++) {
                                if (recipeModel.getCategory().get(i).equals("Obiad")) {
                                    recipes.add(recipeModel);
                                }
                            }
                            break;
                        case "supper":
                            for (int i = 0; i < recipeModel.getCategory().size(); i++) {
                                if (recipeModel.getCategory().get(i).equals("Kolacja")) {
                                    recipes.add(recipeModel);
                                }
                            }
                            break;
                        case "snack":
                            for (int i = 0; i < recipeModel.getCategory().size(); i++) {
                                if (recipeModel.getCategory().get(i).equals("Przekąski")) {
                                    recipes.add(recipeModel);
                                }
                            }
                            break;
                    }

                }
                recipeAdapter = new RecipeAdapter(DisplayRecipes.this, recipes,
                        DisplayRecipes.this);
                recyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayRecipes.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void itemClicked(int position) {
        Toast.makeText(DisplayRecipes.this, recipes.get(position).getDescription(), Toast.LENGTH_LONG).show();

        // recipes.get(position);
        Intent intent = new Intent(DisplayRecipes.this, RecipeDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", recipes.get(position).getName());
        bundle.putString("image", recipes.get(position).getImageUrl());
        bundle.putStringArrayList("ingredients", recipes.get(position).getIngredients());
        bundle.putString("description", recipes.get(position).getDescription());
        bundle.putString("link", recipes.get(position).getLink());
        bundle.putString("key", recipes.get(position).getRecipeKey());
        Toast.makeText(DisplayRecipes.this, recipes.get(position).getDescription(), Toast.LENGTH_LONG).show();
        // TODO dodać pozostałe elementy
        // Toast.makeText(DisplayRecipes.this, recipes.get(position).getDescription(), Toast.LENGTH_LONG).show();
        intent.putExtras(bundle);
        startActivity(intent);
    }
}