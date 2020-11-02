package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeDetail;
import com.example.myapp.ShoppingList.IngredientAdapter;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetailPlanner extends AppCompatActivity implements IngredientAdapter.ItemClickedListener{

    private DatabaseReference databaseReference, databaseReferencePlanner, databaseReferenceIngredients;
    private String key;
    IngredientAdapter arrayAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> days = new ArrayList<>();
    ArrayList<IngredientModel> ingredients;
    boolean isInPlanner = false;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        final TextView textViewName = findViewById(R.id.textViewRecipeNameDetail);
        final TextView textViewDescription = findViewById(R.id.textViewDisplayDescription);
        final TextView textViewLink = findViewById(R.id.textViewDisplayLink);
        textViewLink.setMovementMethod(LinkMovementMethod.getInstance());
        final ImageView imageView = findViewById(R.id.imageViewRecipeImageDetail);
        recyclerView = findViewById(R.id.recycler_view_ingredients);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        key = bundle.getString("key");

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
        databaseReferencePlanner = FirebaseDatabase.getInstance().getReference("Planner").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    textViewName.setText(snapshot.child("name").getValue().toString());
                    textViewDescription.setText(snapshot.child("description").getValue().toString());
                    textViewLink.setText(snapshot.child("link").getValue().toString());
                    Picasso.with(RecipeDetailPlanner.this)
                            .load(snapshot.child("imageUrl").getValue().toString())
                            .centerCrop()
                            .fit()
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceIngredients = FirebaseDatabase.getInstance().getReference("Recipes").child(uid)
                .child(key).child("ingredientModels");

        databaseReferenceIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ingredients = new ArrayList<>();
                    for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                        final String name = snapshot.child(String.valueOf(i))
                                .child("name").getValue().toString();
                        final double amount = Double.parseDouble(snapshot.child(String.valueOf(i))
                                .child("amount").getValue().toString());
                        final String unit = snapshot.child(String.valueOf(i))
                                .child("unit").getValue().toString();
                        IngredientModel item = new IngredientModel(name, amount, unit);
                        ingredients.add(item);
                    }
                    arrayAdapter = new IngredientAdapter(ingredients, RecipeDetailPlanner.this);
                    recyclerView.setAdapter(arrayAdapter);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void itemClicked(int position) {

    }
}