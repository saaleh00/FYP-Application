package com.example.medicalcentreappointmentbooker.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

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