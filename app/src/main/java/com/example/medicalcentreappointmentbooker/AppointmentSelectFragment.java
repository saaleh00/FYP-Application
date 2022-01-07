package com.example.medicalcentreappointmentbooker;

import android.app.DatePickerDialog;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class AppointmentSelectFragment extends Fragment{

    private static final String ARG_DOCTOR_NAME = "doctorName";

    private String doctorName;

    private TextView appointmentSelectDoctorName;
    private TextView dateText;
    private Button datePickerButton;

    public AppointmentSelectFragment() {
        // Required empty public constructor
    }

    public static AppointmentSelectFragment newInstance(String doctorName) {
        AppointmentSelectFragment fragment = new AppointmentSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCTOR_NAME, doctorName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorName = getArguments().getString(ARG_DOCTOR_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_select, container, false);

        dateText = view.findViewById(R.id.dateText);

        datePickerButton = view.findViewById(R.id.datePickerButton);
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                dateText.setText(currentDateString);
            }
        };
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int Year = calendar.get(Calendar.YEAR);
                int Month = calendar.get(Calendar.MONTH);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, Year, Month, Day);
                dateDialog.show();
            }
        });

        appointmentSelectDoctorName = view.findViewById(R.id.appointmentSelectDoctorName);
        appointmentSelectDoctorName.setText(doctorName);

        return view;
    }
}