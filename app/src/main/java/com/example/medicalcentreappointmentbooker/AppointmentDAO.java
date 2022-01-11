package com.example.medicalcentreappointmentbooker;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AppointmentDAO {

    private DatabaseReference databaseReference;

    public AppointmentDAO() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(AppointmentModel.class.getSimpleName());
    }

    public Task<Void> insert(AppointmentModel appointmentModel) {
        //can throw exception here
        return databaseReference.push().setValue(appointmentModel);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }
}
