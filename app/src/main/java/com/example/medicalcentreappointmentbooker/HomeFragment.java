package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {
    private Button bookingActivityButton;
    private Button bookedAppointmentsButton;

    private DoctorSelectFragment doctorSelectFragment;
    private AppointmentBookedFragment appointmentBookedFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Appointment Booker");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        doctorSelectFragment = new DoctorSelectFragment();
        appointmentBookedFragment = new AppointmentBookedFragment();

        bookingActivityButton = view.findViewById(R.id.BookingActivityButton);
        bookingActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(doctorSelectFragment);
            }
        });

        bookedAppointmentsButton = view.findViewById(R.id.bookedAppointmentsButton);
        bookedAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(appointmentBookedFragment);
            }
        });

        return view;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}