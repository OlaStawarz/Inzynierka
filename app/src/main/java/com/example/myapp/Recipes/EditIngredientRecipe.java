package com.example.myapp.Recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.EditIngredient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditIngredientRecipe extends AppCompatActivity {

    private EditText name, amount;
    private Spinner unit;
    private Button edit;
    private DatabaseReference databaseReference;
    String position, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        name = findViewById(R.id.editTextEditName);
        amount = findViewById(R.id.editTextEditAmount);
        unit = findViewById(R.id.spinnerEditUnit);
        edit = findViewById(R.id.buttonEditIngredient);
        addItemsToSpinner();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        position = bundle.getString("position");
        key = bundle.getString("key");

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key).child("ingredientModels");


        //Toast.makeText(EditIngredient.this, key, Toast.LENGTH_LONG).show();
        String itemName = bundle.getString("name");
        name.setText(itemName);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty()) {
                    Toast.makeText(EditIngredientRecipe.this, position, Toast.LENGTH_SHORT).show();
                    assert position != null;
                    databaseReference.child(position).child("name").setValue(name.getText().toString());
                }
                if (!amount.getText().toString().isEmpty()) {
                    databaseReference.child(position).child("amount").setValue(Double.parseDouble(amount.getText().toString()));
                }
                if (!unit.getSelectedItem().toString().isEmpty()) {
                    databaseReference.child(position).child("unit").setValue(unit.getSelectedItem().toString());
                }
                finish();
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
        unit.setAdapter(new ArrayAdapter<>(EditIngredientRecipe.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }
}