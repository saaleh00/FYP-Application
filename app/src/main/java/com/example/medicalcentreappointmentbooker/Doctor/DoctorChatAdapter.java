package com.example.medicalcentreappointmentbooker.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Model.Message;
import com.example.medicalcentreappointmentbooker.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DoctorChatAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Message> messageArrayList;
    private int ITEM_SEND = 1;
    private int ITEM_RECEIVE = 2;

    public DoctorChatAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_item, parent, false);
            return new DoctorChatAdapter.SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_item, parent, false);
            return new DoctorChatAdapter.ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);

        if (holder.getClass() == DoctorChatAdapter.SenderViewHolder.class){
            DoctorChatAdapter.SenderViewHolder viewHolder = (DoctorChatAdapter.SenderViewHolder) holder;

            viewHolder.textMessage.setText(message.getMessage());
        } else {
            DoctorChatAdapter.ReceiverViewHolder viewHolder = (DoctorChatAdapter.ReceiverViewHolder) holder;

            viewHolder.textMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderID()))
            return ITEM_SEND;
        else
            return ITEM_RECEIVE;
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.senderTextMessage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.receiverTextMessage);
        }
    }
}
