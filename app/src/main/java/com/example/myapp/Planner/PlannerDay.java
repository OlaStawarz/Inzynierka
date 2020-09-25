package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class PlannerDay extends AppCompatActivity {

    private FloatingActionButton addBreakfast, addDinner, addSupper;
    private TextView textViewBreakfast, textViewDinner, textViewSupper;
    private ImageView imageBreakfast, imageDinner, imageSupper;
    private Intent addMealIntent;
    private Bundle addMealBundle;
    private Button addIngredients;


    private DatabaseReference databaseReferencePlanner, databaseReferenceRecipe,
            databaseReferenceShoppingList;
    private ArrayList<String> names, keys, units;
    private ArrayList<Double> amounts;
    IngredientModel item;

    DatabaseReference breakfastDatabaseReference, dinnerDatabaseReference, supperDatabaseReference,
            recipeDatabaseReference;
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_day);

        addIngredients = findViewById(R.id.buttonSaveDay);
        addBreakfast = findViewById(R.id.floatingActionButtonAddBreakfastToPlanner);
        addDinner = findViewById(R.id.floatingActionButtonAddDinnerToPlanner);
        addSupper = findViewById(R.id.floatingActionButtonAddSupperToPlanner);
        textViewBreakfast = findViewById(R.id.textViewPlannerBreakfastName);
        imageBreakfast = findViewById(R.id.imageViewPlannerBreakfast);
        textViewDinner = findViewById(R.id.textViewPlannerDinnerName);
        imageDinner = findViewById(R.id.imageViewPlannerDinner);
        textViewSupper = findViewById(R.id.textViewPlannerSupperName);
        imageSupper = findViewById(R.id.imageViewPlannerSupper);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String day = bundle.getString("day");
        //Toast.makeText(this, day, Toast.LENGTH_SHORT).show();

        assert day != null;
        breakfastDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(day).child("breakfast");
        dinnerDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(day).child("dinner");
        supperDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(day).child("supper");

        databaseReferenceShoppingList = FirebaseDatabase.getInstance().getReference("ShoppingList");

        names = new ArrayList<>();
        keys = new ArrayList<>();
        amounts = new ArrayList<>();
        units = new ArrayList<>();
        databaseReferenceShoppingList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    item = postSnapshot.getValue(IngredientModel.class);
                    item.setItemKey(postSnapshot.getKey());
                    names.add(item.getName());
                    keys.add(item.getItemKey());
                    amounts.add(item.getAmount());
                    units.add(item.getUnit());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ValueEventListener breakfastEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));

                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewBreakfast.setText(name);
                            Picasso.with(PlannerDay.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageBreakfast);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                if (names.contains(ingredientName)) {
                                    int index = names.indexOf(ingredientName);
                                    String ingredientUnit = units.get(index);
                                    if (ingredientUnit.equals("kg")) {
                                        switch (unit) {
                                            case "kg": {
                                                Double newAmount = amount + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "g": {
                                                Double newAmount = amount / 1000 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "łyżka": {
                                                Double newAmount = 0.015 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "łyżeczka": {
                                                Double newAmount = 0.005 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "szczypta": {
                                                Double newAmount = 0.0005 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                        }
                                    } else if (ingredientUnit.equals("l")) {
                                        switch (unit) {
                                            case "l": {
                                                Double newAmount = amount + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "ml": {
                                                Double newAmount = amount / 100 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "szklanka": {
                                                Double newAmount = 0.25 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                        }
                                    } else {
                                        Double newAmount = amount + amounts.get(index);
                                        amounts.set(index, newAmount);
                                    }
                                }
                                else {
                                    names.add(ingredientName);
                                    switch (unit) {
                                        case "kg":
                                        case "l":
                                        case "sztuka": {
                                            amounts.add(amount);
                                            units.add(unit);
                                            break;
                                        }
                                        case "g": {
                                            amounts.add(amount / 1000);
                                            units.add("kg");
                                            break;
                                        }
                                        case "łyżka": {
                                            amounts.add(0.015);
                                            units.add("kg");
                                            break;
                                        }
                                        case "łyżeczka": {
                                            amounts.add(0.005);
                                            units.add("kg");
                                            break;
                                        }
                                        case "szczypta": {
                                            amounts.add(0.0005);
                                            units.add("kg");
                                            break;
                                        }
                                        case "ml": {
                                            amounts.add(amount / 100);
                                            units.add("l");
                                            break;
                                        }
                                        case "szklanka": {
                                            amounts.add(0.25);
                                            units.add("kg");
                                            break;
                                        }
                                    }

                                }
                                //keys.add(uploadId);
                                //names.add(ingredientName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(PlannerDay.this, "No", Toast.LENGTH_SHORT).show();
                    addBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        breakfastDatabaseReference.addValueEventListener(breakfastEventListener);

        ValueEventListener dinnerEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addDinner.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));

                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewDinner.setText(name);
                            Picasso.with(PlannerDay.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageDinner);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                if (names.contains(ingredientName)) {
                                    int index = names.indexOf(ingredientName);
                                    String ingredientUnit = units.get(index);
                                    //TODO: jak mniej niż 1kg to w g, a jak więcej to w kg i zaokrąglać
                                    if (ingredientUnit.equals("kg")) {
                                        switch (unit) {
                                            case "kg": {
                                                Double newAmount = amount + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "g": {
                                                Double newAmount = amount / 1000 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "łyżka": {
                                                Double newAmount = 0.015 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "łyżeczka": {
                                                Double newAmount = 0.005 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "szczypta": {
                                                Double newAmount = 0.0005 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                        }
                                    } else if (ingredientUnit.equals("l")) {
                                        switch (unit) {
                                            case "l": {
                                                Double newAmount = amount + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "ml": {
                                                Double newAmount = amount / 100 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "szklanka": {
                                                Double newAmount = 0.25 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                        }
                                    } /*else if (ingredientUnit.equals("g")) {
                                        if (unit.equals("kg")) {
                                            double newAmount = amount + (amounts.get(index) / 1000);
                                            amounts.set(index, newAmount);
                                            units.set(index, "kg");
                                        } else {
                                            double newAmount = 0;
                                            switch (unit) {
                                                case "g": {
                                                    newAmount = amount + amounts.get(index);

                                                    break;
                                                }
                                                case "łyżka": {
                                                    newAmount = 15 + amounts.get(index);
                                                    break;
                                                }
                                                case "łyżeczka": {
                                                    newAmount = 5 + amounts.get(index);
                                                    break;
                                                }
                                                case "szczypta": {
                                                    newAmount = 0.5 + amounts.get(index);
                                                    break;
                                                }
                                            }
                                            amounts.set(index, newAmount);

                                        }


                                    }*/ else {
                                        Double newAmount = amount + amounts.get(index);
                                        amounts.set(index, newAmount);
                                    }
                                }
                                else {
                                    names.add(ingredientName);
                                    switch (unit) {
                                        case "kg":
                                        case "l":
                                        case "sztuka": {
                                            amounts.add(amount);
                                            units.add(unit);
                                            break;
                                        }
                                        case "g": {
                                            amounts.add(amount / 1000);
                                            units.add("kg");
                                            break;
                                        }
                                        case "łyżka": {
                                            amounts.add(0.015);
                                            units.add("kg");
                                            break;
                                        }
                                        case "łyżeczka": {
                                            amounts.add(0.005);
                                            units.add("kg");
                                            break;
                                        }
                                        case "szczypta": {
                                            amounts.add(0.0005);
                                            units.add("kg");
                                            break;
                                        }
                                        case "ml": {
                                            amounts.add(amount / 100);
                                            units.add("l");
                                            break;
                                        }
                                        case "szklanka": {
                                            amounts.add(0.25);
                                            units.add("kg");
                                            break;
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    //Toast.makeText(PlannerDay.this, "No", Toast.LENGTH_SHORT).show();
                    addDinner.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dinnerDatabaseReference.addValueEventListener(dinnerEventListener);

        ValueEventListener supperEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addSupper.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSupper.setText(name);
                            Picasso.with(PlannerDay.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageSupper);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                if (names.contains(ingredientName)) {
                                    int index = names.indexOf(ingredientName);
                                    String ingredientUnit = units.get(index);
                                    if (ingredientUnit.equals("kg")) {
                                        switch (unit) {
                                            case "kg": {
                                                Double newAmount = amount + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "g": {
                                                Double newAmount = amount / 1000 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "łyżka": {
                                                Double newAmount = 0.015 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "łyżeczka": {
                                                Double newAmount = 0.005 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "szczypta": {
                                                Double newAmount = 0.0005 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                        }
                                    } else if (ingredientUnit.equals("l")) {
                                        switch (unit) {
                                            case "l": {
                                                Double newAmount = amount + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "ml": {
                                                Double newAmount = amount / 100 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                            case "szklanka": {
                                                Double newAmount = 0.25 + amounts.get(index);
                                                amounts.set(index, newAmount);
                                                break;
                                            }
                                        }
                                    } else {
                                        Double newAmount = amount + amounts.get(index);
                                        amounts.set(index, newAmount);
                                    }
                                }
                                else {
                                    names.add(ingredientName);
                                    switch (unit) {
                                        case "kg":
                                        case "l":
                                        case "sztuka": {
                                            amounts.add(amount);
                                            units.add(unit);
                                            break;
                                        }
                                        case "g": {
                                            amounts.add(amount / 1000);
                                            units.add("kg");
                                            break;
                                        }
                                        case "łyżka": {
                                            amounts.add(0.015);
                                            units.add("kg");
                                            break;
                                        }
                                        case "łyżeczka": {
                                            amounts.add(0.005);
                                            units.add("kg");
                                            break;
                                        }
                                        case "szczypta": {
                                            amounts.add(0.0005);
                                            units.add("kg");
                                            break;
                                        }
                                        case "ml": {
                                            amounts.add(amount / 100);
                                            units.add("l");
                                            break;
                                        }
                                        case "szklanka": {
                                            amounts.add(0.25);
                                            units.add("kg");
                                            break;
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(PlannerDay.this, "No", Toast.LENGTH_SHORT).show();
                    addSupper.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        supperDatabaseReference.addValueEventListener(supperEventListener);
        //dinnerEventListener.notify();

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceShoppingList.removeValue();
                for (int i = 0; i < names.size(); i++) {
                    //Toast.makeText(PlannerDay.this, names.get(i), Toast.LENGTH_SHORT).show();
                    IngredientModel ingredientModel = new IngredientModel(names.get(i), amounts.get(i), units.get(i));
                    String uploadId = databaseReferenceShoppingList.push().getKey();
                    databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                }
                finish();
            }
        });

        addMealIntent = new Intent(PlannerDay.this, ChooseMeal.class);
        addMealBundle = new Bundle();
        addMealBundle.putString("day", day);

        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealBundle.putString("meal", "breakfast");
                addMealIntent.putExtras(addMealBundle);
                startActivity(addMealIntent);
            }
        });

        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealBundle.putString("meal", "dinner");
                addMealIntent.putExtras(addMealBundle);
                startActivity(addMealIntent);
            }
        });

        addSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealBundle.putString("meal", "supper");
                addMealIntent.putExtras(addMealBundle);
                startActivity(addMealIntent);
            }
        });
    }
}