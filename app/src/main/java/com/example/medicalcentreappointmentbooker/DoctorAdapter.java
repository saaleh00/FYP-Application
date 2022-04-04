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
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private ArrayList<Doctor> doctorArrayList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public DoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList) {
        this.doctorArrayList = doctorArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    public DoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList, ItemClickListener clickListener) {
        this.doctorArrayList = doctorArrayList;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {
        holder.doctorName.setText(doctorArrayList.get(position).getDoctorName());
        holder.doctorImage.setImageResource(doctorArrayList.get(position).getDoctorProfileImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(doctorArrayList.get(position).getDoctorName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView doctorName;
        ImageView doctorImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorImage = itemView.findViewById(R.id.doctorImage);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public interface ItemClickListener {

        public void onItemClick(String s);
    }
}
