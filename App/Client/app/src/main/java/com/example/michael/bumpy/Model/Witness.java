package com.example.michael.bumpy.Model;

/**
 * Created by Dudu on 07/05/2016.
 */
public class Witness {
    private String name;
    private String phone;

    public Witness(String name, String phone)
    {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getName() {
        return this.name;
    }
}
