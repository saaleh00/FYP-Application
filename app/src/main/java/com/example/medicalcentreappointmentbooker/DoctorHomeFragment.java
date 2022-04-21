package com.example.medicalcentreappointmentbooker;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DoctorHomeFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;

    private Button viewBookingsButton;

    private TextView doctorUserName;
    private ImageView doctorImage;

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userProfileImages/");

    private DoctorAppointmentBookedFragment doctorAppointmentBookedFragment;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    public static DoctorHomeFragment newInstance() {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        doctorImage = view.findViewById(R.id.doctorHomeImage);
        doctorUserName = view.findViewById(R.id.doctorUserName);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();



        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String userName = userProfile.name;
                    doctorUserName.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        storageReference.child(userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).centerCrop().placeholder(R.drawable.ic_baseline_person_24).into(doctorImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(getActivity()).load(R.drawable.resource_default).into(doctorImage);
            }
        });

        doctorAppointmentBookedFragment = new DoctorAppointmentBookedFragment();

        viewBookingsButton = view.findViewById(R.id.doctorViewBookings);
        viewBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(doctorAppointmentBookedFragment);
            }
        });

        return view;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.doctorContainer, fragment);
        fragmentTransaction.commit();
    }
}