package com.example.medicalcentreappointmentbooker.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalcentreappointmentbooker.R;
import com.example.medicalcentreappointmentbooker.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminHomeFragment extends Fragment implements View.OnClickListener{

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;

    private Button addDoctorButton, userPageButton, doctorPageButton;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    public static AdminHomeFragment newInstance() {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView adminUserName = view.findViewById(R.id.adminUserName);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String userName = userProfile.name;
                    adminUserName.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


        addDoctorButton = view.findViewById(R.id.adminAddDoctor);
        addDoctorButton.setOnClickListener(this);

        userPageButton = view.findViewById(R.id.adminUserButton);
        userPageButton.setOnClickListener(this);

        doctorPageButton = view.findViewById(R.id.adminDoctorButton);
        doctorPageButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adminAddDoctor:
                Navigation.findNavController(v).navigate(R.id.adminHomeToAddDoctor);
                break;
            case R.id.adminUserButton:
                Navigation.findNavController(v).navigate(R.id.adminHomeToUserList);
                break;
            case R.id.adminDoctorButton:
                Navigation.findNavController(v).navigate(R.id.adminHomeToDoctorList);
                break;
        }
    }
}