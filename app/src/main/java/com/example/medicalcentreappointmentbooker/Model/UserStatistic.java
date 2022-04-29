package com.example.medicalcentreappointmentbooker.Model;

public class UserStatistic {

    public String name, age, bloodType, userID;
    public int noOfAppointments, noOfCancellations;
    public long timeStamp;
    private double height, weight;

    //For user
    public UserStatistic(String name, String age, String bloodType, String userID, double height, double weight, int noOfAppointments, int noOfCancellations, long timeStamp) {
        this.name = name;
        this.age = age;
        this.bloodType = bloodType;
        this.userID = userID;
        this.height = height;
        this.weight = weight;
        this.noOfAppointments = noOfAppointments;
        this.noOfCancellations = noOfCancellations;
        this.timeStamp = timeStamp;
    }

    //For doctor
    public UserStatistic(String name, String age, String userID, int noOfAppointments, int noOfCancellations, long timeStamp){
        this.name = name;
        this.age = age;
        this.userID = userID;
        this.noOfAppointments = noOfAppointments;
        this.noOfCancellations = noOfCancellations;
        this.timeStamp = timeStamp;
    }

    public UserStatistic(String name, int noOfAppointments, int noOfCancellations, long timeStamp){
        this.name = name;
        this.noOfAppointments = noOfAppointments;
        this.noOfCancellations = noOfCancellations;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getUserID() {
        return userID;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public int getNoOfAppointments() {
        return noOfAppointments;
    }

    public int getNoOfCancellations() {
        return noOfCancellations;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public UserStatistic(){
    }
}
