package com.example.medicalcentreappointmentbooker;

public class Doctor {
    private String doctorName;
    private String doctorAge;
    private String doctorEmail;
    private Integer doctorProfileImage;
    private String doctorID;


    Doctor(String doctorName, Integer doctorProfileImage, String doctorID) {
        this.doctorName = doctorName;
        this.doctorProfileImage = doctorProfileImage;
        this.doctorID = doctorID;
    }


    Doctor(String doctorName, String doctorID,String doctorAge, String doctorEmail, Integer doctorProfileImage){
        this.doctorName = doctorName;
        this.doctorID = doctorID;
        this.doctorAge = doctorAge;
        this.doctorEmail = doctorEmail;
        this.doctorProfileImage = doctorProfileImage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorAge() {
        return doctorAge;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public Integer getDoctorProfileImage() {
        return doctorProfileImage;
    }

    public String getDoctorID() {
        return doctorID;
    }

}
