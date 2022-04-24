package com.example.medicalcentreappointmentbooker.User;

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

public class UserSeeChatAdapter extends RecyclerView.Adapter<UserSeeChatAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Doctor> list;
    private ItemClickListener clickListener;

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userProfileImages/");

    public UserSeeChatAdapter(Context context, ArrayList<Doctor> list, ItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_see_chat, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Doctor doctor = list.get(position);

        holder.doctorName.setText(doctor.getDoctorName());

        storageReference.child(doctor.getDoctorID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                clickListener.onItemClick(doctor.getDoctorName(), doctor.getDoctorID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView doctorName;
        private ImageView doctorImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.seeChatDoctorName);
            doctorImage = itemView.findViewById(R.id.seeChatDoctorImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }

    public interface  ItemClickListener {
        void onItemClick(String doctorName, String doctorID);
    }
}
