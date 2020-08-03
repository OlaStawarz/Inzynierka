package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ImageView welcomeImage;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello!");

        welcomeImage = findViewById(R.id.imageViewWelcomeScreen);
        welcomeText = findViewById(R.id.textViewWelcomeScreen);

        YoYo.with(Techniques.BounceInDown)
                .duration(2000)
                .playOn(welcomeImage);

        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .playOn(welcomeText);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Planner.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}