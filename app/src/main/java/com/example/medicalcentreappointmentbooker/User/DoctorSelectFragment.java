package com.example.medicalcentreappointmentbooker.User;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Callback.DoctorCallback;
import com.example.medicalcentreappointmentbooker.Doctor.DoctorAdapter;
import com.example.medicalcentreappointmentbooker.Model.Doctor;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class DoctorSelectFragment extends Fragment implements DoctorAdapter.ItemClickListener {

    private RecyclerView doctorRecyclerView;

    private ArrayList<Doctor> doctorArrayList;

    private DoctorAdapter adapter;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");


    public DoctorSelectFragment() {
        // Required empty public constructor
    }

    public static DoctorSelectFragment newInstance() {
        DoctorSelectFragment fragment = new DoctorSelectFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor_select, container, false);

        doctorArrayList = new ArrayList<>();

        adapter = new DoctorAdapter(getActivity(), doctorArrayList, DoctorSelectFragment.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        doctorRecyclerView = view.findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(gridLayoutManager);
        doctorRecyclerView.setAdapter(adapter);

        getDoctorList(new DoctorCallback() {
            @Override
            public void onComplete(ArrayList<Doctor> doctorArrayList) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void getDoctorList(DoctorCallback doctorCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("doctor")) {
                        String doctorName = dataSnapshot.child("name").getValue().toString();
                        String doctorID = dataSnapshot.getKey();
                        //Get image from firebase
                        Doctor doctor = new Doctor(doctorName, R.drawable.ic_baseline_person_24, doctorID);
                        doctorArrayList.add(doctor);
                        doctorCallback.onComplete(doctorArrayList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Executive Order", "The read failed: " + error.getDetails());
            }
        });
    }

    @Override
    public void onItemClick(String doctorName, String doctorID) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.UK);
                String currentDateString = dateFormat.format(calendar.getTime());


                Bundle bundle = new Bundle();
                bundle.putString("doctorName", doctorName);
                bundle.putString("doctorID", doctorID);
                bundle.putString("selectedDate", currentDateString);

                Navigation.findNavController(getView()).navigate(R.id.doctorToAppointmentSelect, bundle);
            }
        };

        final Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, Year, Month, Day);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        dateDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        cal.add(Calendar.WEEK_OF_MONTH, 2);
        dateDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        dateDialog.show();
    }
}