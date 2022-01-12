package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AppointmentBookedFragment extends Fragment{

    private RecyclerView bookedAppointmentsRecyclerView;
    private AppointmentBookedAdapter appointmentBookedAdapter;

    private ArrayList<AppointmentModel> appointmentModelArrayList;

    private AppointmentDAO appointmentDAO;

    public AppointmentBookedFragment() {
        // Required empty public constructor
    }

    public static AppointmentBookedFragment newInstance() {
        AppointmentBookedFragment fragment = new AppointmentBookedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Booked Appointments");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_booked, container, false);

        appointmentModelArrayList = new ArrayList<>();

        bookedAppointmentsRecyclerView = view.findViewById(R.id.bookedAppointmentsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        bookedAppointmentsRecyclerView.setLayoutManager(layoutManager);

        appointmentBookedAdapter = new AppointmentBookedAdapter(getActivity(), appointmentModelArrayList);
        bookedAppointmentsRecyclerView.setAdapter(appointmentBookedAdapter);

        appointmentDAO = new AppointmentDAO();

        loadData();


        return view;
    }

    private void loadData() {
        appointmentDAO.read().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentModelArrayList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    AppointmentModel appointmentModel = data.getValue(AppointmentModel.class);
                    appointmentModelArrayList.add(appointmentModel);
                    appointmentModel.setKey(data.getKey());
                }
                appointmentBookedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}