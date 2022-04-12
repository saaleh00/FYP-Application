package com.example.medicalcentreappointmentbooker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class UserProfileUpdateFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private static final String ARG_NAME = "userName";

    private EditText userHeightInput, userWeightInput;
    private TextView userUpdateName;
    private Spinner bloodTypeSpinner;
    private Button updateButton;
    private ProgressBar progressBar;

    private String userName, userBloodType;
    private int userHeight, userWeight;

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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userHeightString = userHeightInput.getText().toString().trim();
                userHeight = Integer.parseInt(userHeightString);
                String userWeightString = userWeightInput.getText().toString().trim();
                userWeight = Integer.parseInt(userWeightString);

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
                progressBar.setVisibility(View.VISIBLE);
//                User user = new User(userName, userHeight, userWeight, userBloodType);

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
                            openFragment(userProfilePageFragment);

                        } else {
                            Toast.makeText(getActivity(), "Failed to update", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userBloodType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}