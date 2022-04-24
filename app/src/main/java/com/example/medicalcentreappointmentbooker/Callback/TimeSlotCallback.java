package com.example.medicalcentreappointmentbooker.Callback;

import com.example.medicalcentreappointmentbooker.Model.TimeSlot;

import java.util.ArrayList;

public interface TimeSlotCallback {

    void onComplete(ArrayList<TimeSlot> timeSlotArrayList);
}
