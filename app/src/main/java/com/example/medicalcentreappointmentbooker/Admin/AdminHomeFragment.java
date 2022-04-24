package com.example.medicalcentreappointmentbooker.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


public class AdminHomeFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;

    private Button addDoctorButton, userPageButton, doctorPageButton;

    private CreateDoctorFragment createDoctorFragment;
    private AdminUserFragment adminUserFragment;
    private AdminDoctorFragment adminDoctorFragment;

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
        ((AdminActivity) getActivity()).setActionBarTitle("Admin Page");
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

        createDoctorFragment = new CreateDoctorFragment();
        adminUserFragment = new AdminUserFragment();
        adminDoctorFragment = new AdminDoctorFragment();

        addDoctorButton = view.findViewById(R.id.adminAddDoctor);
        addDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(createDoctorFragment);
            }
        });

        userPageButton = view.findViewById(R.id.adminUserButton);
        userPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(adminUserFragment);
            }
        });

        doctorPageButton = view.findViewById(R.id.adminDoctorButton);
        doctorPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(adminDoctorFragment);
            }
        });

        return view;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.adminContainer, fragment);
        fragmentTransaction.commit();
    }
}