package com.example.medicalcentreappointmentbooker.User;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medicalcentreappointmentbooker.Model.Message;
import com.example.medicalcentreappointmentbooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class UserChatFragment extends Fragment {

    private static final String ARG_RECEIVER_NAME = "receiverName";
    private static final String ARG_RECEIVER_ID = "receiverID";

    private String receiverName, receiverID, senderID, senderRoom, receiverRoom;
    private ImageView doctorImage;
    private TextView doctorNameTextView;
    private EditText sendMessageEditText;
    private CardView sendMessageButton;

    private RecyclerView chatRecyclerView;
    private ArrayList<Message> messageArrayList;

    private UserChatAdapter userChatAdapter;

    public UserChatFragment() {
        // Required empty public constructor
    }


    public static UserChatFragment newInstance(String doctorName, String receiverID) {
        UserChatFragment fragment = new UserChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECEIVER_NAME, doctorName);
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
        View view = inflater.inflate(R.layout.fragment_user_chat, container, false);

        doctorImage = view.findViewById(R.id.chatDoctorImage);
        doctorNameTextView = view.findViewById(R.id.chatDoctorName);
        sendMessageEditText = view.findViewById(R.id.messageEditText);
        sendMessageButton = view.findViewById(R.id.sendMessageButton);

        doctorNameTextView.setText(receiverName);

        messageArrayList = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        senderID = firebaseAuth.getUid();

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userProfileImages/");
        DatabaseReference userReference = database.getReference().child("Users").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("Messages");


        userChatAdapter = new UserChatAdapter(getActivity(), messageArrayList);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(userChatAdapter);

        storageReference.child(receiverID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
                userChatAdapter.notifyDataSetChanged();
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