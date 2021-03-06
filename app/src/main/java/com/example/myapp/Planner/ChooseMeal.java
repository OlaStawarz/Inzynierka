package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeDetail;
import com.example.myapp.Recipes.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ArrayList<RecipeModel> recipes, filteredList;
    private EditText searchRecipeEditText;
    String day, meal;

    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_meal);

        searchRecipeEditText = findViewById(R.id.editTextSearchRecipePlanner);
        recyclerView = findViewById(R.id.recycler_view_recipes_planner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid);
        plannerDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        day = bundle.getString("day");
        meal = bundle.getString("meal");
        Toast.makeText(this, meal, Toast.LENGTH_SHORT).show();

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
                recipeAdapter.filterRecipeListPlanner(filteredList);
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
                    recipes.add(recipeModel);
                }
                recipeAdapter = new RecipePlannerAdapter(ChooseMeal.this, recipes);
                recyclerView.setAdapter(recipeAdapter);

                recipeAdapter.setOnButtonClickListener(new RecipePlannerAdapter.OnButtonClickListener() {
                    @Override
                    public void onItemChoose(int position) {
                        if (!filteredList.isEmpty()) {
                            ChosenMeal chosenMeal = new ChosenMeal(filteredList.get(position).getRecipeKey());
                            plannerDatabaseReference.child(day).child(meal).setValue(chosenMeal);
                        } else {
                            ChosenMeal chosenMeal = new ChosenMeal(recipes.get(position).getRecipeKey());
                            plannerDatabaseReference.child(day).child(meal).setValue(chosenMeal);
                        }
                        finish();

                    }

                    @Override
                    public void onItemShow(int position) {
                        Intent showIntent = new Intent(ChooseMeal.this, RecipeDetail.class);
                        Bundle showBundle = new Bundle();
                        if (filteredList.isEmpty()) {
                            showBundle.putString("key", recipes.get(position).getRecipeKey());
                        } else {
                            showBundle.putString("key", filteredList.get(position).getRecipeKey());
                        }
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