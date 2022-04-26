package com.example.medicalcentreappointmentbooker.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.medicalcentreappointmentbooker.R;
import com.example.medicalcentreappointmentbooker.User.HomeFragment;

public class DoctorActivity extends AppCompatActivity {

    private DoctorHomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        homeFragment = new DoctorHomeFragment();
    }
}