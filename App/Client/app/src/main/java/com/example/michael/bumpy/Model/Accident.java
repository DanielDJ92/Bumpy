package com.example.michael.bumpy.Model;

/**
 * Created by Michael on 5/6/2016.
 */
public class Accident {
    private String firstDriverId;
    private String secondDriverId;
    private String id;

    public Accident(String firstDriver, String secondDriver)
    {
        this.firstDriverId = firstDriver;
        this.secondDriverId = secondDriver;
    }

    public String getFirstDriverId() {
        return this.firstDriverId;
    }

    public String getSecondDriverId() {
        return this.secondDriverId;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
}
