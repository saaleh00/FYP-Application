package com.example.medicalcentreappointmentbooker.User;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalcentreappointmentbooker.Model.AppointmentModel;
import com.example.medicalcentreappointmentbooker.Model.User;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;


public class AppointmentConfirmationFragment extends Fragment {

    private AppointmentModel appointmentModel;

    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_DOCTOR = "doctor";
    private static final String ARG_DOCTOR_ID = "doctorID";


    private String date, time, doctor, doctorID, patient, userID;
    private int noOfAppointments, doctorAppointments;


    private TextView dateConfirmationTextView, timeConfirmationTextView, doctorConfirmationTextView;
    private Button confirmConfirmationButton;

    private AppointmentDAO appointmentDAO;

    private DatabaseReference databaseReference;
    private DatabaseReference statisticReference;

    private AppointmentBookedFragment appointmentBookedFragment;

    private FirebaseAuth firebaseAuth;

    public AppointmentConfirmationFragment() {
        // Required empty public constructor
    }

    public static AppointmentConfirmationFragment newInstance(String date, String time, String doctor, String doctorID) {
        AppointmentConfirmationFragment fragment = new AppointmentConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_TIME, time);
        args.putString(ARG_DOCTOR, doctor);
        args.putString(ARG_DOCTOR_ID, doctorID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((MainActivity) getActivity()).setActionBarTitle("Please Confirm");
        if (getArguments() != null) {
            date = getArguments().getString(ARG_DATE);
            time = getArguments().getString(ARG_TIME);
            doctor = getArguments().getString(ARG_DOCTOR);
            doctorID = getArguments().getString(ARG_DOCTOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_confirmation, container, false);

        dateConfirmationTextView = view.findViewById(R.id.dateConfirmationTextView);
        timeConfirmationTextView = view.findViewById(R.id.timeConfirmationTextView);
        doctorConfirmationTextView = view.findViewById(R.id.doctorConfirmationTextView);
        confirmConfirmationButton = view.findViewById(R.id.confirmConfirmationButton);

        dateConfirmationTextView.setText(date);
        timeConfirmationTextView.setText(time);
        doctorConfirmationTextView.setText(doctor);

        appointmentDAO = new AppointmentDAO();

        appointmentBookedFragment = new AppointmentBookedFragment();

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        statisticReference = FirebaseDatabase.getInstance().getReference("Statistics");

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String userName = userProfile.name;
                    patient = userName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        statisticReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("noOfAppointments"))
                    noOfAppointments = snapshot.child("noOfAppointments").getValue(Integer.class);
                else
                    noOfAppointments = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        statisticReference.child(doctorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("noOfAppointments"))
                    doctorAppointments = snapshot.child("noOfAppointments").getValue(Integer.class);
                else
                    doctorAppointments = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });



        confirmConfirmationButton.setOnClickListener(v -> {
            appointmentModel = new AppointmentModel(date, time, doctor, patient, doctorID, userID);

            appointmentDAO.create(appointmentModel).addOnSuccessListener(success ->
            {
                statisticReference.child(userID).child("noOfAppointments").setValue(noOfAppointments+1);
                statisticReference.child(doctorID).child("noOfAppointments").setValue(doctorAppointments+1);
                openFragment(R.id.appointmentConfirmToBooked);
                Toast.makeText(getActivity(), "Appointment Successfully Booked", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(error ->
            {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            });
        });

        return view;
    }

    public void openFragment(int navigationAction) {
        Navigation.findNavController(getView()).navigate(navigationAction);
    }
}