package com.example.myapp.Planner;

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

import java.util.ArrayList;

public class AddPlan extends AppCompatActivity {

    private Spinner spinnerDays;
    private CardView day1, day2, day3, day4;
    private Button savePlan;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        spinnerDays = findViewById(R.id.spinnerNumberOfDays);
        day1 = findViewById(R.id.cardViewDay1);
        day2 = findViewById(R.id.cardViewDay2);
        day3 = findViewById(R.id.cardViewDay3);
        day4 = findViewById(R.id.cardViewDay4);
        savePlan = findViewById(R.id.buttonSavePlan);

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


        final ArrayList<String> days = new ArrayList<>();
        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        break;
                    case 1:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.INVISIBLE);
                        day4.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.VISIBLE);
                        day4.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        day1.setVisibility(View.VISIBLE);
                        day2.setVisibility(View.VISIBLE);
                        day3.setVisibility(View.VISIBLE);
                        day4.setVisibility(View.VISIBLE);
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