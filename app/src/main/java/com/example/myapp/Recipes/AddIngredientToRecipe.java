package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.AddItem;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddIngredientToRecipe extends AppCompatActivity {

    private EditText name, amount;
    private Spinner unit;
    private FloatingActionButton addIngredient;
    IngredientModel newItem;
    DatabaseReference databaseReference;
    ArrayList<IngredientModel> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = findViewById(R.id.editTextEditName);
        amount = findViewById(R.id.editTextEditAmount);
        unit = findViewById(R.id.spinnerUnit);
        addIngredient = findViewById(R.id.floatingActionBarAddItemShoppingList);

        addItemsToSpinner();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String key = bundle.getString("key");
        ingredients = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key).child("ingredientModels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                        Toast.makeText(AddIngredientToRecipe.this, "ok", Toast.LENGTH_SHORT).show();
                        String name = snapshot.child(String.valueOf(i)).child("name").getValue().toString();
                        double amount = Double.parseDouble(snapshot.child(String.valueOf(i))
                                .child("amount").getValue().toString());
                        String unit = snapshot.child(String.valueOf(i))
                                .child("unit").getValue().toString();
                        IngredientModel ingredient = new IngredientModel(name, amount, unit);
                        ingredients.add(ingredient);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty() && !amount.getText().toString().isEmpty()) {
                    newItem = new IngredientModel(name.getText().toString(),
                            Double.parseDouble(amount.getText().toString()),
                            unit.getSelectedItem().toString());
                    ingredients.add(newItem);
                    databaseReference.setValue(ingredients);
                    finish();
                }
            }
        });



    }

    private void addItemsToSpinner() {
        ArrayList<String> units = new ArrayList<>();
        units.add("kg");
        units.add("g");
        units.add("sztuka");
        units.add("łyżka");
        units.add("łyżeczka");
        units.add("szczypta");
        units.add("l");
        units.add("ml");
        units.add("szklanka");
        unit.setAdapter(new ArrayAdapter<>(AddIngredientToRecipe.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }
}