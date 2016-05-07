package com.example.michael.bumpy.Model;

import android.content.ContextWrapper;
import android.graphics.Bitmap;

import com.example.michael.bumpy.Globals.Globals;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
        try(BufferedReader br = new BufferedReader(new FileReader( "driver.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String fileContent = sb.toString();
            this.id = fileContent;
            FillDriverDetails();

        } catch (FileNotFoundException e) {
            this.id = "";
        } catch (IOException e) {
            e.printStackTrace();
            this.id = "";
        }
    }

    public void SaveLocally(){
        try(BufferedWriter br = new BufferedWriter(new FileWriter("driver.txt"))) {
            StringBuilder sb = new StringBuilder();
            br.write(this.id, 0, this.id.length());
            br.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void FillDriverDetails()
    {
        String jsonData = Globals.GetDataFromServer("/user/" + id);
    }
}
