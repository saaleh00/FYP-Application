package com.example.medicalcentreappointmentbooker.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.medicalcentreappointmentbooker.Callback.TimeSlotCallback;
import com.example.medicalcentreappointmentbooker.Model.AppointmentModel;
import com.example.medicalcentreappointmentbooker.Model.TimeSlot;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppointmentSelectFragment extends Fragment implements TimeSlotAdapter.ItemClickListener {

    private View view;


    private static final String ARG_DOCTOR_NAME = "doctorName";
    private static final String ARG_DOCTOR_ID = "doctorID";
    private static final String ARG_SELECT_DATE = "selectedDate";

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(AppointmentModel.class.getSimpleName());


    private TimeSlotAdapter timeSlotAdapter;
    private AppointmentConfirmationFragment appointmentConfirmationFragment;


    private String doctorName, doctorID, selectedDate;

    private GridView appointmentSelectGridView;
    private ArrayList<String> unavailableTimeSlots = new ArrayList<>();
    private ArrayList<TimeSlot> timeSlotList;
    private ArrayList<String> timeList = new ArrayList<>();

    private TextView appointmentSelectDoctorName, dateText, timeTextView;

    public AppointmentSelectFragment() {
        // Required empty public constructor
    }

    public static AppointmentSelectFragment newInstance(String doctorName, String doctorID, String selectedDate) {
        AppointmentSelectFragment fragment = new AppointmentSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCTOR_NAME, doctorName);
        args.putString(ARG_DOCTOR_ID, doctorID);
        args.putString(ARG_SELECT_DATE, selectedDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorName = getArguments().getString(ARG_DOCTOR_NAME);
            doctorID = getArguments().getString(ARG_DOCTOR_ID);
            selectedDate = getArguments().getString(ARG_SELECT_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_select, container, false);

        timeSlotList = new ArrayList<>();

        dateText = view.findViewById(R.id.dateText);
        dateText.setText("Date: " + selectedDate);

        appointmentSelectDoctorName = view.findViewById(R.id.appointmentSelectDoctorName);
        appointmentSelectDoctorName.setText("Doctor " + doctorName);

        timeTextView = view.findViewById(R.id.timeTextView);

        appointmentSelectGridView = view.findViewById(R.id.appointmentSelectGridView);

        checkTimeSlots(new TimeSlotCallback() {
            @Override
            public void onComplete(ArrayList<TimeSlot> unavailableTimeSlots) {
                timeSlotAdapter = new TimeSlotAdapter(getActivity(), timeSlotList, AppointmentSelectFragment.this);
                appointmentSelectGridView.setAdapter(timeSlotAdapter);
            }
        });

        return view;
    }

    public void checkTimeSlots(TimeSlotCallback timeSlotCallback) {
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
                timeList.clear();
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

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
                timeFormatter = timeFormatter.withLocale(Locale.US);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
                dateFormatter = dateFormatter.withLocale(Locale.UK);
                LocalDate date = LocalDate.parse(selectedDate, dateFormatter);
                LocalDate currentDate = LocalDate.now();

                Log.i("current date", currentDate.toString());
                Log.i("selected date", date.toString());




                for (String time : timeList) {
                    TimeSlot timeSlot;
                    LocalTime timeSelected = LocalTime.parse(time, timeFormatter);

                    if (unavailableTimeSlots.contains(time) ||
                            ( timeSelected.isBefore(LocalTime.now()) && date.isEqual(currentDate))) {
                        timeSlot = new TimeSlot(time, false);
                    } else {
                        timeSlot = new TimeSlot(time, true);
                    }
                    timeSlotList.add(timeSlot);
                    timeSlotCallback.onComplete(timeSlotList);
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

        Bundle bundle = new Bundle();
        bundle.putString("date", selectedDate);
        bundle.putString("time", timeItem);
        bundle.putString("doctor", doctorName);
        bundle.putString("doctorID", doctorID);

        Navigation.findNavController(view).navigate(R.id.appointmentSelectToConfirm, bundle);

    }

}