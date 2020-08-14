package com.example.myapp.ShoppingList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private ArrayList<String> names, keys;
    private ArrayList<Double> amounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameEditText = findViewById(R.id.editTextEditName);
        amountEditText = findViewById(R.id.editTextEditAmount);
        unitSpinner = findViewById(R.id.spinnerEditUnit);
        addItem = findViewById(R.id.floatingActionBarAddItemShoppingList);
        addItemsToSpinner();

        // TODO - obczaić czy można to lepiej rozwiązać
        names = new ArrayList<>();
        keys = new ArrayList<>();
        amounts = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    item = postSnapshot.getValue(IngredientModel.class);
                    item.setItemKey(postSnapshot.getKey());
                    //Toast.makeText(AddItem.this, item.getName(), Toast.LENGTH_LONG).show();
                    names.add(item.getName());
                    keys.add(item.getItemKey());
                    amounts.add(item.getAmount());
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
                        if (newItem.getName().equals(names.get(i))) {
                            Toast.makeText(AddItem.this,"ok", Toast.LENGTH_LONG).show();
                            Double newAmount = amounts.get(i) + newItem.getAmount();
                            Toast.makeText(AddItem.this,keys.get(i), Toast.LENGTH_LONG).show();
                            databaseReference.child(keys.get(i)).child("amount").setValue(newAmount);
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
        units.add("sztuka");
        unitSpinner.setAdapter(new ArrayAdapter<>(AddItem.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }
}