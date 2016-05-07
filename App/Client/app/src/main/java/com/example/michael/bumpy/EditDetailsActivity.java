package com.example.michael.bumpy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.michael.bumpy.Model.Accident;
import com.example.michael.bumpy.Model.Driver;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditDetailsActivity extends AppCompatActivity {

    private String mainUrl = "http://10.10.16.151:3000";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton imageButtonToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
    }

    public void imageButtonListener(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageButtonToUpdate = (ImageButton)v;

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onClickListener(View v) {
        Driver driver = Driver.getInstance();
        String phone = ((EditText)findViewById(R.id.phoneVal)).getText().toString();
        String name = ((EditText)findViewById(R.id.nameVal)).getText().toString();
        String email = ((EditText)findViewById(R.id.emailVal)).getText().toString();
        Bitmap drivingLicense = ((BitmapDrawable)((ImageButton)findViewById(R.id.addDrivingLicensePhoto)).getDrawable()).getBitmap();
        Bitmap carInsurance = ((BitmapDrawable)((ImageButton)findViewById(R.id.addCarInsurancePhoto)).getDrawable()).getBitmap();
        Bitmap carLicense = ((BitmapDrawable)((ImageButton)findViewById(R.id.addCarLicensePhoto)).getDrawable()).getBitmap();

        driver.setPhone(phone);
        driver.setEmail(email);
        driver.setName(name);

        PostDataToServer(driver);
        PostImagesToServer(drivingLicense,carInsurance,carLicense);

    }

    private void PostImagesToServer(Bitmap drivingLicense, Bitmap carInsurance, Bitmap carLicense) {

    }

    protected String PostDataToServer(Driver driver) {
        InputStream inputStream = null;
        Gson gson = new Gson();
        String result = "";
        String serverUrl = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(serverUrl);
            String json = gson.toJson(driver);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            try {
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                }
                else {
                    result = "Did not work!";
                }
            }
            catch (Exception ex){
                String msg = ex.getMessage();
            }

        }
        catch (Exception e)
        {

        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButtonToUpdate.setImageBitmap(imageBitmap);
        }
    }
}
