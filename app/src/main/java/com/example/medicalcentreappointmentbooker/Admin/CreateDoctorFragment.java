package com.example.medicalcentreappointmentbooker.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medicalcentreappointmentbooker.Model.UserStatistic;
import com.example.medicalcentreappointmentbooker.R;
import com.example.medicalcentreappointmentbooker.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class CreateDoctorFragment extends Fragment {

    private EditText registerNameInput, registerAgeInput, registerEmailInput, registerPasswordInput;
    private Button registerButton;
    private ProgressBar registerProgressBar;

    private FirebaseAuth mAuth;


    public CreateDoctorFragment() {
        // Required empty public constructor
    }

    public static CreateDoctorFragment newInstance() {
        CreateDoctorFragment fragment = new CreateDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_doctor, container, false);

        mAuth = FirebaseAuth.getInstance();


        registerButton = view.findViewById(R.id.createDoctorRegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDoctor();
            }
        });

        registerNameInput = view.findViewById(R.id.createDoctorRegisterNameInput);
        registerAgeInput = view.findViewById(R.id.createDoctorRegisterAgeInput);
        registerEmailInput = view.findViewById(R.id.createDoctorRegisterEmailInput);
        registerPasswordInput = view.findViewById(R.id.createDoctorRegisterPasswordInput);

        registerProgressBar = view.findViewById(R.id.createDoctorRegisterProgressBar);


        return view;
    }

    private void registerDoctor() {
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
                            User user = new User(nameInput, ageInput, emailInput, "doctor");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        UserStatistic userStats = new UserStatistic(nameInput, 0, 0, System.currentTimeMillis());

                                        FirebaseDatabase.getInstance().getReference("Statistics").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userStats);

                                        Toast.makeText(getActivity(), "User successfully registered", Toast.LENGTH_LONG).show();

                                        NavController navController = Navigation.findNavController(getView());
                                        navController.popBackStack();
                                        navController.navigate(R.id.createDoctorFragment);
                                    } else{
                                        Toast.makeText(getActivity(), "Failed to register user", Toast.LENGTH_LONG).show();
                                    }
                                    registerProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }else{
                            Toast.makeText(getActivity(), "Failed to register user", Toast.LENGTH_LONG).show();
                            registerProgressBar.setVisibility(View.GONE);
                        }
                    }
                });



    }
}