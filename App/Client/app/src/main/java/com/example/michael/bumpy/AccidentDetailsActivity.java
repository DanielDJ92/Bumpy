package com.example.michael.bumpy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.michael.bumpy.Model.Accident;
import com.example.michael.bumpy.Model.Driver;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class AccidentDetailsActivity extends AppCompatActivity {
    private String serverUrl = "http://10.10.20.145:3000/acc";
    private Accident accident;
    private String secondDriver;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton addImageButton;
    Button finish;
    LinearLayout imagesLayout;
    LayoutInflater inflater;

    File file;

    int i = 0;

    ArrayList imagesList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accident_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        String secondDriver = intent.getStringExtra("secondDriver");
        ImageButton addImage = (ImageButton) findViewById(R.id.addPhoto);

//        addImageButton = (ImageButton) findViewById(R.id.addPhoto);
        imagesLayout = (LinearLayout) findViewById(R.id.imagesLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        finish = (Button) findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        addImageButton = (ImageButton) inflater.inflate(R.layout.image_button, null);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
        addImageButton.setLayoutParams(layoutParams);
        imagesLayout.addView(addImageButton,i);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // duplicate + button
//                ImageButton newButton = addImageButton;
//                newButton.setId(0);


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ImageButton newButton = (ImageButton) inflater.inflate(R.layout.image_button, null, true);
                        newButton.setLayoutParams(layoutParams);
                        imagesLayout.addView(newButton,i);
                        imagesList.add(newButton);
                        i++;
                    }
                }, 2000);


            }
        });
    }


    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        Accident accident = new Accident(Driver.getInstance().getId(), secondDriver);
        String result = PostDataToServer(accident);

        if (result != null){
            accident.setId(result);
        }
    }

    protected String PostDataToServer(Accident accident) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(serverUrl);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "bla");
            jsonObject.put("location", "bla");
            jsonObject.put("my_id", accident.getFirstDriverId());
            jsonObject.put("opp_id", accident.getSecondDriverId());
            json = jsonObject.toString();

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
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(getFilesDir() + "/new.jpeg", false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageButton)imagesList.get(i - 1)).setImageBitmap(imageBitmap);

            Thread thread = new Thread(new Runnable(){
                public void run(){
                    uploadFile();
                }
            });
            thread.start();
//            Uri uri = data.getData();
//            file = new File(getRealPathFromURI(uri));
        }
    }

    private String uploadFile() {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(serverUrl + "/a/pic");

//        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            File sourceFile = new File(getFilesDir() + "/new.jpeg");
            //File sourceFile = new File("/storage/emulated/0/Android/data/com.hutchgames.mud/files/al/1454455321_768x1024.jpeg");

            // Adding file data to http body
            FileBody body = new FileBody(sourceFile);
            entity.addPart("file", body);

            httppost.setEntity(entity);

            // Making server call
            try {
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            }
            catch (Exception ex) {
                String a = ex.getMessage();
            }

        return responseString;

    }


}