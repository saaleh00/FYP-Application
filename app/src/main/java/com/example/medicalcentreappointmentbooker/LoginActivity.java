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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView loginRegister, loginForgotPassword;
    private EditText loginEmailInput, loginPasswordInput;
    private Button loginButton;
    private ProgressBar loginProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        loginForgotPassword.setOnClickListener(this);

        loginRegister = findViewById(R.id.loginRegister);
        loginRegister.setOnClickListener(this);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        loginEmailInput = findViewById(R.id.loginEmailInput);
        loginPasswordInput = findViewById(R.id.loginPasswordInput);

        loginProgressBar = findViewById(R.id.loginProgressBar);
    }

    private void userLogin() {
        String emailInput = loginEmailInput.getText().toString().trim();
        String passwordInput = loginPasswordInput.getText().toString().trim();

        if (emailInput.isEmpty()) {
            loginEmailInput.setError("Email is required");
            loginEmailInput.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            loginEmailInput.setError("Please provide valid email");
            loginEmailInput.requestFocus();
            return;
        }

        if (passwordInput.isEmpty()) {
            loginPasswordInput.setError("Password is required");
            loginPasswordInput.requestFocus();
            return;
        }

        if (passwordInput.length() < 6) {
            loginPasswordInput.setError("Min. password length should be 6 characters");
            loginPasswordInput.requestFocus();
            return;
        }

        loginProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Email verification is needed to login", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login unsuccessful", Toast.LENGTH_LONG).show();
                }
                loginProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginRegister:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;
            case R.id.loginButton:
                userLogin();
                break;
            case R.id.loginForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
        }
    }
}