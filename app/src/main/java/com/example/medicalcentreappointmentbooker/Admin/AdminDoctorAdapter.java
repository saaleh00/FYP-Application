package com.example.medicalcentreappointmentbooker.Admin;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medicalcentreappointmentbooker.Model.Doctor;
import com.example.medicalcentreappointmentbooker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminDoctorAdapter extends RecyclerView.Adapter<AdminDoctorAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Doctor> doctorArrayList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userProfileImages/");


    public AdminDoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList, ItemClickListener clickListener){
        this.context = context;
        this.doctorArrayList = doctorArrayList;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
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

        storageReference.child(doctorArrayList.get(position).getDoctorID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).centerCrop().placeholder(R.drawable.ic_baseline_person_24).into(holder.doctorImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(context).load(R.drawable.resource_default).into(holder.doctorImage);
            }
        });

//        holder.doctorImage.setImageResource(doctorArrayList.get(position).getDoctorProfileImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(doctorArrayList.get(position).getDoctorName(), doctorArrayList.get(position).getDoctorID());
            }
        });
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    public interface ItemClickListener {
        public void onItemClick(String doctorName, String doctorID);

    }
}
