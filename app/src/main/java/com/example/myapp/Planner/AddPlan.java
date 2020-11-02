package com.example.myapp.Planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPlan extends AppCompatActivity {

    private Spinner spinnerDays;
    private CardView day1CardView, day2CardView, day3CardView, day4CardView;
    private Button savePlan;
    private FloatingActionButton isDay1Button, isDay2Button, isDay3Button, isDay4Button;
    private boolean isDay1 = false, isDay2 = false, isDay3 = false, isDay4 = false;
    private Intent intent;
    private Bundle bundle;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    String uid;
    private String day1, day2, day3, day4;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        spinnerDays = findViewById(R.id.spinnerNumberOfDays);
        day1CardView = findViewById(R.id.cardViewDay1);
        day2CardView = findViewById(R.id.cardViewDay2);
        day3CardView = findViewById(R.id.cardViewDay3);
        day4CardView = findViewById(R.id.cardViewDay4);
        isDay1Button = findViewById(R.id.floatingActionButtonIsAddedDay1);
        isDay2Button = findViewById(R.id.floatingActionButtonIsAddedDay2);
        isDay3Button = findViewById(R.id.floatingActionButtonIsAddedDay3);
        isDay4Button = findViewById(R.id.floatingActionButtonIsAddedDay4);
        savePlan = findViewById(R.id.buttonSavePlan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Planner").child(uid);

        getDate();
        addNumbersToSpinner(spinnerDays);
        setVisibility();
        setButtonBackground();

        intent = new Intent(AddPlan.this, PlannerDay.class);
        bundle = new Bundle();

        day1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDay1) {
                    bundle.putString("day", "day1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        day2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDay2) {
                    bundle.putString("day", "day2");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        day3CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDay3) {
                    bundle.putString("day", "day3");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        day4CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDay4) {
                    bundle.putString("day", "day4");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getDate () {
        day1 =  translate(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date()))
                + ", " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        Date nextDate2 = c.getTime();
        day2 = translate(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(nextDate2.getTime()))
                + ", " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(nextDate2);
        c.setTime(new Date());
        c.add(Calendar.DATE, 2);
        Date nextDate3 = c.getTime();
        day3 = translate(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(nextDate3.getTime()))
                + ", " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(nextDate3);
        c.setTime(new Date());
        c.add(Calendar.DATE, 3);
        Date nextDate4 = c.getTime();
        day4 = translate(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(nextDate4.getTime()))
                + ", " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(nextDate4);
    }

    private String translate(String dayInEnglish) {

        String day = "";
        switch (dayInEnglish.toUpperCase()){
            case "MONDAY":
                day = "PONIEDZIAŁEK";
                break;
            case "TUESDAY":
                day = "WTOREK";
                break;
            case "WEDNESDAY":
                day = "ŚRODA";
                break;
            case "THURSDAY":
                day = "CZWARTEK";
                break;
            case "FRIDAY":
                day = "PIĄTEK";
                break;
            case "SATURDAY":
                day = "SOBOTA";
                break;
            case "SUNDAY":
                day = "NIEDZIELA";
                break;

        }

        return day;
    }

    private void setButtonBackground() {
        databaseReference.child("day1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isDay1Button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_check_24));
                    isDay1 = true;
                    databaseReference.child("day1").child("date").setValue(day1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("day2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isDay2Button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_check_24));
                    isDay2 = true;
                    databaseReference.child("day2").child("date").setValue(day2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("day3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isDay3Button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_check_24));
                    databaseReference.child("day3").child("date").setValue(day3);
                    isDay3 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("day4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isDay4Button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_check_24));
                    databaseReference.child("day4").child("date").setValue(day4);
                    isDay4 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVisibility () {

        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        day1CardView.setVisibility(View.VISIBLE);
                        day2CardView.setVisibility(View.INVISIBLE);
                        day3CardView.setVisibility(View.INVISIBLE);
                        day4CardView.setVisibility(View.INVISIBLE);
                        isDay1Button.setVisibility(View.VISIBLE);
                        isDay2Button.setVisibility(View.INVISIBLE);
                        isDay3Button.setVisibility(View.INVISIBLE);
                        isDay4Button.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        day1CardView.setVisibility(View.VISIBLE);
                        day2CardView.setVisibility(View.VISIBLE);
                        day3CardView.setVisibility(View.INVISIBLE);
                        day4CardView.setVisibility(View.INVISIBLE);
                        isDay1Button.setVisibility(View.VISIBLE);
                        isDay2Button.setVisibility(View.VISIBLE);
                        isDay3Button.setVisibility(View.INVISIBLE);
                        isDay4Button.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        day1CardView.setVisibility(View.VISIBLE);
                        day2CardView.setVisibility(View.VISIBLE);
                        day3CardView.setVisibility(View.VISIBLE);
                        day4CardView.setVisibility(View.INVISIBLE);
                        isDay1Button.setVisibility(View.VISIBLE);
                        isDay2Button.setVisibility(View.VISIBLE);
                        isDay3Button.setVisibility(View.VISIBLE);
                        isDay4Button.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        day1CardView.setVisibility(View.VISIBLE);
                        day2CardView.setVisibility(View.VISIBLE);
                        day3CardView.setVisibility(View.VISIBLE);
                        day4CardView.setVisibility(View.VISIBLE);
                        isDay1Button.setVisibility(View.VISIBLE);
                        isDay2Button.setVisibility(View.VISIBLE);
                        isDay3Button.setVisibility(View.VISIBLE);
                        isDay4Button.setVisibility(View.VISIBLE);
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
        spinner.setAdapter(new ArrayAdapter<>(AddPlan.this,
                android.R.layout.simple_spinner_dropdown_item, units));
    }

}