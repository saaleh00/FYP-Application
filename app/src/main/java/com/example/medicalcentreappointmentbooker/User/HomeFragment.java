package com.example.medicalcentreappointmentbooker.User;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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


public class HomeFragment extends Fragment implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;

    private Button bookingActivityButton, bookedAppointmentsButton, userProfileButton, seeChatButton;
    private TextView userNameTextView;
    private ImageView logout;

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
//        ((MainActivity) getActivity()).setActionBarTitle("Appointment Booker");
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


        userNameTextView = view.findViewById(R.id.homeUserName);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
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

        logout = view.findViewById(R.id.userHomeLogout);
        logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bookingActivityButton:
                Navigation.findNavController(v).navigate(R.id.homeToDoctorSelect);
                break;
            case R.id.bookedAppointmentsButton:
                Navigation.findNavController(v).navigate(R.id.homeToAppointmentsBooked);
                break;
            case R.id.homeUserProfileButton:
                Navigation.findNavController(v).navigate(R.id.homeToProfile);
                break;
            case R.id.seeChatButton:
                Navigation.findNavController(v).navigate(R.id.homeToSeeChat);
                break;
            case R.id.userHomeLogout:
                logoutDialog();
                break;
        }
    }

    public void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                triggerRebirth(getActivity());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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