package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminDoctorFragment extends Fragment {

    private RecyclerView adminDoctorRecyclerView;

    private ArrayList<Doctor> doctorArrayList;

    private AdminDoctorAdapter adapter;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    public AdminDoctorFragment() {
        // Required empty public constructor
    }

    public static AdminDoctorFragment newInstance() {
        AdminDoctorFragment fragment = new AdminDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_doctor, container, false);

        doctorArrayList = new ArrayList<>();

        adminDoctorRecyclerView = view.findViewById(R.id.adminDoctorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adminDoctorRecyclerView.setLayoutManager(layoutManager);

        adapter = new AdminDoctorAdapter(getActivity(), doctorArrayList);
        adminDoctorRecyclerView.setAdapter(adapter);

        loadData(new AdminDoctorCallback() {
            @Override
            public void onComplete(ArrayList<Doctor> doctorArrayList) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void loadData(AdminDoctorCallback adminDoctorCallback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("doctor")){
                        String doctorName = dataSnapshot.child("name").getValue().toString();
                        String doctorAge = dataSnapshot.child("age").getValue().toString();
                        String doctorEmail = dataSnapshot.child("email").getValue().toString();
                        Integer doctorProfileImage = R.drawable.ic_baseline_person_24;

                        Doctor doctor = new Doctor(doctorName, doctorAge, doctorEmail, doctorProfileImage);
                        doctorArrayList.add(doctor);
                        adminDoctorCallback.onComplete(doctorArrayList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Executive Order", "The read failed: " + error.getDetails());
            }
        });
    }
}