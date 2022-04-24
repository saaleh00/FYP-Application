package com.example.medicalcentreappointmentbooker.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicalcentreappointmentbooker.Model.Doctor;
import com.example.medicalcentreappointmentbooker.Callback.DoctorCallback;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserSeeChatFragment extends Fragment implements UserSeeChatAdapter.ItemClickListener{

    private RecyclerView seeChatRecyclerView;
    private UserSeeChatAdapter userSeeChatAdapter;

    private ArrayList<Doctor> doctorArrayList;

    private UserChatFragment chatFragment;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    public UserSeeChatFragment() {
        // Required empty public constructor
    }

    public static UserSeeChatFragment newInstance() {
        UserSeeChatFragment fragment = new UserSeeChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_see_chat, container, false);

        doctorArrayList = new ArrayList<>();

        seeChatRecyclerView = view.findViewById(R.id.seeChatRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        seeChatRecyclerView.setLayoutManager(layoutManager);

        userSeeChatAdapter = new UserSeeChatAdapter(getActivity(), doctorArrayList, this);
        seeChatRecyclerView.setAdapter(userSeeChatAdapter);

        loadData(new DoctorCallback() {
            @Override
            public void onComplete(ArrayList<Doctor> doctorArrayList) {
                userSeeChatAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    private void loadData(DoctorCallback doctorCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("doctor")){
                        String doctorName = dataSnapshot.child("name").getValue().toString();
                        String doctorID = dataSnapshot.getKey();

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
        chatFragment = UserChatFragment.newInstance(doctorName, doctorID);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, chatFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}