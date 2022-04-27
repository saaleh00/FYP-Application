package com.example.medicalcentreappointmentbooker.Admin;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicalcentreappointmentbooker.Callback.DoctorCallback;
import com.example.medicalcentreappointmentbooker.Model.Doctor;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminDoctorFragment extends Fragment implements AdminDoctorAdapter.ItemClickListener{

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).navigate(R.id.adminDoctorListToHome);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

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

        adapter = new AdminDoctorAdapter(getActivity(), doctorArrayList, this);
        adminDoctorRecyclerView.setAdapter(adapter);

        loadData(new DoctorCallback() {
            @Override
            public void onComplete(ArrayList<Doctor> doctorArrayList) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void loadData(DoctorCallback doctorCallback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("doctor")){
                        String doctorName = dataSnapshot.child("name").getValue().toString();
                        String doctorID = dataSnapshot.getKey();
                        String doctorAge = dataSnapshot.child("age").getValue().toString();
                        String doctorEmail = dataSnapshot.child("email").getValue().toString();
                        Integer doctorProfileImage = R.drawable.ic_baseline_person_24;

                        Doctor doctor = new Doctor(doctorName, doctorID,doctorAge, doctorEmail, doctorProfileImage);
                        doctorArrayList.add(doctor);
                        doctorCallback.onComplete(doctorArrayList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Executive Order", "The read failed: " + error.getDetails());
            }
        });
    }

    @Override
    public void onItemClick(String doctorName, String doctorID) {
        Bundle bundle = new Bundle();
        bundle.putString("doctorName", doctorName);
        bundle.putString("doctorID", doctorID);
        Navigation.findNavController(getView()).navigate(R.id.adminDoctorListToDoctor, bundle);
    }
}