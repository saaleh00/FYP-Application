package com.example.medicalcentreappointmentbooker.Admin;

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

import com.example.medicalcentreappointmentbooker.Callback.AdminUserCallback;
import com.example.medicalcentreappointmentbooker.Model.User;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminUserFragment extends Fragment implements AdminUserAdapter.ItemClickListener{

    private RecyclerView adminUserRecyclerView;

    private ArrayList<User> userArrayList;

    private AdminUserAdapter adapter;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    public AdminUserFragment() {
        // Required empty public constructor
    }

    public static AdminUserFragment newInstance() {
        AdminUserFragment fragment = new AdminUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);

        userArrayList = new ArrayList<>();

        adminUserRecyclerView = view.findViewById(R.id.adminUserRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adminUserRecyclerView.setLayoutManager(layoutManager);

        adapter = new AdminUserAdapter(getActivity(), userArrayList, this);
        adminUserRecyclerView.setAdapter(adapter);

        loadData(new AdminUserCallback() {
            @Override
            public void onComplete(ArrayList<User> userArrayList) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void loadData(AdminUserCallback adminUserCallback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("user")){
                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userEmail = dataSnapshot.child("email").getValue().toString();
                        String userAge = dataSnapshot.child("age").getValue().toString();
                        String userID = dataSnapshot.getKey();

                        User user = new User(userName, userAge, userEmail, "user", userID);
                        userArrayList.add(user);
                        adminUserCallback.onComplete(userArrayList);
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
        bundle.putString("userName", userName);
        bundle.putString("userID", userID);
        Navigation.findNavController(getView()).navigate(R.id.adminUserListToUser, bundle);


    }
}