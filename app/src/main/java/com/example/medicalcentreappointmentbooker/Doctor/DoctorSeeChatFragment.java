package com.example.medicalcentreappointmentbooker.Doctor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Callback.UserArrayListCallback;
import com.example.medicalcentreappointmentbooker.Model.User;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DoctorSeeChatFragment extends Fragment implements DoctorSeeChatAdapter.ItemClickListener{

    private RecyclerView recyclerView;
    private DoctorSeeChatAdapter adapter;

    private ArrayList<User> userArrayList;

    private DoctorChatFragment chatFragment;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    public DoctorSeeChatFragment() {
        // Required empty public constructor
    }


    public static DoctorSeeChatFragment newInstance() {
        DoctorSeeChatFragment fragment = new DoctorSeeChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor_see_chat, container, false);

        userArrayList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.doctorSeeChatRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoctorSeeChatAdapter(getActivity(), userArrayList, this);
        recyclerView.setAdapter(adapter);

        loadData(new UserArrayListCallback() {
            @Override
            public void onComplete(ArrayList<User> userArrayList) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void loadData(UserArrayListCallback callback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("user")){
                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userAge = dataSnapshot.child("age").getValue().toString();
                        String userID = dataSnapshot.getKey();

                        User user = new User(userName, userAge, userID);
                        userArrayList.add(user);
                        callback.onComplete(userArrayList);
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
    public void onItemClick(String userName, String userID) {
        Bundle bundle = new Bundle();
        bundle.putString("receiverName", userName);
        bundle.putString("receiverID", userID);
        Navigation.findNavController(getView()).navigate(R.id.doctorSeeChatToChat, bundle);



    }
}