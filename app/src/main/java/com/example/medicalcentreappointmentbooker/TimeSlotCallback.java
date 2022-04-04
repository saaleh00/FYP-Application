package com.example.medicalcentreappointmentbooker;

import java.util.ArrayList;

public interface TimeSlotCallback {

    void onComplete(ArrayList<TimeSlot> timeSlotArrayList);
}
