package com.example.medicalcentreappointmentbooker.Doctor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalcentreappointmentbooker.R;

public class DoctorActivity extends AppCompatActivity {

    private DoctorHomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        homeFragment = new DoctorHomeFragment();
    }
}