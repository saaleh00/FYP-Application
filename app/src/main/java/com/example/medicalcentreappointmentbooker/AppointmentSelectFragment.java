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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AppointmentSelectFragment extends Fragment implements TimeSlotAdapter.ItemClickListener {


    private static final String ARG_DOCTOR_NAME = "doctorName";
    private static final String ARG_SELECT_DATE = "selectedDate";

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(AppointmentModel.class.getSimpleName());


    private TimeSlotAdapter timeSlotAdapter;
    private AppointmentConfirmationFragment appointmentConfirmationFragment;


    private String doctorName, selectedDate;

    private GridView appointmentSelectGridView;
    private ArrayList<String> unavailableTimeSlots = new ArrayList<>();
    private ArrayList<TimeSlot> timeSlotList;
    private ArrayList<String> timeList = new ArrayList<>();

    private TextView appointmentSelectDoctorName, dateText, timeTextView;

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

        timeSlotList = new ArrayList<>();

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(selectedDate);

        appointmentSelectDoctorName = view.findViewById(R.id.appointmentSelectDoctorName);
        appointmentSelectDoctorName.setText(doctorName);

        timeTextView = view.findViewById(R.id.timeTextView);

        appointmentSelectGridView = view.findViewById(R.id.appointmentSelectGridView);

        checkTimeSlots(new Callback() {
            @Override
            public void onComplete(ArrayList<TimeSlot> unavailableTimeSlots) {
                timeSlotAdapter = new TimeSlotAdapter(getActivity(), timeSlotList, AppointmentSelectFragment.this);
                appointmentSelectGridView.setAdapter(timeSlotAdapter);
            }
        });

        return view;
    }

    public void checkTimeSlots(Callback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.hasChild("time")) {
                        if ((snapshot.child("date").getValue().toString()).equals(selectedDate) &&
                                (snapshot.child("doctor").getValue().toString()).equals(doctorName)) {
                            //callback function
                            String time = snapshot.child("time").getValue().toString();
                            unavailableTimeSlots.add(time);
                        }
                    }
                }
                //Morning
                timeList.add("09:00 AM");
                timeList.add("09:30 AM");
                timeList.add("10:00 AM");
                timeList.add("10:30 AM");
                timeList.add("11:00 AM");
                timeList.add("11:30 AM");

                //Afternoon
                timeList.add("12:00 PM");
                timeList.add("12:30 PM");
                timeList.add("13:00 PM");
                timeList.add("13:30 PM");
                timeList.add("14:00 PM");
                timeList.add("14:30 PM");
                timeList.add("15:00 PM");
                timeList.add("15:30 PM");
                timeList.add("16:00 PM");

                //Evening
                timeList.add("16:30 PM");
                timeList.add("17:00 PM");
                timeList.add("17:30 PM");
                timeList.add("18:00 PM");

                for (String time :
                        timeList) {
                    TimeSlot timeSlot;
                    if (unavailableTimeSlots.contains(time)) {
                        timeSlot = new TimeSlot(time, false);
                    } else {
                        timeSlot = new TimeSlot(time, true);
                    }
                    timeSlotList.add(timeSlot);
                    callback.onComplete(timeSlotList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Executive Order", "The read failed: " + databaseError.getDetails());
            }
        });
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