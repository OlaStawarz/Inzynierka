package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowPlan extends AppCompatActivity implements HorizontalDaysAdapter.OnHorizontalItemClickListener{

    RecyclerView recyclerView;
    ArrayList<DayModel> arrayList;
    HorizontalDaysAdapter daysAdapter;
    TextView textViewBreakfast, textViewDinner, textViewSupper, textViewBreakfastTitle,
            textViewDinnerTitle, textViewSupperTitle, isPlanExist;
    CardView breakfast, dinner, supper;
    ImageView imageViewBreakfast, imageViewDinner, imageViewSupper;
    DatabaseReference recipeDatabaseReference, databaseReference, databaseReferenceBreakfast,
                        databaseReferenceDinner, databaseReferenceSupper, databaseReferenceT;
    String[] daysNumbers;
    String keyBreakfast, keyDinner, keySupper;

    Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

        recyclerView = findViewById(R.id.recycler_view_show_plan);
        textViewBreakfast = findViewById(R.id.textViewPlannerBreakfastName);
        textViewDinner = findViewById(R.id.textViewPlannerDinnerName);
        textViewSupper = findViewById(R.id.textViewPlannerSupperName);
        imageViewBreakfast = findViewById(R.id.imageViewPlannerBreakfast);
        imageViewDinner = findViewById(R.id.imageViewPlannerDinner);
        imageViewSupper = findViewById(R.id.imageViewPlannerSupper);
        textViewBreakfastTitle = findViewById(R.id.textViewChosenBreakfast);
        textViewDinnerTitle = findViewById(R.id.textViewChosenDinner);
        textViewSupperTitle = findViewById(R.id.textViewChosenSupper);
        isPlanExist = findViewById(R.id.textViewIfPlanExist);
        breakfast = findViewById(R.id.cardViewAddBreakfast);
        dinner = findViewById(R.id.cardViewAddDinner);
        supper = findViewById(R.id.cardViewAddSupper);

        intent = new Intent(ShowPlan.this, RecipeDetail.class);
        bundle = new Bundle();

        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Planner");
        databaseReferenceT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isPlanExist.setText("Ustalony plan posiłków");
                    Toast.makeText(ShowPlan.this, "jest", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowPlan.this, "nie jest", Toast.LENGTH_SHORT).show();
                    isPlanExist.setText("Obecnie nie posiadasz żadnego planu.");
                    recyclerView.setVisibility(View.INVISIBLE);
                    textViewBreakfast.setVisibility(View.INVISIBLE);
                    textViewDinner.setVisibility(View.INVISIBLE);
                    textViewSupper.setVisibility(View.INVISIBLE);
                    breakfast.setVisibility(View.INVISIBLE);
                    dinner.setVisibility(View.INVISIBLE);
                    supper.setVisibility(View.INVISIBLE);
                    textViewBreakfast.setVisibility(View.INVISIBLE);
                    textViewDinner.setVisibility(View.INVISIBLE);
                    textViewSupper.setVisibility(View.INVISIBLE);
                    imageViewBreakfast.setVisibility(View.INVISIBLE);
                    imageViewDinner.setVisibility(View.INVISIBLE);
                    imageViewSupper.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // show first day when open activity
        databaseReferenceBreakfast = FirebaseDatabase.getInstance().getReference("Planner").child("day1").child("breakfast");
        databaseReferenceDinner = FirebaseDatabase.getInstance().getReference("Planner").child("day1").child("dinner");
        databaseReferenceSupper = FirebaseDatabase.getInstance().getReference("Planner").child("day1").child("supper");

        databaseReferenceBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keyBreakfast = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(keyBreakfast);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewBreakfast.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewBreakfast);
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
        });

        databaseReferenceSupper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keyDinner = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(keyDinner);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSupper.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewSupper);
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
        });

        databaseReferenceDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keySupper = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(keySupper);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewDinner.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewDinner);
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
        });

        // show plan from another days
        databaseReference = FirebaseDatabase.getInstance().getReference("Planner");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long children = snapshot.getChildrenCount();
                Toast.makeText(ShowPlan.this, String.valueOf(children), Toast.LENGTH_SHORT).show();
                daysNumbers = new String[(int)children];

                if (snapshot.hasChild("day1") && !snapshot.hasChild("day2")) {
                    daysNumbers[0] = "Dzień 1";
                } else if (snapshot.hasChild("day1") && snapshot.hasChild("day2")
                        && !snapshot.hasChild("day3")) {
                    daysNumbers[0] = "Dzień 1";
                    daysNumbers[1] = "Dzień 2";
                } else if (snapshot.hasChild("day1") && snapshot.hasChild("day2")
                        && snapshot.hasChild("day3") && !snapshot.hasChild("day4")) {
                    daysNumbers[0] = "Dzień 1";
                    daysNumbers[1] = "Dzień 2";
                    daysNumbers[2] = "Dzień 3";
                } else if (snapshot.hasChild("day1") && snapshot.hasChild("day2")
                        && snapshot.hasChild("day3") && snapshot.hasChild("day4")) {
                    daysNumbers[0] = "Dzień 1";
                    daysNumbers[1] = "Dzień 2";
                    daysNumbers[2] = "Dzień 3";
                    daysNumbers[3] = "Dzień 4";
                }

                arrayList = new ArrayList<>();
                for (int i = 0; i < daysNumbers.length; i++) {
                    DayModel dayModel = new DayModel(daysNumbers[i]);
                    arrayList.add(dayModel);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowPlan.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                daysAdapter = new HorizontalDaysAdapter(ShowPlan.this, arrayList, ShowPlan.this);
                recyclerView.setAdapter(daysAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("key", keyBreakfast);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("key", keyDinner);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        supper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("key", keySupper);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void dayClicked(int position) {
        Toast.makeText(ShowPlan.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        databaseReferenceBreakfast = FirebaseDatabase.getInstance().getReference("Planner")
                .child("day" + (position + 1)).child("breakfast");
        databaseReferenceBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                keyBreakfast = snapshot.child("key").getValue().toString();
                recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(keyBreakfast);
                recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        textViewBreakfast.setText(name);
                        Picasso.with(ShowPlan.this)
                                .load(snapshot.child("imageUrl").getValue().toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(imageViewBreakfast);
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

        databaseReferenceDinner = FirebaseDatabase.getInstance().getReference("Planner")
                .child("day" + (position + 1)).child("dinner");
        databaseReferenceDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                keyDinner = snapshot.child("key").getValue().toString();
                recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(keyDinner);
                recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        textViewDinner.setText(name);
                        Picasso.with(ShowPlan.this)
                                .load(snapshot.child("imageUrl").getValue().toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(imageViewDinner);
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

        databaseReferenceSupper = FirebaseDatabase.getInstance().getReference("Planner")
                .child("day" + (position + 1)).child("supper");
        databaseReferenceSupper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                keySupper = snapshot.child("key").getValue().toString();
                recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(keySupper);
                recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        textViewSupper.setText(name);
                        Picasso.with(ShowPlan.this)
                                .load(snapshot.child("imageUrl").getValue().toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(imageViewSupper);
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