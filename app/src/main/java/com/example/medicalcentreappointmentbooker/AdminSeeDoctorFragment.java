package com.example.medicalcentreappointmentbooker;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AdminSeeDoctorFragment extends Fragment {

    private static final String ARG_DOCTOR_NAME = "doctorName";
    private static final String ARG_DOCTOR_ID = "doctorID";

    private String doctorName;
    private String doctorID;

    private TextView doctorBanner, doctorAge, doctorDate, doctorAppointments, doctorCancellations;
    private ImageView doctorImage;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public AdminSeeDoctorFragment() {
        // Required empty public constructor
    }

    public static AdminSeeDoctorFragment newInstance(String doctorName, String doctorID) {
        AdminSeeDoctorFragment fragment = new AdminSeeDoctorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCTOR_NAME, doctorName);
        args.putString(ARG_DOCTOR_ID, doctorID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorName = getArguments().getString(ARG_DOCTOR_NAME);
            doctorID = getArguments().getString(ARG_DOCTOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_see_doctor, container, false);

        doctorBanner = view.findViewById(R.id.statDoctorName);
        doctorAge = view.findViewById(R.id.statDoctorAge);
        doctorDate = view.findViewById(R.id.statDoctorDate);
        doctorAppointments = view.findViewById(R.id.statDoctorAppointments);
        doctorCancellations = view.findViewById(R.id.statDoctorCancellations);

        doctorImage = view.findViewById(R.id.statDoctorImage);

        doctorBanner.setText(doctorName);
        loadData(new UserStatisticCallback() {
            @Override
            public void onComplete(UserStatistic userStatistic) {
                Date creationDate = new Date(userStatistic.getTimeStamp());
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                String date = f.format(creationDate);

                doctorAge.setText(userStatistic.getAge());
                doctorDate.setText(date);
                doctorAppointments.setText(Integer.toString(userStatistic.getNoOfAppointments()));
                doctorCancellations.setText(Integer.toString(userStatistic.getNoOfCancellations()));
            }
        });

        return view;
    }

    private void loadData(UserStatisticCallback userStatisticCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int noOfAppointments, noOfCancellations;
                String age;
                long timeStamp;

                DataSnapshot users = snapshot.child("Users").child(doctorID);
                DataSnapshot statistics = snapshot.child("Statistics").child(doctorID);

                //Add user profile pic here and then to userStatistic constructor

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

                UserStatistic userStatistic = new UserStatistic(doctorName, age, doctorID, noOfAppointments, noOfCancellations, timeStamp);
                userStatisticCallback.onComplete(userStatistic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });

    }
}