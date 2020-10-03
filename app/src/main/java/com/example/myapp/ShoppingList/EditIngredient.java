package com.example.myapp.ShoppingList;

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
import com.example.myapp.Recipes.RecipesStep2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditIngredient extends AppCompatActivity {

    private EditText name, amount;
    private Spinner unit;
    private Button edit;
    private DatabaseReference databaseReference;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        name = findViewById(R.id.editTextEditName);
        amount = findViewById(R.id.editTextEditAmount);
        unit = findViewById(R.id.spinnerEditUnit);
        edit = findViewById(R.id.buttonEditIngredient);
        addItemsToSpinner();

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList").child(uid);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        final String key = bundle.getString("key");
        //Toast.makeText(EditIngredient.this, key, Toast.LENGTH_LONG).show();
        String itemName = bundle.getString("name");
        name.setText(itemName);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty()) {
                    databaseReference.child(key).child("name").setValue(name.getText().toString());
                }
                if (!amount.getText().toString().isEmpty()) {
                    databaseReference.child(key).child("amount").setValue(Double.parseDouble(amount.getText().toString()));
                }
                if (!unit.getSelectedItem().toString().isEmpty()) {
                    databaseReference.child(key).child("unit").setValue(unit.getSelectedItem().toString());
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
        units.add("l");
        units.add("ml");
        unit.setAdapter(new ArrayAdapter<>(EditIngredient.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }
}