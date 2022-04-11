package com.example.medicalcentreappointmentbooker;

public class User {

    public String name, age, email, role, bloodType;
    public int height, weight;

    public User() {
    }

    public User(String name, String age, String email, String role) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }

    public User(String name, int height, int weight, String bloodType){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
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
