package com.example.medicalcentreappointmentbooker;

import java.util.ArrayList;

public interface Callback {

    void onComplete(ArrayList<TimeSlot> timeSlotArrayList);
}
