package com.example.medicalcentreappointmentbooker.Admin;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalcentreappointmentbooker.Callback.UserStatisticCallback;
import com.example.medicalcentreappointmentbooker.Model.UserStatistic;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AdminSeeUserFragment extends Fragment {

    private static final String ARG_USER_NAME = "userName";
    private static final String ARG_USER_ID = "userID";

    private String userName;
    private String userID;

    private TextView userBanner, userHeight, userWeight, userBlood, userAge, userDate, userAppointments, userCancellations;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public AdminSeeUserFragment() {
        // Required empty public constructor
    }

    public static AdminSeeUserFragment newInstance(String userName, String userID) {
        AdminSeeUserFragment fragment = new AdminSeeUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_USER_ID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_USER_NAME);
            userID = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_see_user, container, false);

        userBanner = view.findViewById(R.id.statUserName);
        userHeight = view.findViewById(R.id.statUserHeight);
        userWeight = view.findViewById(R.id.statUserWeight);
        userBlood = view.findViewById(R.id.statUserBlood);
        userAge = view.findViewById(R.id.statUserAge);
        userDate = view.findViewById(R.id.statUserDate);
        userAppointments = view.findViewById(R.id.statUserAppointments);
        userCancellations = view.findViewById(R.id.statUserCancellations);

        userBanner.setText(userName);
        loadData(new UserStatisticCallback() {
            @Override
            public void onComplete(UserStatistic userStatistic) {
                Date creationDate = new Date(userStatistic.getTimeStamp());
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                String date = f.format(creationDate);

                userHeight.setText(userStatistic.getHeight() + " CM");
                userWeight.setText(userStatistic.getWeight() + " KG");
                userBlood.setText(userStatistic.getBloodType());
                userAge.setText(userStatistic.getAge());
                userDate.setText(date);
                userAppointments.setText(Integer.toString(userStatistic.getNoOfAppointments()));
                userCancellations.setText(Integer.toString(userStatistic.getNoOfCancellations()));
            }
        });


        return view;
    }

    private void loadData(UserStatisticCallback userStatisticCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int noOfAppointments, noOfCancellations;
                double height, weight;
                String bloodType, age;
                long timeStamp;

                DataSnapshot users = snapshot.child("Users").child(userID);
                DataSnapshot statistics = snapshot.child("Statistics").child(userID);

                if (users.hasChild("height")) {
                    height = users.child("height").getValue(Double.class);
                }
                else {
                    height = 0.0;
                }
                if (users.hasChild("weight"))
                    weight = users.child("weight").getValue(Double.class);
                else
                    weight = 0.0;
                if (users.hasChild("bloodType"))
                    bloodType = users.child("bloodType").getValue(String.class);
                else
                    bloodType = "UNKNOWN";
                if (users.hasChild("age"))
                    age = users.child("age").getValue(String.class);
                else
                    age = "UNKNOWN";
                if (statistics.hasChild("timeStamp"))
                    timeStamp = statistics.child("timeStamp").getValue(long.class);
                else
                    timeStamp = 0;
                if (statistics.hasChild("noOfAppointments"))
                    noOfAppointments = statistics.child("noOfAppointments").getValue(Integer.class);
                else
                    noOfAppointments = 0;
                if (statistics.hasChild("noOfCancellations"))
                    noOfCancellations = statistics.child("noOfCancellations").getValue(Integer.class);
                else
                    noOfCancellations = 0;

                UserStatistic userStatistic = new UserStatistic(userName, age, bloodType, userID, height, weight, noOfAppointments, noOfCancellations, timeStamp);
                userStatisticCallback.onComplete(userStatistic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }
}