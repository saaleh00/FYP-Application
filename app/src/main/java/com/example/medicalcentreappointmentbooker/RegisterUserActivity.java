package com.example.medicalcentreappointmentbooker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView registerBanner, registerSubBanner;
    private EditText registerNameInput, registerAgeInput, registerEmailInput, registerPasswordInput;
    private Button registerButton;
    private ProgressBar registerProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerBanner = findViewById(R.id.registerBanner);
        registerBanner.setOnClickListener(this);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        registerNameInput = findViewById(R.id.registerNameInput);
        registerAgeInput = findViewById(R.id.registerAgeInput);
        registerEmailInput = findViewById(R.id.registerEmailInput);
        registerPasswordInput = findViewById(R.id.registerPasswordInput);

        registerProgressBar = findViewById(R.id.registerProgressBar);
    }

    private void registerUser() {
        String nameInput = registerNameInput.getText().toString().trim();
        String ageInput = registerAgeInput.getText().toString().trim();
        String emailInput = registerEmailInput.getText().toString().trim();
        String passwordInput = registerPasswordInput.getText().toString().trim();

        if (nameInput.isEmpty()){
            registerNameInput.setError("Full name is required");
            registerNameInput.requestFocus();
            return;
        }
        if (ageInput.isEmpty()){
            registerAgeInput.setError("Age is required");
            registerAgeInput.requestFocus();
            return;
        }
        if (emailInput.isEmpty()){
            registerEmailInput.setError("Email is required");
            registerEmailInput.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            registerEmailInput.setError("Please provide valid email");
            registerEmailInput.requestFocus();
            return;
        }

        if (passwordInput.isEmpty()){
            registerPasswordInput.setError("Password is required");
            registerPasswordInput.requestFocus();
            return;
        }

        if (passwordInput.length() < 6){
            registerPasswordInput.setError("Min. password length should be 6 characters");
            registerPasswordInput.requestFocus();
            return;
        }
        registerProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            User user = new User(nameInput, ageInput, emailInput);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterUserActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                                    } else{
                                        Toast.makeText(RegisterUserActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                                    }
                                    registerProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUserActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                            registerProgressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBanner:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case  R.id.registerButton:
                registerUser();
                break;
        }
    }


}