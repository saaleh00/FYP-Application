package com.example.medicalcentreappointmentbooker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private RecyclerView doctorRecyclerView;
    List<String> doctorNames;
    List<Integer> doctorImages;
    DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        doctorNames = new ArrayList<>();
        doctorImages = new ArrayList<>();
        doctorNames.add("John");
        doctorNames.add("Who");
        doctorImages.add(R.drawable.ic_baseline_person_24);
        doctorImages.add(R.drawable.ic_baseline_person_24);

        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        adapter = new DoctorAdapter(this, doctorNames, doctorImages);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        doctorRecyclerView.setLayoutManager(gridLayoutManager);
        doctorRecyclerView.setAdapter(adapter);



    }
}