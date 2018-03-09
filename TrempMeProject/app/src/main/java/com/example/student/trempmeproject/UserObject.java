package com.example.student.trempmeproject;

import android.graphics.Bitmap;


public class UserObject {
    private int id;
    private String username;
    private String address;
    private int drivingLicence;
    private Bitmap userImg;

    public UserObject(int id, String username, String address, int drivingLicence, Bitmap userImg){
        this.id=id;
        this.username=username;
        this.address=address;
        this.drivingLicence=drivingLicence;
        this.userImg=userImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(int drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public Bitmap getUserImg() {
        return userImg;
    }

    public void setUserImg(Bitmap userImg) {
        this.userImg = userImg;
    }
}
