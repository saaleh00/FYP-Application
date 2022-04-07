package com.example.medicalcentreappointmentbooker;

public class User {

    public String name, age, email, role;

    public User(){}

    public User(String name, String age, String email, String role) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
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
}
