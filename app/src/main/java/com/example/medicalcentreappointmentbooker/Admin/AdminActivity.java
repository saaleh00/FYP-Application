package com.example.medicalcentreappointmentbooker.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalcentreappointmentbooker.R;

public class AdminActivity extends AppCompatActivity {

    private AdminHomeFragment adminHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminHomeFragment = new AdminHomeFragment();
    }


}