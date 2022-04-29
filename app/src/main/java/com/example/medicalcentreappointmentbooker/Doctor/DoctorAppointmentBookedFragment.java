package com.example.medicalcentreappointmentbooker.Doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Model.AppointmentModel;
import com.example.medicalcentreappointmentbooker.R;
import com.example.medicalcentreappointmentbooker.User.AppointmentDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class DoctorAppointmentBookedFragment extends Fragment {

    private RecyclerView bookedAppointmentRecyclerView;
    private DoctorAppointmentBookedAdapter doctorAppointmentBookedAdapter;

    private ArrayList<AppointmentModel> appointmentModelArrayList;

    private TextView noAppointments;

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

        noAppointments = view.findViewById(R.id.doctorNoAppointmentsTV);

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
                    String apptDate = data.child("date").getValue(String.class);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
                    formatter = formatter.withLocale(Locale.UK);
                    LocalDate date = LocalDate.parse(apptDate, formatter);
                    LocalDate currentDate = LocalDate.now();

                    if (currentDate.isAfter(date)){
                        appointmentDAO.delete(data.getKey());
                        continue;
                    }

                    if (data.child("doctorID").getValue(String.class).equals(doctorID)) {
                        AppointmentModel appointmentModel = data.getValue(AppointmentModel.class);
                        appointmentModelArrayList.add(appointmentModel);
                        appointmentModel.setKey(data.getKey());
                    }
                }
                if (appointmentModelArrayList.isEmpty())
                    noAppointments.setVisibility(View.VISIBLE);
                else
                    doctorAppointmentBookedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}