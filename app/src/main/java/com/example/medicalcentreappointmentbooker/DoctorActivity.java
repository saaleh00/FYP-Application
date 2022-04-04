package com.example.medicalcentreappointmentbooker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class DoctorActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DoctorHomeFragment doctorHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        toolbar = findViewById(R.id.doctorToolbar);
        setSupportActionBar(toolbar);

        doctorHomeFragment = new DoctorHomeFragment();
        openFragment(doctorHomeFragment);

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menubar_home:
                    openFragment(doctorHomeFragment);
                    return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.doctorContainer, fragment);
        fragmentTransaction.commit();
    }
}