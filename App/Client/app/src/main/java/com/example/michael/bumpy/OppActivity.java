package com.example.michael.bumpy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class OppActivity extends AppCompatActivity {
    private String mainUrl = "http://10.10.16.151:3000";

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    protected String GetOppPics(String opp_id)
    {
        InputStream inputStream = null;
        String result = "";
        String serverUrl = mainUrl + "/user/" + opp_id + "/pic";

        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet http = new HttpGet(serverUrl);

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

        }

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp);

        LinearLayout tl = (LinearLayout) findViewById(R.id.oppAccidents);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        JSONObject result = null;
        try {

            result = new JSONObject(GetOppPics(getIntent().getStringExtra("opp_id")));
            JSONArray arr = result.getJSONArray("message");
            for (int i = 0; i < arr.length(); ++i) {
                JSONObject rec = arr.getJSONObject(i);
                String pic_id = rec.getString("_id");


                ImageView img = new ImageView(this);

                URL url = new URL(mainUrl + pic_id + ".jpg");
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient
                        .execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                img.setImageBitmap(bitmap);
                img.setAdjustViewBounds(true);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(500,500);
                lp.setMargins(0, 15, 0, 15);
                img.setLayoutParams(lp);

                img.setScaleType(ImageView.ScaleType.FIT_XY);


                //tr.setBackgroundResource(R.drawable.sf_gradient_03);
                tl.addView(img);

            }

        } catch (Throwable t) {

        }
    }
}
