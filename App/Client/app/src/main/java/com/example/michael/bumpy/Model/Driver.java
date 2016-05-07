package com.example.michael.bumpy.Model;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.michael.bumpy.Globals.Globals;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
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

    public static Driver getInstance() {

        if (instance == null)
        {
            instance = new Driver();
        }

        return instance;
    }

    private Driver(){
        try(BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/driver.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String fileContent = sb.toString().replaceAll("(\\r|\\n)", "");;
            this.id = fileContent;

        } catch (FileNotFoundException e) {
            this.id = "";
        } catch (IOException e) {
            e.printStackTrace();
            this.id = "";
        }
    }

    public void SaveLocally(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/driver.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(this.id, 0, this.id.length());
        } catch (IOException e) {
            e.printStackTrace(); // I'd rather declare method with throws IOException and omit this catch.
        } finally {
            if (writer != null) try
            {
                writer.close();
            } catch (IOException ignore) {}
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

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    private void FillDriverDetails()
    {

    }
}
