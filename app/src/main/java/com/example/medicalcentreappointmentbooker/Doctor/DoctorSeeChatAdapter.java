package com.example.medicalcentreappointmentbooker.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Model.User;
import com.example.medicalcentreappointmentbooker.R;

import java.util.ArrayList;

public class DoctorSeeChatAdapter extends RecyclerView.Adapter<DoctorSeeChatAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<User> list;
    private ItemClickListener clickListener;

    public DoctorSeeChatAdapter(Context context, ArrayList<User> list, ItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor_see_chat, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorSeeChatAdapter.MyViewHolder holder, int position) {
        User user = list.get(position);

        holder.userName.setText(user.getName());
        holder.userAge.setText(user.getAge() + " Years Old");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(user.getName(), user.getUserID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView userName, userAge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.seeChatUserName);
            userAge = itemView.findViewById(R.id.seeChatUserAge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public interface  ItemClickListener {
        void onItemClick(String userName, String userID);
    }
}
