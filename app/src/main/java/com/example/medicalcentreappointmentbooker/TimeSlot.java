package com.example.medicalcentreappointmentbooker;

public class TimeSlot {
    public Boolean isAvailable;
    public String timeSlot;

    TimeSlot(String timeSlot, Boolean isAvailable){
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
