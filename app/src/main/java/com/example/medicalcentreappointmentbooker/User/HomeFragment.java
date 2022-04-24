package com.example.medicalcentreappointmentbooker.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalcentreappointmentbooker.Model.User;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;

    private Button bookingActivityButton, bookedAppointmentsButton, userProfileButton, seeChatButton;

    private DoctorSelectFragment doctorSelectFragment;
    private AppointmentBookedFragment appointmentBookedFragment;
    private UserProfilePageFragment userProfilePageFragment;
    private UserSeeChatFragment seeChatFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Appointment Booker");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        Date signUpDate = new Date(user.getMetadata().getCreationTimestamp());

        Log.i("tag", signUpDate.toString());

        final TextView userNameTextView = view.findViewById(R.id.homeUserName);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String userName = userProfile.name;
                    userNameTextView.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        bookingActivityButton = view.findViewById(R.id.bookingActivityButton);
        bookingActivityButton.setOnClickListener(this);

        bookedAppointmentsButton = view.findViewById(R.id.bookedAppointmentsButton);
        bookedAppointmentsButton.setOnClickListener(this);

        userProfileButton = view.findViewById(R.id.homeUserProfileButton);
        userProfileButton.setOnClickListener(this);

        seeChatButton = view.findViewById(R.id.seeChatButton);
        seeChatButton.setOnClickListener(this);


        doctorSelectFragment = new DoctorSelectFragment();
        appointmentBookedFragment = new AppointmentBookedFragment();
        userProfilePageFragment = new UserProfilePageFragment();
        seeChatFragment = new UserSeeChatFragment();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bookingActivityButton:
                openFragment(doctorSelectFragment);
                break;
            case R.id.bookedAppointmentsButton:
                openFragment(appointmentBookedFragment);
                break;
            case R.id.homeUserProfileButton:
                openFragment(userProfilePageFragment);
                break;
            case R.id.seeChatButton:
                openFragment(seeChatFragment);
                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }
}