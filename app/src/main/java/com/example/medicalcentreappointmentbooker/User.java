package com.example.medicalcentreappointmentbooker;

public class User {

    public String name, age, email, role, userID, bloodType;
    public int height, weight, noOfAppointments, noOfCancellations;

    public User() {
    }

    //normal user
    public User(String name, String age, String email, String role, String userID) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
        this.userID = userID;
    }

    //User without userID
    public User(String name, String age, String email, String role) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }

    //user profile
    public User(String name, int height, int weight, String bloodType){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
    }

    //user appointments
    public User(String name, int noOfAppointments, int noOfCancellations){
        this.name = name;
        this.noOfAppointments = noOfAppointments;
        this.noOfCancellations = noOfCancellations;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getBloodType() {
        return bloodType;
    }
}
