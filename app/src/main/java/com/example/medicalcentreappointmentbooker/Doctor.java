package com.example.medicalcentreappointmentbooker;

public class Doctor {
    private String doctorName;
    private Integer doctorProfileImage;

    Doctor(String doctorName, Integer doctorProfileImage){
        this.doctorName = doctorName;
        this.doctorProfileImage = doctorProfileImage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Integer getDoctorProfileImage() {
        return doctorProfileImage;
    }

}
