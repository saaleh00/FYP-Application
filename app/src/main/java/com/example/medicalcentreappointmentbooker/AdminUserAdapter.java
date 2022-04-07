package com.example.medicalcentreappointmentbooker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {

    private ArrayList<User> userArrayList;
    private LayoutInflater inflater;

    public AdminUserAdapter(Context context, ArrayList<User> userArrayList){
        this.userArrayList = userArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdminUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.admin_user_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserAdapter.ViewHolder holder, int position) {
        holder.userName.setText(userArrayList.get(position).getName());
        holder.userEmail.setText(userArrayList.get(position).getEmail());
        holder.userAge.setText(userArrayList.get(position).getAge());

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView userEmail;
        TextView userAge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.adminUserNameItem);
            userEmail = itemView.findViewById(R.id.adminUserEmail);
            userAge = itemView.findViewById(R.id.adminUserAge);

        }
    }
}
