package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecipesStep3 extends AppCompatActivity {

    ArrayList<String> category, ingredients;
    String name;
    EditText description, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_step3);

        Button next = findViewById(R.id.buttonNext3);
        description = findViewById(R.id.editTextDescription);
        link = findViewById(R.id.editTextLink);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        category = bundle.getStringArrayList("categoryNext");
        ingredients = bundle.getStringArrayList("ingredients");
        name = bundle.getString("nameNext");

        Toast.makeText(RecipesStep3.this, ingredients.get(0) + " " + category.get(0) + " " + name, Toast.LENGTH_LONG).show();

        // recipeModel = new RecipeModel(name, category, ingredients, description.getText().toString());

       /* next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //databaseReference.setValue("Hello");
                String uploadId = databaseReference.push().getKey();
                databaseReference.child(uploadId).setValue(recipeModel);
                Toast.makeText(RecipesStep3.this, "OK", Toast.LENGTH_LONG).show();
            }
        });*/

       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent newIntent = new Intent(RecipesStep3.this, RecipesStep4.class);
               Bundle newBundle = new Bundle();
               newBundle.putStringArrayList("categoryNext", category);
               newBundle.putStringArrayList("ingredients", ingredients);
               newBundle.putString("nameNext", name);
               newBundle.putString("description", description.getText().toString());
               newBundle.putString("link", link.getText().toString());
               newIntent.putExtras(newBundle);
               startActivity(newIntent);
           }
       });


    }
}