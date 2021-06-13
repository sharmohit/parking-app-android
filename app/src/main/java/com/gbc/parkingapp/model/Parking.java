/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

public class Parking {
    private String id;
    private String building_code;
    private String car_plate_number;
    private GeoPoint coordinate;
    private Timestamp date_time;
    private int parking_hours;
    private String street_address;
    private String suit_number;

    public Parking() {
    }

    @Override
    public String toString() {
        return "Parking{" +
                "id='" + id + '\'' +
                ", building_code='" + building_code + '\'' +
                ", car_plate_number='" + car_plate_number + '\'' +
                ", coordinate=" + coordinate +
                ", date_time=" + date_time +
                ", parking_hours=" + parking_hours +
                ", street_address='" + street_address + '\'' +
                ", suit_number='" + suit_number + '\'' +
                '}';
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuilding_code() {
        return building_code;
    }

    public void setBuilding_code(String building_code) {
        this.building_code = building_code;
    }

    public String getCar_plate_number() {
        return car_plate_number;
    }

    public void setCar_plate_number(String car_plate_number) {
        this.car_plate_number = car_plate_number;
    }

    public GeoPoint getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoPoint coordinate) {
        this.coordinate = coordinate;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
    }

    public int getParking_hours() {
        return parking_hours;
    }

    public void setParking_hours(int parking_hours) {
        this.parking_hours = parking_hours;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getSuit_number() {
        return suit_number;
    }

    public void setSuit_number(String suit_number) {
        this.suit_number = suit_number;
    }
}
