package com.example.medicalcentreappointmentbooker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<String> doctorNames;
    //list of integer to get images from drawable folder -> need to change to get images from database
    private List<Integer> doctorImages;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public DoctorAdapter(Context context, List<String> doctorNames, List<Integer> doctorImages) {
        this.doctorNames = doctorNames;
        this.doctorImages = doctorImages;
        this.inflater = LayoutInflater.from(context);
    }

    public DoctorAdapter(Context context, List<String> doctorNames, List<Integer> doctorImages, ItemClickListener clickListener) {
        this.doctorNames = doctorNames;
        this.doctorImages = doctorImages;
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
        holder.doctorName.setText(doctorNames.get(position));
        holder.doctorImage.setImageResource(doctorImages.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(doctorNames.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorNames.size();
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
