/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.model;

import com.google.firebase.firestore.Exclude;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String car_plate_number;

    public User() {
        this.id = "";
        this.name = "";
        this.email = "";
        this.password = "";
        this.phone = "";
        this.car_plate_number = "";
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", carPlateNumber='" + car_plate_number + '\'' +
                '}';
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCar_plate_number() {
        return car_plate_number;
    }

    public void setCar_plate_number(String car_plate_number) {
        this.car_plate_number = car_plate_number;
    }
}
