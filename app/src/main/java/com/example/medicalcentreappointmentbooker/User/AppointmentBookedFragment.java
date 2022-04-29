package com.example.medicalcentreappointmentbooker.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Model.AppointmentModel;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class AppointmentBookedFragment extends Fragment{

    private RecyclerView bookedAppointmentsRecyclerView;
    private AppointmentBookedAdapter appointmentBookedAdapter;

    private ArrayList<AppointmentModel> appointmentModelArrayList;

    private TextView noAppointments;

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).navigate(R.id.appointmentBookedToHome);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_booked, container, false);

        appointmentModelArrayList = new ArrayList<>();

        noAppointments = view.findViewById(R.id.userNoAppointmentsTV);

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
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String userID = firebaseAuth.getCurrentUser().getUid();

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

                    if (data.child("userID").getValue(String.class).equals(userID)) {
                        AppointmentModel appointmentModel = data.getValue(AppointmentModel.class);
                        appointmentModelArrayList.add(appointmentModel);
                        appointmentModel.setKey(data.getKey());
                    }
                }
                if (appointmentModelArrayList.isEmpty())
                    noAppointments.setVisibility(View.VISIBLE);
                else
                    appointmentBookedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}