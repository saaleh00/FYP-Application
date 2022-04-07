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

public class AdminDoctorAdapter extends RecyclerView.Adapter<AdminDoctorAdapter.ViewHolder> {

    private ArrayList<Doctor> doctorArrayList;
    private LayoutInflater inflater;

    public AdminDoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList){
        this.doctorArrayList = doctorArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdminDoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.admin_doctor_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDoctorAdapter.ViewHolder holder, int position) {
        holder.doctorName.setText(doctorArrayList.get(position).getDoctorName());
        holder.doctorAge.setText(doctorArrayList.get(position).getDoctorAge());
        holder.doctorEmail.setText(doctorArrayList.get(position).getDoctorEmail());
        holder.doctorImage.setImageResource(doctorArrayList.get(position).getDoctorProfileImage());

    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView doctorName;
        TextView doctorEmail;
        TextView doctorAge;
        ImageView doctorImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.adminDoctorNameItem);
            doctorAge = itemView.findViewById(R.id.adminDoctorAge);
            doctorEmail = itemView.findViewById(R.id.adminDoctorEmail);
            doctorImage = itemView.findViewById(R.id.adminDoctorImage);

        }
    }
}
