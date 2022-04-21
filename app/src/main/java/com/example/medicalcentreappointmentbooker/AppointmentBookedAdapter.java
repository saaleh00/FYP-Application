package com.example.medicalcentreappointmentbooker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentBookedAdapter extends RecyclerView.Adapter<AppointmentBookedAdapter.MyViewHolder> {

    private Context context;

    private AppointmentDAO appointmentDAO;

    private DatabaseReference statisticReference;
    private int noOfCancellations, doctorCancellations;

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

        statisticReference = FirebaseDatabase.getInstance().getReference("Statistics");

        statisticReference.child(list.get(position).getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOfCancellations = snapshot.child("noOfCancellations").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        statisticReference.child(list.get(position).getDoctorID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("noOfCancellations"))
                    doctorCancellations = snapshot.child("noOfCancellations").getValue(Integer.class);
                else
                    doctorCancellations = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticReference.child(list.get(position).getUserID()).child("noOfCancellations").setValue(noOfCancellations+1);
                statisticReference.child(list.get(position).getDoctorID()).child("noOfCancellations").setValue(doctorCancellations+1);
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
