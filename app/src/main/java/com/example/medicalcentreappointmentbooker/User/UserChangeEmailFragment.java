package com.example.medicalcentreappointmentbooker.User;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medicalcentreappointmentbooker.Callback.ChangeEmailCallback;
import com.example.medicalcentreappointmentbooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class UserChangeEmailFragment extends Fragment {

    private EditText changeEmailInput, changeCurrentPasswordInput, changeNewPasswordInput;
    private Button changeEmailButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Users");

    private HomeFragment homeFragment;


    public UserChangeEmailFragment() {
        // Required empty public constructor
    }


    public static UserChangeEmailFragment newInstance() {
        UserChangeEmailFragment fragment = new UserChangeEmailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_change_email, container, false);

        changeEmailInput = view.findViewById(R.id.changeEmailInput);
        changeCurrentPasswordInput = view.findViewById(R.id.changeCurrentPasswordInput);
        changeNewPasswordInput = view.findViewById(R.id.changeNewPasswordInput);

        changeEmailButton = view.findViewById(R.id.changeEmailButton);

        progressBar = view.findViewById(R.id.changeEmailProgressBar);

        mAuth = FirebaseAuth.getInstance();

        homeFragment = new HomeFragment();

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmailOrPassword();
            }
        });

        return view;
    }

    private void changeEmailOrPassword(){
        String emailInput = changeEmailInput.getText().toString().trim();
        String currentPasswordInput = changeCurrentPasswordInput.getText().toString().trim();
        String newPasswordInput = changeNewPasswordInput.getText().toString().trim();

        if (currentPasswordInput.isEmpty()){
            changeCurrentPasswordInput.setError("Current Password is required");
            changeCurrentPasswordInput.requestFocus();
            return;
        }

        if (emailInput.isEmpty()){
            if (newPasswordInput.isEmpty()){
                changeEmailInput.setError("You are not changing anything");
                changeNewPasswordInput.setError("You are not changing anything");
                changeEmailInput.requestFocus();
                changeNewPasswordInput.requestFocus();
                return;
            } else{
                progressBar.setVisibility(View.VISIBLE);
                changePassword(new ChangeEmailCallback() {
                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        triggerRebirth(getActivity());
                    }
                });
            }
        }else{
            if (newPasswordInput.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);
                changeEmail(new ChangeEmailCallback() {
                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        triggerRebirth(getActivity());
                    }
                });
            }else{
                progressBar.setVisibility(View.VISIBLE);
                changeEmail(new ChangeEmailCallback() {
                    @Override
                    public void onComplete() {
                        changePassword(new ChangeEmailCallback() {
                            @Override
                            public void onComplete() {
                                progressBar.setVisibility(View.GONE);
                                triggerRebirth(getActivity());
                            }
                        });
                    }
                });
            }
        }
    }

    private void changeEmail(ChangeEmailCallback changeEmailCallback){
        String emailInput = changeEmailInput.getText().toString().trim();
        String currentPasswordInput = changeCurrentPasswordInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            changeEmailInput.setError("Please provide valid email");
            changeEmailInput.requestFocus();
            return;
        }
        if (emailInput.equals(firebaseUser.getEmail())){
            changeEmailInput.setError("Email is the same");
            changeEmailInput.requestFocus();
            return;
        }
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), currentPasswordInput);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updateEmail(emailInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //update email in database
                                Map<String, Object> emailDetails = new HashMap<>();
                                emailDetails.put("email", emailInput);
                                databaseReference.child(firebaseUser.getUid()).updateChildren(emailDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            changeEmailCallback.onComplete();
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    changeCurrentPasswordInput.setError("Incorrect Password");
                    changeCurrentPasswordInput.requestFocus();
                    return;
                }
            }
        });
    }

    private void changePassword(ChangeEmailCallback changeEmailCallback){
        String currentPasswordInput = changeCurrentPasswordInput.getText().toString().trim();
        String newPasswordInput = changeNewPasswordInput.getText().toString().trim();
        if (newPasswordInput.length() < 6) {
            changeNewPasswordInput.setError("Min. password length should be 6 characters");
            changeNewPasswordInput.requestFocus();
            return;
        }
        if (currentPasswordInput.equals(newPasswordInput)){
            changeNewPasswordInput.setError("Passwords are the same");
            changeNewPasswordInput.requestFocus();
            return;
        }
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), currentPasswordInput);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updatePassword(newPasswordInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                changeEmailCallback.onComplete();
                                return;
                            } else {
                                Toast.makeText(getActivity(), "Unable to change details", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    changeCurrentPasswordInput.setError("Incorrect Password");
                    changeCurrentPasswordInput.requestFocus();
                    return;
//                    Toast.makeText(getActivity(), "Unable to change details", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}