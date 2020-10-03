package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayRecipes extends AppCompatActivity implements RecipeAdapter.OnItemClickedListener,
        RecipeAdapter.OnFavouriteButtonClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private EditText searchRecipeEditText;
    private DatabaseReference databaseReference;
    private ArrayList<RecipeModel> recipes, filteredList;
    private ImageView imageViewFavourite;
    String category;
    boolean isFavourite = false;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipes);

        searchRecipeEditText = findViewById(R.id.editTextSearchRecipe);
        recyclerView = findViewById(R.id.recycler_view);
        imageViewFavourite = findViewById(R.id.imageViewAddToFavourite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        category = bundle.getString("category");
        //Toast.makeText(DisplayRecipes.this, category, Toast.LENGTH_LONG).show();


        filteredList = new ArrayList<>();

        searchRecipeEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
        searchRecipeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                filteredList = new ArrayList<>();
                for (RecipeModel recipe : recipes) {
                    if (recipe.getName().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(recipe);
                    }
                }
                recipeAdapter.filterRecipeList(filteredList);
            }
        });

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
                                if (recipeModel.getCategory().get(i).equals("Inne")) {
                                    recipes.add(recipeModel);
                                }
                            }
                            break;
                        case "all":
                            for (int i = 0; i < recipeModel.getCategory().size(); i++) {
                                recipes.add(recipeModel);
                            }
                    }

                }
                recipeAdapter = new RecipeAdapter(DisplayRecipes.this, recipes,
                        DisplayRecipes.this, DisplayRecipes.this);
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
        //Toast.makeText(DisplayRecipes.this, recipes.get(position).getDescription(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(DisplayRecipes.this, RecipeDetail.class);
        Bundle bundle = new Bundle();
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "pusty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, recipes.get(position).getName(), Toast.LENGTH_SHORT).show();
            bundle.putString("key", recipes.get(position).getRecipeKey());

        } else {
            Toast.makeText(this, "niepusty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, filteredList.get(position).getName(), Toast.LENGTH_SHORT).show();
            bundle.putString("key", filteredList.get(position).getRecipeKey());
        }
        intent.putExtras(bundle);
        startActivity(intent);

        //Toast.makeText(this, recipes.get(position).getName(), Toast.LENGTH_SHORT).show();

        //Toast.makeText(this, searchRecipeEditText.getText().toString(), Toast.LENGTH_SHORT).show();
        /*if (searchRecipeEditText.toString().trim().length() > 0) {
            Toast.makeText(this, "niepusty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, String.valueOf(searchRecipeEditText.toString().trim().length()), Toast.LENGTH_SHORT).show();
            *//*
            bundle.putString("key", filteredList.get(position).getRecipeKey());
            intent.putExtras(bundle);
            startActivity(intent);*//*
        } else {
            Toast.makeText(this, "pusty", Toast.LENGTH_SHORT).show();
        }*/

        /*
        bundle.putString("key", recipes.get(position).getRecipeKey());
        intent.putExtras(bundle);
        startActivity(intent);*/

        /*if (!searchRecipeEditText.toString().isEmpty()) {
            bundle.putString("key", filteredList.get(position).getRecipeKey());
        } else {
            bundle.putString("key", recipes.get(position).getRecipeKey());
        }*/
       /* bundle.putString("key", recipes.get(position).getRecipeKey());
        intent.putExtras(bundle);
        startActivity(intent);*/
        // recipes.get(position);
        /*Intent intent = new Intent(DisplayRecipes.this, RecipeDetail.class);
        Bundle bundle = new Bundle();*//*
        bundle.putString("name", recipes.get(position).getName());
        bundle.putString("image", recipes.get(position).getImageUrl());
        bundle.putStringArrayList("ingredients", recipes.get(position).getIngredients());
        bundle.putString("description", recipes.get(position).getDescription());
        bundle.putString("link", recipes.get(position).getLink());*//*
        bundle.putString("key", recipes.get(position).getRecipeKey());
        intent.putExtras(bundle);
        startActivity(intent);*/
    }

    @Override
    public void addToFavourite(int position) {
        Toast.makeText(this, "do ulubionych", Toast.LENGTH_SHORT).show();
        /*if (!isFavourite) {
            imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
            isFavourite = true;
        } else {
            imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_add));
            isFavourite = false;
        }*/



    }


}

/*
package com.example.myapp.Recipes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
public class DisplayRecipes extends AppCompatActivity implements RecipeAdapter.OnItemClickedListener,
        RecipeAdapter.OnFavouriteButtonClickListener {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private EditText searchRecipeEditText;
    private DatabaseReference databaseReference;
    private ArrayList<RecipeModel> recipes, filteredList;
    private ImageView imageViewFavourite;
    String category;
    boolean isFavourite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipes);
        searchRecipeEditText = findViewById(R.id.editTextSearchRecipe);
        recyclerView = findViewById(R.id.recycler_view);
        imageViewFavourite = findViewById(R.id.imageViewAddToFavourite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        category = bundle.getString("category");
        //Toast.makeText(DisplayRecipes.this, category, Toast.LENGTH_LONG).show();
        searchRecipeEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
        searchRecipeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                filteredList = new ArrayList<>();
                for (RecipeModel recipe : recipes) {
                    if (recipe.getName().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(recipe);
                    }
                }
                recipeAdapter.filterRecipeList(filteredList);
            }
        });
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
                        case "all":
                            for (int i = 0; i < recipeModel.getCategory().size(); i++) {
                                recipes.add(recipeModel);
                            }
                    }
                }
                recipeAdapter = new RecipeAdapter(DisplayRecipes.this, recipes,
                        DisplayRecipes.this, DisplayRecipes.this);
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
        Intent intent = new Intent(DisplayRecipes.this, RecipeDetail.class);
        Bundle bundle = new Bundle();
        if (searchRecipeEditText.toString().isEmpty()) {
            bundle.putString("key", recipes.get(position).getRecipeKey());
        } else {
            bundle.putString("key", filteredList.get(position).getRecipeKey());
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public void addToFavourite(int position) {
        Toast.makeText(this, "do ulubionych", Toast.LENGTH_SHORT).show();
        */
/*if (!isFavourite) {
            imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
            isFavourite = true;
        } else {
            imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_add));
            isFavourite = false;
        }*//*
    }
}*/