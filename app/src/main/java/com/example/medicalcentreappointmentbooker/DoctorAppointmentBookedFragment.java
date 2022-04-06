package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DoctorAppointmentBookedFragment extends Fragment {

    private RecyclerView bookedAppointmentRecyclerView;
    private DoctorAppointmentBookedAdapter doctorAppointmentBookedAdapter;

    private ArrayList<AppointmentModel> appointmentModelArrayList;

    private AppointmentDAO appointmentDAO;

    public DoctorAppointmentBookedFragment() {
        // Required empty public constructor
    }

    public static DoctorAppointmentBookedFragment newInstance() {
        DoctorAppointmentBookedFragment fragment = new DoctorAppointmentBookedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_appointment_booked, container, false);

        appointmentModelArrayList = new ArrayList<>();

        bookedAppointmentRecyclerView = view.findViewById(R.id.doctorBookedAppointmentsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        bookedAppointmentRecyclerView.setLayoutManager(layoutManager);

        doctorAppointmentBookedAdapter = new DoctorAppointmentBookedAdapter(getActivity(), appointmentModelArrayList);
        bookedAppointmentRecyclerView.setAdapter(doctorAppointmentBookedAdapter);

        appointmentDAO = new AppointmentDAO();

        loadData();

        return view;
    }

    private void loadData() {
        appointmentDAO.read().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentModelArrayList.clear();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String doctorID = firebaseAuth.getCurrentUser().getUid();
                for(DataSnapshot data : snapshot.getChildren()){
                    AppointmentModel appointmentModel = data.getValue(AppointmentModel.class);
                    if (appointmentModel.getDoctorID().equals(doctorID)) {
                        appointmentModelArrayList.add(appointmentModel);
                        appointmentModel.setKey(data.getKey());
                    }
                }
                doctorAppointmentBookedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}