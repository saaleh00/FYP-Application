package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AppointmentConfirmationFragment extends Fragment {

    private AppointmentModel appointmentModel;

    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_DOCTOR = "doctor";


    private String date;
    private String time;
    private String doctor;


    private TextView dateConfirmationTextView;
    private TextView timeConfirmationTextView;
    private TextView doctorConfirmationTextView;
    private Button confirmConfirmationButton;

    AppointmentDAO appointmentDAO;


    public AppointmentConfirmationFragment() {
        // Required empty public constructor
    }

    public static AppointmentConfirmationFragment newInstance(String date, String time, String doctor) {
        AppointmentConfirmationFragment fragment = new AppointmentConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_TIME, time);
        args.putString(ARG_DOCTOR, doctor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = getArguments().getString(ARG_DATE);
            time = getArguments().getString(ARG_DOCTOR);
            doctor = getArguments().getString(ARG_TIME);
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

        confirmConfirmationButton.setOnClickListener(v -> {
            appointmentModel = new AppointmentModel(date, time, doctor);

            appointmentDAO.insert(appointmentModel).addOnSuccessListener(success ->
            {
                Toast.makeText(getActivity(), "Appointment Successfully Booked", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(error ->
            {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            });
        });

        return view;
    }
}