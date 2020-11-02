package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeDetail;
import com.example.myapp.Recipes.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowPlan extends AppCompatActivity implements HorizontalDaysAdapter.OnHorizontalItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<DayModel> arrayList;
    private HorizontalDaysAdapter daysAdapter;
    private TextView textViewBreakfast, textViewDinner, textViewSupper, textViewBreakfastTitle,
            textViewDinnerTitle, textViewSupperTitle, isPlanExist, textViewSecondBreakfast, textViewSecondBreakfastTitle,
            textViewSnackTitle, textViewSnack, date;
    private CardView breakfast, dinner, supper, secondBreakfast, snack;
    private ImageView imageViewBreakfast, imageViewDinner, imageViewSupper, imageViewSecondBreakfast, imageViewSnack;
    private DatabaseReference recipeDatabaseReference, databaseReference, databaseReferenceBreakfast,
                        databaseReferenceDinner, databaseReferenceSupper, databaseReferenceT,
                        databaseReferenceSecondBreakfast, databaseReferenceSnack;

    private ArrayList<String> daysNumbers;
    private String keyBreakfast = "", keyDinner = "", keySupper = "", keySecondBreakfast = "", keySnack = "",
                    day1, day2, day3, day4;

    private Intent intent;
    private Bundle bundle;

    private FirebaseUser user;
    private String uid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

        recyclerView = findViewById(R.id.recycler_view_show_plan);
        textViewBreakfast = findViewById(R.id.textViewPlannerBreakfastName);
        textViewDinner = findViewById(R.id.textViewPlannerDinnerName);
        textViewSupper = findViewById(R.id.textViewPlannerSupperName);
        textViewSecondBreakfast = findViewById(R.id.textViewChosenSecondBreakfastName);
        textViewSnack = findViewById(R.id.textViewChosenSnackName);

        imageViewBreakfast = findViewById(R.id.imageViewPlannerBreakfast);
        imageViewDinner = findViewById(R.id.imageViewPlannerDinner);
        imageViewSupper = findViewById(R.id.imageViewPlannerSupper);
        imageViewSecondBreakfast = findViewById(R.id.imageViewPlannerSecondBreakfast);
        imageViewSnack = findViewById(R.id.imageViewPlannerSnack);

        textViewBreakfastTitle = findViewById(R.id.textViewChosenBreakfast);
        textViewDinnerTitle = findViewById(R.id.textViewChosenDinner);
        textViewSupperTitle = findViewById(R.id.textViewChosenSupper);
        textViewSecondBreakfastTitle = findViewById(R.id.textViewChosenSecondBreakfastPlanner);
        textViewSnackTitle = findViewById(R.id.textViewChosenSnackPlanner);

        isPlanExist = findViewById(R.id.textViewIfPlanExist);

        breakfast = findViewById(R.id.cardViewAddBreakfast);
        dinner = findViewById(R.id.cardViewAddDinner);
        supper = findViewById(R.id.cardViewAddSupper);
        secondBreakfast = findViewById(R.id.cardViewAddSecondBreakfastShowPlan);
        snack = findViewById(R.id.cardViewAddSnackShowPlan);

        date = findViewById(R.id.textViewDate);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        intent = new Intent(ShowPlan.this, RecipeDetailPlanner.class);
        bundle = new Bundle();



        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Planner").child(uid);
        databaseReferenceT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isPlanExist.setText("Ustalony plan na dzień: ");
                } else {
                    isPlanExist.setText("Obecnie nie posiadasz żadnego planu.");
                    setVisibility();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                daysNumbers = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals("day1")) {
                        daysNumbers.add("Dzień 1");
                        day1 = snapshot.child("day1").child("date").getValue().toString();
                        date.setText(day1);
                    } else if (dataSnapshot.getKey().equals("day2")) {
                        daysNumbers.add("Dzień 2");
                        day2 = snapshot.child("day2").child("date").getValue().toString();
                    } else if (dataSnapshot.getKey().equals("day3")) {
                        daysNumbers.add("Dzień 3");
                        day3 = snapshot.child("day3").child("date").getValue().toString();
                    } else if (dataSnapshot.getKey().equals("day4")) {
                        daysNumbers.add("Dzień 4");
                        day4 = snapshot.child("day4").child("date").getValue().toString();
                    }
                }

                arrayList = new ArrayList<>();
                for (int i = 0; i < daysNumbers.size(); i++) {
                    DayModel dayModel = new DayModel(daysNumbers.get(i));
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

        // show first day when opens activity
        databaseReferenceBreakfast = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child("day1").child("breakfast");
        databaseReferenceDinner = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child("day1").child("dinner");
        databaseReferenceSupper = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child("day1").child("supper");
        databaseReferenceSecondBreakfast = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child("day1").child("secondBreakfast");
        databaseReferenceSnack = FirebaseDatabase.getInstance().getReference("Planner").child(uid).child("day1").child("snack");

        databaseReferenceBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keyBreakfast = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keyBreakfast);
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
                } else {
                   breakfast.setVisibility(View.GONE);
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
                    keySupper = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keySupper);
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
                } else {
                    supper.setVisibility(View.GONE);
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
                    keyDinner = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keyDinner);
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
                } else {
                    dinner.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceSecondBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keySecondBreakfast = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keySecondBreakfast);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSecondBreakfast.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewSecondBreakfast);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    secondBreakfast.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceSnack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keySnack = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keySnack);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSnack.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewSnack);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    snack.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keyBreakfast.isEmpty()) {
                    bundle.putString("key", keyBreakfast);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keyDinner.isEmpty()) {
                    bundle.putString("key", keyDinner);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        supper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keySupper.isEmpty()) {
                    bundle.putString("key", keySupper);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        secondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keySecondBreakfast.isEmpty()){
                    bundle.putString("key", keySecondBreakfast);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keySnack.isEmpty()) {
                    bundle.putString("key", keySnack);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void dayClicked(int position) {

        textViewBreakfast.setText("");
        textViewDinner.setText("");
        textViewSupper.setText("");
        textViewSecondBreakfast.setText("");
        textViewSnack.setText("");

        imageViewBreakfast.setImageResource(0);
        imageViewDinner.setImageResource(0);
        imageViewSupper.setImageResource(0);
        imageViewSecondBreakfast.setImageResource(0);
        imageViewSnack.setImageResource(0);

        breakfast.setVisibility(View.VISIBLE);
        dinner.setVisibility(View.VISIBLE);
        supper.setVisibility(View.VISIBLE);
        secondBreakfast.setVisibility(View.VISIBLE);
        snack.setVisibility(View.VISIBLE);


        if (position == 0) {
            date.setText(day1);
        } else if (position == 1) {
            date.setText(day2);
        } else if (position == 2) {
            date.setText(day3);
        } else {
            date.setText(day4);
        }


        databaseReferenceBreakfast = FirebaseDatabase.getInstance().getReference("Planner").child(uid)
                .child("day" + (position + 1)).child("breakfast");
        databaseReferenceBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keyBreakfast = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keyBreakfast);
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
                } else {
                    breakfast.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceDinner = FirebaseDatabase.getInstance().getReference("Planner").child(uid)
                .child("day" + (position + 1)).child("dinner");
        databaseReferenceDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keyDinner = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keyDinner);
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
                } else {
                    dinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceSupper = FirebaseDatabase.getInstance().getReference("Planner").child(uid)
                .child("day" + (position + 1)).child("supper");
        databaseReferenceSupper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keySupper = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keySupper);
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
                } else {
                    supper.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceSecondBreakfast = FirebaseDatabase.getInstance().getReference("Planner").child(uid)
                .child("day" + (position + 1)).child("secondBreakfast");
        databaseReferenceSecondBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keySecondBreakfast = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keySecondBreakfast);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSecondBreakfast.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewSecondBreakfast);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    secondBreakfast.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceSnack = FirebaseDatabase.getInstance().getReference("Planner").child(uid)
                .child("day" + (position + 1)).child("snack");
        databaseReferenceSnack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keySnack = snapshot.child("key").getValue().toString();
                    recipeDatabaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(keySnack);
                    recipeDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            textViewSnack.setText(name);
                            Picasso.with(ShowPlan.this)
                                    .load(snapshot.child("imageUrl").getValue().toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(imageViewSnack);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    snack.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void setVisibility() {
        recyclerView.setVisibility(View.INVISIBLE);
        textViewBreakfast.setVisibility(View.INVISIBLE);
        textViewDinner.setVisibility(View.INVISIBLE);
        textViewSupper.setVisibility(View.INVISIBLE);
        textViewSecondBreakfast.setVisibility(View.INVISIBLE);
        textViewSnack.setVisibility(View.INVISIBLE);
        breakfast.setVisibility(View.INVISIBLE);
        dinner.setVisibility(View.INVISIBLE);
        supper.setVisibility(View.INVISIBLE);
        secondBreakfast.setVisibility(View.INVISIBLE);
        snack.setVisibility(View.INVISIBLE);
        textViewBreakfastTitle.setVisibility(View.INVISIBLE);
        textViewDinnerTitle.setVisibility(View.INVISIBLE);
        textViewSupperTitle.setVisibility(View.INVISIBLE);
        textViewSecondBreakfastTitle.setVisibility(View.INVISIBLE);
        textViewSnackTitle.setVisibility(View.INVISIBLE);
        imageViewBreakfast.setVisibility(View.INVISIBLE);
        imageViewDinner.setVisibility(View.INVISIBLE);
        imageViewSupper.setVisibility(View.INVISIBLE);
        imageViewSecondBreakfast.setVisibility(View.INVISIBLE);
        imageViewSnack.setVisibility(View.INVISIBLE);
    }

}