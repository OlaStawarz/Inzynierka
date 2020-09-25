package com.example.myapp.ShoppingList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.Planner.Planner;
import com.example.myapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

    private EditText nameEditText, amountEditText;
    private Spinner unitSpinner;
    FloatingActionButton addItem;
    private DatabaseReference databaseReference;
    IngredientModel newItem, item;
    private boolean updated = false;
    private ArrayList<String> names, keys, units;
    private ArrayList<Double> amounts;
    private double newAmount;
    private String newUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameEditText = findViewById(R.id.editTextEditName);
        amountEditText = findViewById(R.id.editTextEditAmount);
        unitSpinner = findViewById(R.id.spinnerUnit);
        addItem = findViewById(R.id.floatingActionBarAddItemShoppingList);
        addItemsToSpinner();

        names = new ArrayList<>();
        keys = new ArrayList<>();
        amounts = new ArrayList<>();
        units = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        item = postSnapshot.getValue(IngredientModel.class);
                        item.setItemKey(postSnapshot.getKey());
                        //Toast.makeText(AddItem.this, item.getName(), Toast.LENGTH_LONG).show();
                        names.add(item.getName());
                        keys.add(item.getItemKey());
                        amounts.add(item.getAmount());
                        units.add(item.getUnit());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameEditText.getText().toString().isEmpty()
                        && !amountEditText.getText().toString().isEmpty()) {
                    newItem = new IngredientModel(nameEditText.getText().toString(),
                            Double.parseDouble(amountEditText.getText().toString()),
                            unitSpinner.getSelectedItem().toString());
                    for (int i = 0; i < names.size(); i++) {
                        String unit = units.get(i);
                        double amount = amounts.get(i);
                        if (newItem.getName().equals(names.get(i))) {
                            if (unit.equals("kg")) {
                                newUnit = "kg";
                                if (newItem.getUnit().equals("kg")) {
                                    newAmount = amount + newItem.getAmount();
                                } else if (newItem.getUnit().equals("g")) {
                                    newAmount = amount + newItem.getAmount() / 1000;
                                }
                            } else if (unit.equals("g")) {
                                if (newItem.getUnit().equals("kg")) {
                                    newAmount = newItem.getAmount() + amount / 1000;
                                    newUnit = "kg";
                                } else {
                                    if (newItem.getUnit().equals("g")) {
                                        newAmount = newItem.getAmount() + amount;
                                    }
                                    if (newAmount < 1000.0) {
                                        newUnit = "g";
                                    } else {
                                        newUnit = "kg";
                                        newAmount /= 1000;
                                    }
                                }
                            } else if (unit.equals("l")){
                                newUnit = "l";
                                if (newItem.getUnit().equals("l")) {
                                    newAmount = amount + newItem.getAmount();
                                } else if (newItem.getUnit().equals("ml")) {
                                    newAmount = amount + newItem.getAmount() / 100;
                                }
                            } else if (unit.equals("ml")) {
                                if (newItem.getUnit().equals("l")) {
                                    newAmount = amount / 100 + newItem.getAmount();
                                    newUnit = "l";
                                } else {
                                    if (newItem.getUnit().equals("ml")) {
                                        newAmount = amount + newItem.getAmount();
                                    }
                                    if (newAmount < 1000.0) {
                                        newUnit = "ml";
                                    } else {
                                        newUnit = "l";
                                        newAmount /= 100;
                                    }
                                }
                            } else {
                                newUnit = "sztuka";
                                newAmount = amount + newItem.getAmount();
                            }

                            databaseReference.child(keys.get(i)).child("amount").setValue(newAmount);
                            databaseReference.child(keys.get(i)).child("unit").setValue(newUnit);
                            updated = true;
                            finish();
                        }
                    }
                    if (!updated) {
                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(newItem);
                        finish();
                    }
                }
            }
        });

    }

    private void addItemsToSpinner() {
        ArrayList<String> units = new ArrayList<>();
        units.add("kg");
        units.add("g");
        units.add("sztuka");
        units.add("l");
        units.add("ml");
        unitSpinner.setAdapter(new ArrayAdapter<>(AddItem.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }
}