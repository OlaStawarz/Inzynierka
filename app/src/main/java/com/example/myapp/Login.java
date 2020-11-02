package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Planner.Planner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    public Button login;
    public TextView register, forgetPassword;
    private String currentEmail, currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextTextEmailAddressLogin);
        password = findViewById(R.id.editTextTextPasswordLogin);
        login = findViewById(R.id.buttonLogin);
        register = findViewById(R.id.textViewRegister);
        forgetPassword = findViewById(R.id.textViewForgetPassword);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentEmail = email.getText().toString();
                currentPassword = password.getText().toString();
                if (currentPassword.isEmpty()) {
                    password.setError("Wprowadź hasło.");
                    return;
                }
                if (currentEmail.isEmpty()) {
                    email.setError("Wprowadź email.");
                    return;
                }

                mAuth.signInWithEmailAndPassword(currentEmail, currentPassword)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    updateUI();
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        email.setError("Podany email nie istnieje.");
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        password.setError("Wprowadzono niepoprawne hasło.");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Login.this, Register.class);
                startActivity(intent1);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent2);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI();
    }

    private void updateUI() {
        Intent intent = new Intent(Login.this, Planner.class);
        startActivity(intent);
    }

}