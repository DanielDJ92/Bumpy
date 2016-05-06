package com.example.michael.bumpy.Model;

import java.util.Random;

/**
 * Created by Michael on 5/6/2016.
 */
public class Driver {
    private String id;
    private static Driver instance;

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

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
