package com.example.michael.bumpy.Globals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Michael on 5/7/2016.
 */
public class Globals {
    final static String serverUrl = "http://10.10.16.151:3000/";

    public static String postDataToServer(JSONObject jsonObj, String uri) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(serverUrl + uri);

            String json = jsonObj.toString();

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

    public static String uploadFile(File file, String accidentId, boolean isAccident) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost;
        if (isAccident) {
            httppost = new HttpPost(serverUrl + "acc/" + accidentId + "/pic");
        }
        else {
            httppost = new HttpPost(serverUrl + "user/"+ accidentId + "/pic");
        }

//        try {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        //File sourceFile = new File("/storage/emulated/0/Android/data/com.hutchgames.mud/files/al/1454455321_768x1024.jpeg");

        // Adding file data to http body
        FileBody body = new FileBody(file);
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

    public static String GetDataFromServer(String endpointUrl){
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet http = new HttpGet(serverUrl + endpointUrl);

            // 7. Set some headers to inform server about the type of the content
            http.setHeader("Accept", "application/json");
            http.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(http);

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
        catch (Exception e)
        {
            result = "Did not work!";
        }

        return result;
    }

    public static Bitmap GetImageFromURL(String url) throws IOException {
        String imageUrl = GetDataFromServer(url);

        HttpGet httpRequest = new HttpGet(serverUrl + imageUrl);

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = (HttpResponse) httpclient
                    .execute(httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();
        BufferedHttpEntity b_entity = null;
        try {
            b_entity = new BufferedHttpEntity(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream input = b_entity.getContent();

        Bitmap bitmap = BitmapFactory.decodeStream(input);
        return bitmap;
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
}

