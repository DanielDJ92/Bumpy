package com.example.michael.bumpy;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.michael.bumpy.Globals.Globals;
import com.example.michael.bumpy.Model.Driver;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyAccidentsActivity extends AppCompatActivity {
    private String mainUrl = Globals.serverUrl;

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    protected String GetAccidentData()
    {
        InputStream inputStream = null;
        String result = "";
        String serverUrl = mainUrl + "user/" + Driver.getInstance().getId() + "/acc";

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
        setContentView(R.layout.activity_my_accidents);
/* Find Tablelayout defined in main.xml */
        TableLayout tl = (TableLayout) findViewById(R.id.accidentTable);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        JSONObject result = null;

        try {
            result = new JSONObject(GetAccidentData());
            JSONArray arr = result.getJSONArray("acc");
            for (int i = 0; i < arr.length(); ++i) {
                final JSONObject rec = arr.getJSONObject(i);
                String name = rec.getString("desc");

                /* Create a new row to be added. */
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */

                TextView nameView = new TextView(this);
                nameView.setText(name);
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 1f);
                nameView.setLayoutParams(params);

                tr.addView(nameView);

                Button b2 = new Button(this);
                b2.setText("Opp Driver");
                b2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
                b2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MyAccidentsActivity.this, OppActivity.class);
                        try {
                            intent.putExtra("opp_id", rec.getString("opp_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                tr.addView(b2);


                Button b3 = new Button(this);
                b3.setText("Witnesses");
                b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
                b3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MyAccidentsActivity.this, OppActivity.class);
                        try {
                            intent.putExtra("acc_id", rec.getString("acc_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                tr.addView(b3);


                Button b = new Button(this);
                b.setText("Accident Pics");
                b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MyAccidentsActivity.this, AccidentDet.class);
                        try {
                            intent.putExtra("acc_id", rec.getString("my_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                tr.addView(b);
                //tr.setBackgroundResource(R.drawable.sf_gradient_03);
                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        /* Add row to TableLayout. */

    }
}
