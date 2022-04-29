package com.example.medicalcentreappointmentbooker.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalcentreappointmentbooker.Model.AppointmentModel;
import com.example.medicalcentreappointmentbooker.R;

import java.util.ArrayList;

public class DoctorAppointmentBookedAdapter extends RecyclerView.Adapter<DoctorAppointmentBookedAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<AppointmentModel> list;

    public DoctorAppointmentBookedAdapter(Context context, ArrayList<AppointmentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_doctor_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppointmentModel appointmentModel = list.get(position);

        holder.date.setText(appointmentModel.getDate());
        holder.time.setText(appointmentModel.getTime());
        holder.patient.setText(appointmentModel.getPatient());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, time, patient;

        public MyViewHolder(View view) {
            super(view);
            date = itemView.findViewById(R.id.appointmentDoctorBookedDateTextView);
            time = itemView.findViewById(R.id.appointmentDoctorBookedTimeTextView);
            patient = itemView.findViewById(R.id.appointmentDoctorBookedPatientTextView);
        }
    }
}
