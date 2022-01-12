package com.example.medicalcentreappointmentbooker;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class AppointmentDAO {

    private DatabaseReference databaseReference;

    public AppointmentDAO() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(AppointmentModel.class.getSimpleName());
    }

    public Task<Void> create(AppointmentModel appointmentModel) {
        //can throw exception here
        return databaseReference.push().setValue(appointmentModel);
    }

    public Query read(){
        return databaseReference.orderByKey();
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key){
        return databaseReference.child(key).removeValue();
    }


}
