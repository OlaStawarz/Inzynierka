package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        registerButton = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailEditText.getText().toString();
                String userPassword = passwordEditText.getText().toString();
                if (userEmail.isEmpty()) {
                    emailEditText.setError("Wprowadź email.");
                    return;
                }
                if (userPassword.isEmpty()) {
                    passwordEditText.setError("Wprowadź hasło.");
                    return;
                }
                registerUser(userEmail, userPassword);

            }
        });

    }

    public void registerUser (final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthWeakPasswordException e) {
                                passwordEditText.setError("Hasło musi zawierać co najmniej 6 znaków.");
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                emailEditText.setError("Niepoprawny adres email.");
                            } catch (FirebaseAuthUserCollisionException e) {
                                emailEditText.setError("Użytkownik o podanym adresie email już istnieje.");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void updateUI() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }
}