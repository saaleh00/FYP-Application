package com.example.medicalcentreappointmentbooker;

public class AppointmentModel {
    private String date;
    private String time;
    private String doctor;

    public AppointmentModel(String date, String time, String doctor) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
