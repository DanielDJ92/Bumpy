package com.example.michael.bumpy.Model;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by Michael on 5/6/2016.
 */
public class Driver {
    private String id;
    private String name;
    private String phone;
    private static Driver instance;
    private String email;
    private Bitmap driverLicense;
    private Bitmap carInsurance;
    private Bitmap carLicense;

    public static Driver getInstance() {

        if (instance == null)
        {
            instance = new Driver();
        }

        return instance;
    }

    private Driver(){
        id = "a";
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }
    
    public boolean SaveToLocalStorage(){
        return  true;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDriverLicense(Bitmap driverLicense) {
        this.driverLicense = driverLicense;
    }

    public void setCarInsurance(Bitmap carInsurance) {
        this.carInsurance = carInsurance;
    }

    public void setCarLicense(Bitmap carLicense) {
        this.carLicense = carLicense;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getDriverLicense() {
        return driverLicense;
    }

    public Bitmap getCarInsurance() {
        return carInsurance;
    }

    public Bitmap getCarLicense() {
        return carLicense;
    }
}
