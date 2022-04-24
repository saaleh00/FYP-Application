package com.example.medicalcentreappointmentbooker.Model;

public class TimeSlot {
    public Boolean isAvailable;
    public String timeSlot;

    public TimeSlot(String timeSlot, Boolean isAvailable){
        this.timeSlot = timeSlot;
        this.isAvailable = isAvailable;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public Boolean isAvailable(){
        return isAvailable;
    }
}
