package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipesStep2 extends AppCompatActivity {


    ArrayList<String> ingredients, category;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText ingredient;
    String name;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_step2);

        listView = findViewById(R.id.ingredientsListView);
        ingredient = findViewById(R.id.editTextIngredient);
        next = findViewById(R.id.buttonNext2);
        FloatingActionButton button = findViewById(R.id.floatingActionButtonAdd);

        ingredients = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.ingredient_item, R.id.list_item_ingredient, ingredients);
        listView.setAdapter(adapter);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        category = bundle.getStringArrayList("category");
        name = bundle.getString("name");
        Toast.makeText(RecipesStep2.this, category.get(0), Toast.LENGTH_LONG).show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newIngredient = ingredient.getText().toString();
                ingredients.add(newIngredient);
                adapter.notifyDataSetChanged();
                ingredient.setText("");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(RecipesStep2.this, RecipesStep3.class);
                Bundle newBundle = new Bundle();
                newBundle.putStringArrayList("categoryNext", category);
                newBundle.putStringArrayList("ingredients", ingredients);
                newBundle.putString("nameNext", name);
                newIntent.putExtras(newBundle);
                startActivity(newIntent);
            }
        });
    }
}