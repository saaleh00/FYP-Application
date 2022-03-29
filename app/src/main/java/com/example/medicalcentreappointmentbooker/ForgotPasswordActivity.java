package com.example.medicalcentreappointmentbooker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgotPasswordEmailInput;
    private Button forgotPasswordResetButton;
    private ProgressBar forgotPasswordProgressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPasswordEmailInput = findViewById(R.id.forgotPasswordEmailInput);
        forgotPasswordResetButton = findViewById(R.id.forgotPasswordResetButton);
        forgotPasswordProgressBar = findViewById(R.id.forgotPasswordProgressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPasswordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String emailInput = forgotPasswordEmailInput.getText().toString().trim();

        if (emailInput.isEmpty()){
            forgotPasswordEmailInput.setError("Email is required");
            forgotPasswordEmailInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            forgotPasswordEmailInput.setError("Please provide valid email");
            forgotPasswordEmailInput.requestFocus();
            return;
        }

        forgotPasswordProgressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(emailInput).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                    forgotPasswordProgressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Try Again! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}