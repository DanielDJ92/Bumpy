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
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
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
        HttpGet httpRequest = new HttpGet(url);

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

    //private
}

