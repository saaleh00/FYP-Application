package com.example.medicalcentreappointmentbooker;

public class Doctor {
    private String doctorName;
    private Integer doctorProfileImage;
    private String doctorID;


    Doctor(String doctorName, Integer doctorProfileImage, String doctorID) {
        this.doctorName = doctorName;
        this.doctorProfileImage = doctorProfileImage;
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Integer getDoctorProfileImage() {
        return doctorProfileImage;
    }

    public String getDoctorID() {
        return doctorID;
    }

}
