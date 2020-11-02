package com.example.myapp.Recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.IngredientModel;
import com.example.myapp.ShoppingList.IngredientAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipesStep2 extends AppCompatActivity implements IngredientAdapter.ItemClickedListener{


    ArrayList<String> ingredients, category, names, units, amounts;
    IngredientAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText ingredientName, ingredientAmount;
    Spinner spinnerUnit;
    String name;
    Button next;
    IngredientModel ingredientModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_step2);

        recyclerView = findViewById(R.id.ingredientsListView);
        recyclerView.setHasFixedSize(true);

        ingredientName = findViewById(R.id.editTextIngredient);
        ingredientAmount = findViewById(R.id.editTextAmountRecipe);
        spinnerUnit = findViewById(R.id.spinnerUnitRecipe);
        next = findViewById(R.id.buttonNext2);
        Button button = findViewById(R.id.floatingActionButtonAdd);

        ingredients = new ArrayList<>();
        names = new ArrayList<>();
        amounts = new ArrayList<>();
        units = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new  DefaultItemAnimator());

        addItemsToSpinner();

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        assert bundle != null;
        category = bundle.getStringArrayList("category");
        name = bundle.getString("name");

        final ArrayList<IngredientModel> ingredientModels = new ArrayList<>();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredientName.getText().toString().isEmpty() || ingredientAmount.getText().toString().isEmpty()) {
                    if (ingredientName.getText().toString().isEmpty())
                        ingredientName.setError("To pole nie może być puste!");
                    else
                        ingredientAmount.setError("To pole nie może być puste");
                } else {
                    String newIngredientName = ingredientName.getText().toString();
                    String newIngredientAmount = ingredientAmount.getText().toString();
                    String newIngredientUnit = spinnerUnit.getSelectedItem().toString();
                    ingredients.add(newIngredientName + "\t\t" + newIngredientAmount + " " + newIngredientUnit);
                    //adapter.notifyDataSetChanged();
                    names.add(newIngredientName);
                    amounts.add(newIngredientAmount);
                    units.add(newIngredientUnit);

                    IngredientModel ingredientModel = new IngredientModel(newIngredientName,
                            Double.parseDouble(newIngredientAmount), newIngredientUnit);
                    ingredientModels.add(ingredientModel);
                    adapter = new IngredientAdapter(ingredientModels, RecipesStep2.this);
                    recyclerView.setAdapter(adapter);
                    ingredientName.setText("");
                    ingredientAmount.setText("");
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (names.isEmpty()) {
                    Toast.makeText(RecipesStep2.this, "Dodaj przynajmniej jeden składnik", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                    Intent newIntent = new Intent(RecipesStep2.this, RecipesStep3.class);
                    Bundle newBundle = new Bundle();
                    newBundle.putStringArrayList("categoryNext", category);
                    newBundle.putStringArrayList("ingredients", ingredients);
                    newBundle.putStringArrayList("names", names);
                    newBundle.putStringArrayList("amounts", amounts);
                    newBundle.putStringArrayList("units", units);
                    newBundle.putString("nameNext", name);
                    newIntent.putExtras(newBundle);
                    startActivity(newIntent);
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
        spinnerUnit.setAdapter(new ArrayAdapter<>(RecipesStep2.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }

    @Override
    public void itemClicked(int position) {

    }
}