package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class PlannerDay extends AppCompatActivity {

    private FloatingActionButton addBreakfast, addDinner, addSupper, addSecondBreakfast, addSnack;
    private TextView textViewBreakfast, textViewDinner, textViewSupper, textViewSecondBreakfast, textViewSnack,
                secondBreakfastTitle, snackTitle;
    private Intent addMealIntent;
    private Bundle addMealBundle;
    private Button addIngredients;
    private CardView snack, secondBreakfast;
    private CheckBox checkBoxSecondBreakfast, checkBoxSnack;

    private DatabaseReference databaseReferencePlanner, databaseReferenceRecipe,
            databaseReferenceShoppingList;
    private ArrayList<String> names, keys, units;
    private ArrayList<Double> amounts;
    IngredientModel item;

    DatabaseReference breakfastDatabaseReference, dinnerDatabaseReference, supperDatabaseReference,
            recipeDatabaseReference, secondBreakfastDatabaseReference, snackDatabaseReference;
    String key = "";
    private boolean isSecondBreakfast = false, isSnack = false;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_day);

        addIngredients = findViewById(R.id.buttonSaveDay);
        addBreakfast = findViewById(R.id.floatingActionButtonAddBreakfastToPlanner);
        addDinner = findViewById(R.id.floatingActionButtonAddDinnerToPlanner);
        addSupper = findViewById(R.id.floatingActionButtonAddSupperToPlanner);
        addSecondBreakfast = findViewById(R.id.floatingActionButtonAddSecondBreakfastToPlanner);
        addSnack = findViewById(R.id.floatingActionBarAddSnackToPlanner);

        textViewBreakfast = findViewById(R.id.textViewPlannerBreakfastName);
        textViewDinner = findViewById(R.id.textViewPlannerDinnerName);
        textViewSupper = findViewById(R.id.textViewPlannerSupperName);
        textViewSnack = findViewById(R.id.textViewSnackName);
        textViewSecondBreakfast = findViewById(R.id.textViewPlannerSecondBreakfastName);

        secondBreakfastTitle = findViewById(R.id.textViewChosenSecondBreakfast);
        snackTitle = findViewById(R.id.textViewChosenSnack);

        snack = findViewById(R.id.cardViewAddSnack);
        secondBreakfast = findViewById(R.id.cardViewAddSecondBreakfast);
        checkBoxSecondBreakfast = findViewById(R.id.checkBoxSecondBreakfast);
        checkBoxSnack = findViewById(R.id.checkBoxAddSnack);

        secondBreakfast.setVisibility(View.INVISIBLE);
        secondBreakfastTitle.setVisibility(View.INVISIBLE);
        textViewSecondBreakfast.setVisibility(View.INVISIBLE);
        addSecondBreakfast.setVisibility(View.INVISIBLE);

        snackTitle.setVisibility(View.INVISIBLE);
        snack.setVisibility(View.INVISIBLE);
        textViewSnack.setVisibility(View.INVISIBLE);
        addSecondBreakfast.setVisibility(View.INVISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String day = bundle.getString("day");
        //Toast.makeText(this, day, Toast.LENGTH_SHORT).show();

        assert day != null;
        breakfastDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child(day).child("breakfast");
        dinnerDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child(day).child("dinner");
        supperDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child(day).child("supper");
        snackDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child(day).child("snack");
        secondBreakfastDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child(day).child("secondBreakfast");

        checkBoxSnack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxSnack.isChecked()) {
                    isSnack = true;
                } else {
                    isSnack = false;
                }
                checkAdditionalMeals();
            }
        });

        checkBoxSecondBreakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxSecondBreakfast.isChecked()) {
                    isSecondBreakfast = true;
                } else {
                    isSecondBreakfast = false;
                }
                checkAdditionalMeals();

            }
        });

        databaseReferenceShoppingList = FirebaseDatabase.getInstance().getReference("ShoppingList").child(uid);

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

                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewBreakfast.setText(name);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                checkIngredients(ingredientName, amount, unit);

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

                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewDinner.setText(name);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                checkIngredients(ingredientName, amount, unit);

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
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSupper.setText(name);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {

                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                checkIngredients(ingredientName, amount, unit);

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

        ValueEventListener secondBreakfastEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addSecondBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            if (secondBreakfastTitle.getText().toString().equals("Przekąska"))
                                textViewSnack.setText(name);
                            else
                                textViewSecondBreakfast.setText(name);
                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                checkIngredients(ingredientName, amount, unit);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(PlannerDay.this, "No", Toast.LENGTH_SHORT).show();
                    addSecondBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        secondBreakfastDatabaseReference.addValueEventListener(secondBreakfastEventListener);
        //dinnerEventListener.notify();

        ValueEventListener snackEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addSnack.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            if (secondBreakfastTitle.getText().toString().equals("Przekąska"))
                                textViewSecondBreakfast.setText(name);
                            else
                                textViewSnack.setText(name);

                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {

                                final String ingredientName = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("name").getValue().toString();
                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("amount").getValue().toString());
                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                        .child("unit").getValue().toString();
                                checkIngredients(ingredientName, amount, unit);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    addSnack.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        snackDatabaseReference.addValueEventListener(snackEventListener);

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

        addSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealBundle.putString("meal", "secondBreakfast");
                addMealIntent.putExtras(addMealBundle);
                startActivity(addMealIntent);
            }
        });

        addSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealBundle.putString("meal", "snack");
                addMealIntent.putExtras(addMealBundle);
                startActivity(addMealIntent);
            }
        });
    }

    private void checkAdditionalMeals () {

        if (isSnack && isSecondBreakfast) {
            secondBreakfast.setVisibility(View.VISIBLE);
            secondBreakfastTitle.setVisibility(View.VISIBLE);
            textViewSecondBreakfast.setVisibility(View.VISIBLE);
            addSecondBreakfast.setVisibility(View.VISIBLE);
            secondBreakfastTitle.setText("II śniadanie");

            snackTitle.setVisibility(View.VISIBLE);
            snack.setVisibility(View.VISIBLE);
            textViewSnack.setVisibility(View.VISIBLE);
            addSnack.setVisibility(View.VISIBLE);
            snackTitle.setText("Przekąska");
        } else if (isSnack) {
            secondBreakfast.setVisibility(View.VISIBLE);
            secondBreakfastTitle.setVisibility(View.VISIBLE);
            textViewSecondBreakfast.setVisibility(View.VISIBLE);
            addSecondBreakfast.setVisibility(View.VISIBLE);
            secondBreakfastTitle.setText("Przekąska");

            snackTitle.setVisibility(View.GONE);
            snack.setVisibility(View.GONE);
            textViewSnack.setVisibility(View.GONE);
            addSnack.setVisibility(View.GONE);
        } else if (isSecondBreakfast) {
            secondBreakfast.setVisibility(View.VISIBLE);
            secondBreakfastTitle.setVisibility(View.VISIBLE);
            textViewSecondBreakfast.setVisibility(View.VISIBLE);
            addSecondBreakfast.setVisibility(View.VISIBLE);
            secondBreakfastTitle.setText("II śniadanie");

            snackTitle.setVisibility(View.GONE);
            snack.setVisibility(View.GONE);
            textViewSnack.setVisibility(View.GONE);
            addSnack.setVisibility(View.GONE);
        }  else {
            secondBreakfast.setVisibility(View.INVISIBLE);
            secondBreakfastTitle.setVisibility(View.INVISIBLE);
            textViewSecondBreakfast.setVisibility(View.INVISIBLE);
            addSecondBreakfast.setVisibility(View.INVISIBLE);

            snackTitle.setVisibility(View.INVISIBLE);
            snack.setVisibility(View.INVISIBLE);
            textViewSnack.setVisibility(View.INVISIBLE);
            addSecondBreakfast.setVisibility(View.INVISIBLE);

        }

    }

    private void checkIngredients (String ingredientName, double amount, String unit) {
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
                        Double newAmount = amount / 1000 + amounts.get(index);
                        amounts.set(index, newAmount);
                        break;
                    }
                    case "szklanka": {
                        Double newAmount = 0.25 + amounts.get(index);
                        amounts.set(index, newAmount);
                        break;
                    }
                }
            } else if (ingredientUnit.equals("ml")) {
                if (unit.equals("l")) {
                    double newAmount = amount + (amounts.get(index) / 1000);
                    amounts.set(index, newAmount);
                    units.set(index, "l");
                } else  {
                    double newAmount = 0;
                    switch (unit) {
                        case "ml": {
                            newAmount = amount + amounts.get(index);
                            break;
                        }
                        case "szklanka": {
                            newAmount = 250 + amounts.get(index);
                            break;
                        }

                    }
                    if (newAmount < 1000.0) {
                        amounts.set(index, newAmount);
                        units.set(index, "ml");
                    } else {
                        amounts.set(index, newAmount/1000);
                        units.set(index, "l");
                    }
                }
            } else if (ingredientUnit.equals("g")) {
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
                    if (newAmount < 1000.0) {
                        amounts.set(index, newAmount);
                        units.set(index, "g");
                    } else {
                        amounts.set(index, newAmount/1000);
                        units.set(index, "kg");
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
                    if (amount < 1000.0) {
                        amounts.add(amount);
                        units.add("g");
                    } else {
                        amounts.add(amount / 1000);
                        units.add("kg");
                    }
                    break;
                }
                case "łyżka": {
                    amounts.add(15.0);
                    units.add("g");
                    break;
                }
                case "łyżeczka": {
                    amounts.add(5.0);
                    units.add("g");
                    break;
                }
                case "szczypta": {
                    amounts.add(0.05);
                    units.add("g");
                    break;
                }
                case "ml": {
                    amounts.add(amount);
                    units.add("ml");
                    break;
                }
                case "szklanka": {
                    amounts.add(250.0);
                    units.add("ml");
                    break;
                }
            }
        }
    }
}