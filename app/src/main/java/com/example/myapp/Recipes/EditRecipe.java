package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.EditIngredient;
import com.example.myapp.ShoppingList.IngredientAdapter;
import com.example.myapp.ShoppingList.IngredientModel;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditRecipe extends AppCompatActivity implements IngredientAdapter.ItemClickedListener{

    private DatabaseReference databaseReference, databaseReferenceIngredients;
    private ImageView imageView, editImageView;
    private EditText editTextName, editTextDescription, editTextLink;
    IngredientAdapter arrayAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<IngredientModel> ingredients;
    Button saveChanges;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        imageView = findViewById(R.id.imageViewRecipeImageDetailEdit);
        editImageView = findViewById(R.id.imageViewEditImage);
        editTextName = findViewById(R.id.editTextRecipeNameDetailEdit);
        editTextDescription = findViewById(R.id.editTextDisplayDescriptionEdit);
        editTextLink = findViewById(R.id.editTextDisplayLinkEdit);
        saveChanges = findViewById(R.id.buttonSaveChanges);
        recyclerView = findViewById(R.id.recycler_view_ingredients_edit);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        key = bundle.getString("key");
        //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Toast.makeText(EditRecipe.this, snapshot.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                    editTextName.setText(snapshot.child("name").getValue().toString());
                    editTextDescription.setText(snapshot.child("description").getValue().toString());
                    editTextLink.setText(snapshot.child("link").getValue().toString());
                    Picasso.with(EditRecipe.this)
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

        databaseReferenceIngredients = FirebaseDatabase.getInstance().getReference("Recipes")
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
                        //Toast.makeText(RecipeDetail.this, name, Toast.LENGTH_SHORT).show();
                        IngredientModel item = new IngredientModel(name, amount, unit);
                        ingredients.add(item);
                    }
                    arrayAdapter = new IngredientAdapter(ingredients, EditRecipe.this);
                    recyclerView.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("name").setValue(editTextName.getText().toString());
                databaseReference.child("description").setValue(editTextDescription.getText().toString());
                databaseReference.child("link").setValue(editTextLink.getText().toString());
                finish();
            }
        });
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(EditRecipe.this, EditIngredientRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", ingredients.get(position).getName());
        bundle.putString("position", String.valueOf(position));
        bundle.putString("key", key);
        intent.putExtras(bundle);
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}