package com.example.medicalcentreappointmentbooker.User;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalcentreappointmentbooker.R;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
    }
}