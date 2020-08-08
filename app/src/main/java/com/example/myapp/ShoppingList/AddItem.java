package com.example.myapp.ShoppingList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

    private EditText nameEditText, amountEditText;
    private Spinner unitSpinner;
    FloatingActionButton addItem;
    private DatabaseReference databaseReference;
    ItemModel item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameEditText = findViewById(R.id.editTextNameShoppingList);
        amountEditText = findViewById(R.id.editTextAddAmountShoppingList);
        unitSpinner = findViewById(R.id.spinnerAddUnitShoppingList);
        addItem = findViewById(R.id.floatingActionBarAddItemShoppingList);
        addItemsToSpinner();

        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList");

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameEditText.getText().toString().isEmpty()
                        && !amountEditText.getText().toString().isEmpty()) {
                    item = new ItemModel(nameEditText.getText().toString(),
                            Double.parseDouble(amountEditText.getText().toString()),
                            unitSpinner.getSelectedItem().toString());
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(item);
                    finish();
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