package com.example.medicalcentreappointmentbooker.User;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.medicalcentreappointmentbooker.Callback.UserProfileCallback;
import com.example.medicalcentreappointmentbooker.Model.User;
import com.example.medicalcentreappointmentbooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UserProfileUpdateFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private static final String ARG_NAME = "userName";

    private EditText userHeightInput, userWeightInput;
    private TextView userUpdateName;
    private Spinner bloodTypeSpinner;
    private Button updateButton;
    private ProgressBar progressBar;

    private String userName, userBloodType;
    private double userHeight, userWeight;

    private UserProfilePageFragment userProfilePageFragment;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Users");


    public UserProfileUpdateFragment() {
        // Required empty public constructor
    }


    public static UserProfileUpdateFragment newInstance(String userName) {
        UserProfileUpdateFragment fragment = new UserProfileUpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_update, container, false);

        userHeightInput = view.findViewById(R.id.userUpdateHeight);
        userWeightInput = view.findViewById(R.id.userUpdateWeight);

        userUpdateName = view.findViewById(R.id.userUpdateName);
        userUpdateName.setText(userName);

        updateButton = view.findViewById(R.id.userUpdateButton);
        progressBar = view.findViewById(R.id.userUpdateProgressBar);

        bloodTypeSpinner = (Spinner) view.findViewById(R.id.userUpdateBlood);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.blood_type_array, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(arrayAdapter);
        bloodTypeSpinner.setOnItemSelectedListener(this);

        userProfilePageFragment = new UserProfilePageFragment();

        updateButton.setOnClickListener(this);

        loadData(new UserProfileCallback() {
            @Override
            public void onComplete(User user) {
                userHeightInput.setText(Double.toString(user.getHeight()), TextView.BufferType.EDITABLE);
                userWeightInput.setText(Double.toString(user.getWeight()), TextView.BufferType.EDITABLE);

                if (!user.bloodType.equals("UNKNOWN")){
                    int spinnerPosition = arrayAdapter.getPosition(user.bloodType);
                    bloodTypeSpinner.setSelection(spinnerPosition);
                }
            }
        });

        return view;
    }

    private void loadData(UserProfileCallback userProfileCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                double userHeight,userWeight;
                String userBlood;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.getKey().equals(firebaseUser.getUid())){
                        if (dataSnapshot.hasChild("height")){
                            userHeight = dataSnapshot.child("height").getValue(Double.class);
                        } else{
                            userHeight = 0;
                        }
                        if (dataSnapshot.hasChild("weight")){
                            userWeight = dataSnapshot.child("weight").getValue(Double.class);
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userBloodType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openFragment(int navigationAction) {
        Navigation.findNavController(getView()).navigate(navigationAction);
    }

    @Override
    public void onClick(View v) {
        String userHeightString = userHeightInput.getText().toString().trim();
        String userWeightString = userWeightInput.getText().toString().trim();

        if (userHeightString.isEmpty()){
            userHeightInput.setError("No Height provided");
            userHeightInput.requestFocus();
            return;
        }
        if (userWeightString.isEmpty()){
            userWeightInput.setError("No Weight provided");
            userWeightInput.requestFocus();
            return;
        }
        if (userBloodType.isEmpty()){
            TextView errorText = (TextView)bloodTypeSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("No Blood type selected");
            return;
        }

        userHeight = Double.parseDouble(userHeightString);
        userWeight = Double.parseDouble(userWeightString);

        progressBar.setVisibility(View.VISIBLE);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("height", userHeight);
        userDetails.put("weight", userWeight);
        userDetails.put("bloodType", userBloodType);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //What to do after updating page
                    openFragment(R.id.updateToProfile);

                } else {
                    Toast.makeText(getActivity(), "Failed to update", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}