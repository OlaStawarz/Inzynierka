package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.EditIngredient;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddPlan extends AppCompatActivity {

    //String updatedKey;
    //Double newAmount;
    private Spinner spinnerDays;
    private CardView day1, day2, day3, day4, day5, day6;
    private Button savePlan;
    private Intent intent;
    private Bundle bundle;
    private DatabaseReference databaseReferencePlanner, databaseReferenceRecipe,
            databaseReferenceShoppingList;
    private ArrayList<String> names, keys, existingNames;
    private ArrayList<Double> amounts, blaA;
    IngredientModel newItem, item;
    boolean updatedSupper = false, updatedDinner = false, updatedBreakfast = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        spinnerDays = findViewById(R.id.spinnerNumberOfDays);
        day1 = findViewById(R.id.cardViewDay1);
        day2 = findViewById(R.id.cardViewDay2);
        day3 = findViewById(R.id.cardViewDay3);
        day4 = findViewById(R.id.cardViewDay4);
        day5 = findViewById(R.id.cardViewDay5);
        day6 = findViewById(R.id.cardViewDay6);
        savePlan = findViewById(R.id.buttonSavePlan);


        databaseReferenceRecipe = FirebaseDatabase.getInstance().getReference("Recipes");
        databaseReferencePlanner = FirebaseDatabase.getInstance().getReference("Planner");
        databaseReferenceShoppingList = FirebaseDatabase.getInstance().getReference("ShoppingList");

        names = new ArrayList<>();
        keys = new ArrayList<>();
        amounts = new ArrayList<>();
        databaseReferenceShoppingList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    item = postSnapshot.getValue(IngredientModel.class);
                    item.setItemKey(postSnapshot.getKey());
                    names.add(item.getName());
                    keys.add(item.getItemKey());
                    amounts.add(item.getAmount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addNumbersToSpinner(spinnerDays);
        setVisibility();

        intent = new Intent(AddPlan.this, PlannerDay.class);
        bundle = new Bundle();

        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("day", "day1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("day", "day2");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("day", "day3");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("day", "day4");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("day", "day5");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("day", "day6");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        final ArrayList<String> days = new ArrayList<>();
        /*names = new ArrayList<>();
        keys = new ArrayList<>();
        amounts = new ArrayList<>();
        existingNames = new ArrayList<>();*/
        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            //Toast.makeText(AddPlan.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            days.add(dataSnapshot.getKey());
                        }
                        for (int i = 0; i < days.size(); i++) {
                            databaseReferencePlanner.child(days.get(i)).child("breakfast").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String key = snapshot.child("key").getValue().toString();
                                    databaseReferenceRecipe = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
                                    databaseReferenceRecipe.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                                final String name = snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("name").getValue().toString();
                                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("amount").getValue().toString());
                                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("unit").getValue().toString();
                                                if (names.contains(name)) {
                                                    Toast.makeText(AddPlan.this, "juz jest", Toast.LENGTH_SHORT).show();
                                                    int index = names.indexOf(name);
                                                    Toast.makeText(AddPlan.this, names.get(index) + keys.get(index), Toast.LENGTH_SHORT).show();
                                                    Double newAmount = amount + amounts.get(index);
                                                    amounts.set(index, newAmount);
                                                    databaseReferenceShoppingList.child(keys.get(index)).child("amount").setValue(newAmount);
                                                } else {
                                                    IngredientModel ingredientModel = new IngredientModel(name, amount, unit);
                                                    String uploadId = databaseReferenceShoppingList.push().getKey();
                                                    databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                                                    names.add(name);
                                                    amounts.add(amount);
                                                    keys.add(uploadId);
                                                }

                                                /*databaseReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot data : snapshot.getChildren()) {
                                                            if (data.child("name").getValue().toString().equals(name)) {
                                                                Toast.makeText(AddPlan.this, "juz jest", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(AddPlan.this, " ", Toast.LENGTH_SHORT).show();
                                                                newAmount = amount + Double.parseDouble(data.child("amount").getValue().toString());
                                                                databaseReferenceShoppingList.child(data.getKey()).child("amount").setValue(newAmount);
                                                            } else {
                                                                IngredientModel ingredientModel = new IngredientModel(name, amount, unit);
                                                                String uploadId = databaseReferenceShoppingList.push().getKey();
                                                                databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });*/
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReferencePlanner.child(days.get(i)).child("dinner").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String key = snapshot.child("key").getValue().toString();
                                    databaseReferenceRecipe = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
                                    databaseReferenceRecipe.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                                final String name = snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("name").getValue().toString();
                                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("amount").getValue().toString());
                                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("unit").getValue().toString();
                                                if (names.contains(name)) {
                                                    Toast.makeText(AddPlan.this, "juz jest", Toast.LENGTH_SHORT).show();
                                                    int index = names.indexOf(name);
                                                    Toast.makeText(AddPlan.this, names.get(index) + keys.get(index), Toast.LENGTH_SHORT).show();
                                                    Double newAmount = amount + amounts.get(index);
                                                    amounts.set(index, newAmount);
                                                    databaseReferenceShoppingList.child(keys.get(index)).child("amount").setValue(newAmount);
                                                } else {
                                                    IngredientModel ingredientModel = new IngredientModel(name, amount, unit);
                                                    String uploadId = databaseReferenceShoppingList.push().getKey();
                                                    databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                                                    names.add(name);
                                                    amounts.add(amount);
                                                    keys.add(uploadId);
                                                }
                                                /*databaseReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot data : snapshot.getChildren()) {
                                                            if (data.child("name").getValue().toString().equals(name)) {
                                                                Toast.makeText(AddPlan.this, "juz jest", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(AddPlan.this, " ", Toast.LENGTH_SHORT).show();
                                                                newAmount = amount + Double.parseDouble(data.child("amount").getValue().toString());
                                                                databaseReferenceShoppingList.child(data.getKey()).child("amount").setValue(newAmount);
                                                            } else {

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });*/
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReferencePlanner.child(days.get(i)).child("supper").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String key = snapshot.child("key").getValue().toString();
                                    databaseReferenceRecipe = FirebaseDatabase.getInstance().getReference("Recipes").child(key);

                                    databaseReferenceRecipe.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (int i = 0; i < snapshot.child("ingredientModels").getChildrenCount(); i++) {
                                                final String name = snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("name").getValue().toString();
                                                final double amount = Double.parseDouble(snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("amount").getValue().toString());
                                                final String unit = snapshot.child("ingredientModels").child(String.valueOf(i))
                                                        .child("unit").getValue().toString();
                                                if (names.contains(name)) {
                                                    Toast.makeText(AddPlan.this, "juz jest", Toast.LENGTH_SHORT).show();
                                                    int index = names.indexOf(name);
                                                    Toast.makeText(AddPlan.this, names.get(index) + keys.get(index), Toast.LENGTH_SHORT).show();
                                                    Double newAmount = amount + amounts.get(index);
                                                    amounts.set(index, newAmount);
                                                    databaseReferenceShoppingList.child(keys.get(index)).child("amount").setValue(newAmount);
                                                } else {
                                                    IngredientModel ingredientModel = new IngredientModel(name, amount, unit);
                                                    String uploadId = databaseReferenceShoppingList.push().getKey();
                                                    databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                                                    names.add(name);
                                                    amounts.add(amount);
                                                    keys.add(uploadId);
                                                }
                                                /*databaseReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot data : snapshot.getChildren()) {
                                                            if (data.child("name").getValue().toString().equals(name)) {
                                                                Toast.makeText(AddPlan.this, "juz jest", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(AddPlan.this, " ", Toast.LENGTH_SHORT).show();
                                                                newAmount = amount + Double.parseDouble(data.child("amount").getValue().toString());
                                                                databaseReferenceShoppingList.child(data.getKey()).child("amount").setValue(newAmount);
                                                            } else {

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });*/
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                databaseReferencePlanner.addListenerForSingleValueEvent(valueEventListener);
                finish();
            }
        });

    }

    private void setVisibility () {

        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.INVISIBLE);
                        day3.setVisibility(View.INVISIBLE);
                        day4.setVisibility(View.INVISIBLE);
                        day5.setVisibility(View.INVISIBLE);
                        day6.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.INVISIBLE);
                        day4.setVisibility(View.INVISIBLE);
                        day5.setVisibility(View.INVISIBLE);
                        day6.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.VISIBLE);
                        day4.setVisibility(View.INVISIBLE);
                        day5.setVisibility(View.INVISIBLE);
                        day6.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.VISIBLE);
                        day4.setVisibility(View.VISIBLE);
                        day5.setVisibility(View.INVISIBLE);
                        day6.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.VISIBLE);
                        day4.setVisibility(View.VISIBLE);
                        day5.setVisibility(View.VISIBLE);
                        day6.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.VISIBLE);
                        day4.setVisibility(View.VISIBLE);
                        day5.setVisibility(View.VISIBLE);
                        day6.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addNumbersToSpinner(Spinner spinner) {
        ArrayList<Integer> units = new ArrayList<>();
        units.add(1);
        units.add(2);
        units.add(3);
        units.add(4);
        units.add(5);
        units.add(6);
        spinner.setAdapter(new ArrayAdapter<>(AddPlan.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }

    /*databaseReferenceShoppingList.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            if (name.equals(dataSnapshot.child("name").getValue().toString())) {
                                                                Double newAmount = amount + Double.parseDouble(dataSnapshot
                                                                        .child("amount").getValue().toString());
                                                                Toast.makeText(AddPlan.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                                                databaseReferenceShoppingList.child(dataSnapshot.getKey()).child("amount").setValue(newAmount);
                                                                updatedSupper = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!updatedSupper) {
                                                            IngredientModel ingredientModel = new IngredientModel(name, amount, unit);
                                                            String uploadId = databaseReferenceShoppingList.push().getKey();
                                                            databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });*/
                                                /*if (bla.contains(name)) {
                                                    Toast.makeText(AddPlan.this, "jest juz", Toast.LENGTH_SHORT).show();
                                                    updatedSupper = true;
                                                }*/
                                                /*databaseReferenceShoppingList.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            if (name.equals(dataSnapshot.child("name").getValue().toString())) {
                                                                Double newAmount = amount + Double.parseDouble(dataSnapshot
                                                                        .child("amount").getValue().toString());
                                                                Toast.makeText(AddPlan.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                                                databaseReferenceShoppingList.child(dataSnapshot.getKey()).child("amount").setValue(newAmount);
                                                                updatedSupper = true;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });*/

                                               /* for (int w = 0; w < bla.size(); w++) {
                                                    Toast.makeText(AddPlan.this, "jak to zrobic??", Toast.LENGTH_SHORT).show();
                                                    *//*if (name.equals(bla.get(w))) {
                                                        Double newAmount = amounts.get(w) + amount;
                                                        databaseReferenceShoppingList.child(keys.get(w)).child("amount").setValue(newAmount);

                                                    }*//*
                                                    //updatedSupper = true;
                                                }*/
                                                /*for (int j = 0; j < names.size(); j++) {
                                                    if (name.equals(names.get(j))) {
                                                        //Toast.makeText(AddPlan.this, "nadpisz", Toast.LENGTH_SHORT).show();
                                                        Double newAmount = amounts.get(j) + amount;
                                                        databaseReferenceShoppingList.child(keys.get(j)).child("amount").setValue(newAmount);
                                                        updatedSupper = true;
                                                    }
                                                }*/
                                               /* for (int k = 0; k < uploadedItems.size(); k++) {
                                                    if (name.equals(uploadedItems.get(k))) {
                                                        Double newAmount = amounts.get(k) + amount;
                                                        databaseReferenceShoppingList.child(keys.get(k)).child("amount").setValue(newAmount);
                                                        updatedSupper = true;
                                                    }
                                                }*/
                                                /*if (!updatedSupper) {
                                                    IngredientModel ingredientModel = new IngredientModel(name, amount, unit);
                                                    String uploadId = databaseReferenceShoppingList.push().getKey();
                                                    databaseReferenceShoppingList.child(uploadId).setValue(ingredientModel);
                                                    *//*bla.add(name);
                                                    blaA.add(amount);*//*
                                                }*/

}