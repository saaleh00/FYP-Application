package com.example.medicalcentreappointmentbooker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfilePageFragment extends Fragment {

    private TextView userProfileName, userProfileHeight, userProfileWeight, userProfileBlood;
    private Button userProfileChangeButton;

    private UserProfileUpdateFragment userProfileUpdateFragment;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    public UserProfilePageFragment() {
        // Required empty public constructor
    }

    public static UserProfilePageFragment newInstance() {
        UserProfilePageFragment fragment = new UserProfilePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_profile_page, container, false);

        userProfileName = view.findViewById(R.id.userProfileName);
        userProfileHeight = view.findViewById(R.id.userProfileHeight);
        userProfileWeight = view.findViewById(R.id.userProfileWeight);
        userProfileBlood = view.findViewById(R.id.userProfileBlood);

        userProfileChangeButton = view.findViewById(R.id.userProfileChangeButton);

        loadData(new UserProfileCallback() {
            @Override
            public void onComplete(User user) {
                userProfileName.setText(user.getName());
                userProfileHeight.setText(Integer.toString(user.getHeight()));
                userProfileWeight.setText(Integer.toString(user.getWeight()));
                userProfileBlood.setText(user.getBloodType());

                userProfileUpdateFragment = UserProfileUpdateFragment.newInstance(user.getName());
                userProfileChangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFragment(userProfileUpdateFragment);
                    }
                });
            }
        });

        return view;
    }

    private void loadData(UserProfileCallback userProfileCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                int userHeight;
                int userWeight;
                String userBlood;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.getKey().equals(firebaseUser.getUid())){
                        if (dataSnapshot.hasChild("height")){
                            userHeight = dataSnapshot.child("height").getValue(Integer.class);
                        } else{
                            userHeight = 0;
                        }
                        if (dataSnapshot.hasChild("weight")){
                            userWeight = dataSnapshot.child("weight").getValue(Integer.class);
                        } else{
                            userWeight = 0;
                        }
                        if (dataSnapshot.hasChild("bloodType")){
                            userBlood = dataSnapshot.child("bloodType").getValue().toString();
                        } else{
                            userBlood = "UNKNOWN";
                        }
                        String userName = dataSnapshot.child("name").getValue().toString();

                        User user = new User(userName, userHeight, userWeight, userBlood);
                        userProfileCallback.onComplete(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("tag", error.getMessage());
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}