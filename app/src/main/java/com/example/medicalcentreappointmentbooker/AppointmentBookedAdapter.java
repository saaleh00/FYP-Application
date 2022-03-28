package com.example.medicalcentreappointmentbooker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentBookedAdapter extends RecyclerView.Adapter<AppointmentBookedAdapter.MyViewHolder> {

    private Context context;

    private AppointmentDAO appointmentDAO;

    private ArrayList<AppointmentModel> list;


    public AppointmentBookedAdapter(Context context, ArrayList<AppointmentModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppointmentModel appointmentModel = list.get(position);

        holder.date.setText(appointmentModel.getDate());
        holder.time.setText(appointmentModel.getTime());
        holder.doctor.setText(appointmentModel.getDoctor());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentDAO = new AppointmentDAO();
                appointmentDAO.delete(appointmentModel.getKey()).addOnSuccessListener(success ->
                {
                    Toast.makeText(context, "Appointment Cancelled", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(position);
                }).addOnFailureListener(error ->
                {
                    Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView date, time, doctor;
        public Button deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.appointmentBookedDateTextView);
            time = itemView.findViewById(R.id.appointmentBookedTimeTextView);
            doctor = itemView.findViewById(R.id.appointmentBookedDoctorTextView);

            deleteButton = itemView.findViewById(R.id.deleteBookedAppointmentButton);
        }
    }
}
