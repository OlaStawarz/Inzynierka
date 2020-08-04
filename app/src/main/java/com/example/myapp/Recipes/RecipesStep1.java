package com.example.myapp.Recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.myapp.R;

import java.util.ArrayList;

public class RecipesStep1 extends AppCompatActivity {

    EditText recipeName;
    CheckBox breakfast, dinner, supper, snack;
    Button next;
    ArrayList<String> category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_step1);

        recipeName = findViewById(R.id.editTextName);
        breakfast = findViewById(R.id.checkBoxBreakfast);
        dinner = findViewById(R.id.checkBoxDinner);
        supper = findViewById(R.id.checkBoxSupper);
        snack = findViewById(R.id.checkBoxSnack);
        next = findViewById(R.id.buttonNext1);

        category = new ArrayList<>();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (breakfast.isChecked()) {
                    category.add(breakfast.getText().toString());
                }
                if (dinner.isChecked()) {
                    category.add(dinner.getText().toString());
                }
                if (supper.isChecked()) {
                    category.add(supper.getText().toString());
                }
                if (snack.isChecked()) {
                    category.add(snack.getText().toString());
                }

                Intent intent = new Intent(RecipesStep1.this, RecipesStep2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", recipeName.getText().toString());
                bundle.putStringArrayList("category", category);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }
}