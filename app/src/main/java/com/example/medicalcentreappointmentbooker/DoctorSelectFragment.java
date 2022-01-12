package com.example.medicalcentreappointmentbooker;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DoctorSelectFragment extends Fragment implements DoctorAdapter.ItemClickListener {

    private RecyclerView doctorRecyclerView;

    private List<String> doctorNames;
    private List<Integer> doctorImages;

    private DoctorAdapter adapter;

    private AppointmentSelectFragment appointmentSelectFragment;


    public DoctorSelectFragment() {
        // Required empty public constructor
    }

    public static DoctorSelectFragment newInstance() {
        DoctorSelectFragment fragment = new DoctorSelectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Select Doctor and Date");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_select, container, false);

        doctorNames = new ArrayList<>();
        doctorImages = new ArrayList<>();
        doctorNames.add("John");
        doctorNames.add("Who");
        doctorImages.add(R.drawable.ic_baseline_person_24);
        doctorImages.add(R.drawable.ic_baseline_person_24);

        doctorRecyclerView = view.findViewById(R.id.doctorRecyclerView);
        adapter = new DoctorAdapter(getActivity(), doctorNames, doctorImages, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        doctorRecyclerView.setLayoutManager(gridLayoutManager);
        doctorRecyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onItemClick(String s) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                appointmentSelectFragment = AppointmentSelectFragment.newInstance(s, currentDateString);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, appointmentSelectFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };

        final Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, Year, Month, Day);
        dateDialog.show();
    }
}