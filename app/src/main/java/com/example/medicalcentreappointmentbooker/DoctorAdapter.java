package com.example.medicalcentreappointmentbooker;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private ArrayList<Doctor> doctorArrayList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    private Context context;

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userProfileImages/");


    public DoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList, ItemClickListener clickListener) {
        this.doctorArrayList = doctorArrayList;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.context = context;
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

        public void onItemClick(String doctorName, String doctorID);
    }
}
