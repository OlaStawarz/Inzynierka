package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText enterEmail;
    public Button sendEmailButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        enterEmail = findViewById(R.id.editTextEmailAddressReset);
        sendEmailButton = findViewById(R.id.buttonSendEmail);
        auth = FirebaseAuth.getInstance();

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(enterEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPassword.this, "Email został poprawnie wysłany.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetPassword.this, "Email nie został poprawnie wysłany.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                finish();
            }
        });
    }
}