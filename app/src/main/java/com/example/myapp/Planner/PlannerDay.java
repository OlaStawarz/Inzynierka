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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PlannerDay extends AppCompatActivity {

    private FloatingActionButton addBreakfast, addDinner, addSupper;
    private TextView textViewBreakfast, textViewDinner, textViewSupper;
    private ImageView imageBreakfast, imageDinner, imageSupper;
    private Intent addMealIntent;
    private Bundle addMealBundle;

    DatabaseReference breakfastDatabaseReference, dinnerDatabaseReference, supperDatabaseReference, recipeDatabaseReference;
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_day);

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
        Toast.makeText(this, day, Toast.LENGTH_SHORT).show();

        assert day != null;
        breakfastDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(day).child("breakfast");
        dinnerDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(day).child("dinner");
        supperDatabaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(day).child("supper");


        ValueEventListener breakfastEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //Toast.makeText(PlannerDay.this, "Yes", Toast.LENGTH_SHORT).show();
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    //Toast.makeText(PlannerDay.this, key, Toast.LENGTH_SHORT).show();

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
        //breakfastEventListener.notifyAll();

        ValueEventListener dinnerEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //Toast.makeText(PlannerDay.this, "Yes", Toast.LENGTH_SHORT).show();
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    //Toast.makeText(PlannerDay.this, key, Toast.LENGTH_SHORT).show();

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
        dinnerDatabaseReference.addValueEventListener(dinnerEventListener);

        ValueEventListener supperEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //Toast.makeText(PlannerDay.this, "Yes", Toast.LENGTH_SHORT).show();
                    key = Objects.requireNonNull(snapshot.child("key").getValue()).toString();
                    addBreakfast.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    //Toast.makeText(PlannerDay.this, key, Toast.LENGTH_SHORT).show();

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
        supperDatabaseReference.addValueEventListener(supperEventListener);
        //dinnerEventListener.notify();

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