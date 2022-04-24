package com.example.medicalcentreappointmentbooker.Doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalcentreappointmentbooker.Model.Message;
import com.example.medicalcentreappointmentbooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class DoctorChatFragment extends Fragment {

    private static final String ARG_RECEIVER_NAME = "receiverName";
    private static final String ARG_RECEIVER_ID = "receiverID";

    private String receiverName, receiverID, senderID, senderRoom, receiverRoom;
    private TextView userNameTextView;
    private EditText sendMessageEditText;
    private CardView sendMessageButton;

    private RecyclerView chatRecyclerView;
    private ArrayList<Message> messageArrayList;

    private DoctorChatAdapter adapter;

    public DoctorChatFragment() {
        // Required empty public constructor
    }

    public static DoctorChatFragment newInstance(String receiverName, String receiverID) {
        DoctorChatFragment fragment = new DoctorChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECEIVER_NAME, receiverName);
        args.putString(ARG_RECEIVER_ID, receiverID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receiverName = getArguments().getString(ARG_RECEIVER_NAME);
            receiverID = getArguments().getString(ARG_RECEIVER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_chat, container, false);

        userNameTextView = view.findViewById(R.id.chatUserName);
        sendMessageEditText = view.findViewById(R.id.doctorChatEditText);
        sendMessageButton = view.findViewById(R.id.doctorChatButton);
        chatRecyclerView = view.findViewById(R.id.doctorChatRV);

        userNameTextView.setText(receiverName);

        messageArrayList = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        senderID = firebaseAuth.getUid();

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

        DatabaseReference userReference = database.getReference().child("Users").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("Messages");

        adapter = new DoctorChatAdapter(getActivity(), messageArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageString = sendMessageEditText.getText().toString();
                if (messageString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter valid message", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessageEditText.setText("");
                Date date = new Date();

                Message message = new Message(messageString, senderID, date.getTime());

                database.getReference().child("Chats")
                        .child(senderRoom)
                        .child("Messages").push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("Chats")
                                .child(receiverRoom)
                                .child("Messages").push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });


        return view;
    }
}