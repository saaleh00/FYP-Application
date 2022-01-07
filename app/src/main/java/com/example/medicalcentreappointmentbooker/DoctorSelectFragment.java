package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
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
        appointmentSelectFragment = AppointmentSelectFragment.newInstance(s);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, appointmentSelectFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}