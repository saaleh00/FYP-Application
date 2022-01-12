package com.example.medicalcentreappointmentbooker;

import android.app.DatePickerDialog;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AppointmentSelectFragment extends Fragment implements TimeSlotAdapter.ItemClickListener{

    private static final String ARG_DOCTOR_NAME = "doctorName";
    private static final String ARG_SELECT_DATE = "selectedDate";

    private TimeSlotAdapter timeSlotAdapter;
    private AppointmentConfirmationFragment appointmentConfirmationFragment;

    private String doctorName;
    private String selectedDate;

    private RecyclerView timeSlotRecyclerView;

    private ArrayList<String> timePeriodList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> timeSlotList = new HashMap<>();

    private TextView appointmentSelectDoctorName;
    private TextView dateText;
    private TextView timeTextView;

    public AppointmentSelectFragment() {
        // Required empty public constructor
    }

    public static AppointmentSelectFragment newInstance(String doctorName, String selectedDate) {
        AppointmentSelectFragment fragment = new AppointmentSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCTOR_NAME, doctorName);
        args.putString(ARG_SELECT_DATE, selectedDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Select Time");
        if (getArguments() != null) {
            doctorName = getArguments().getString(ARG_DOCTOR_NAME);
            selectedDate = getArguments().getString(ARG_SELECT_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_select, container, false);
        createTimeLists();

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(selectedDate);

        appointmentSelectDoctorName = view.findViewById(R.id.appointmentSelectDoctorName);
        appointmentSelectDoctorName.setText(doctorName);

        timeTextView = view.findViewById(R.id.timeTextView);

        timeSlotRecyclerView = view.findViewById(R.id.timeSlotRecyclerView);

        timeSlotAdapter = new TimeSlotAdapter(getActivity(), timePeriodList, timeSlotList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        timeSlotRecyclerView.setLayoutManager(gridLayoutManager);
        timeSlotAdapter.setLayoutManager(gridLayoutManager);

        timeSlotAdapter.shouldShowHeadersForEmptySections(false);

        timeSlotRecyclerView.setAdapter(timeSlotAdapter);

        return view;
    }

    public void createTimeLists(){
        timePeriodList.add("Morning");
        timePeriodList.add("Afternoon");
        timePeriodList.add("Evening");

        ArrayList<String> timeList = new ArrayList<>();
        //Morning
        timeList.add("09:00 AM");
        timeList.add("09:30 AM");
        timeList.add("10:00 AM");
        timeList.add("10:30 AM");
        timeList.add("11:00 AM");
        timeList.add("11:30 AM");
        timeSlotList.put(timePeriodList.get(0), timeList);
        //Afternoon
        timeList = new ArrayList<>();
        timeList.add("12:00 PM");
        timeList.add("12:30 PM");
        timeList.add("13:00 PM");
        timeList.add("13:30 PM");
        timeList.add("14:00 PM");
        timeList.add("14:30 PM");
        timeList.add("15:00 PM");
        timeList.add("15:30 PM");
        timeList.add("16:00 PM");
        timeSlotList.put(timePeriodList.get(1), timeList);
        //Evening
        timeList = new ArrayList<>();
        timeList.add("16:30 PM");
        timeList.add("17:00 PM");
        timeList.add("17:30 PM");
        timeList.add("18:00 PM");
        timeSlotList.put(timePeriodList.get(2), timeList);
    }

    @Override
    public void onItemClick(String timeItem) {
        appointmentConfirmationFragment = AppointmentConfirmationFragment.newInstance(selectedDate, timeItem, doctorName);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, appointmentConfirmationFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}