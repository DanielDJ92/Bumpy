package com.example.michael.bumpy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.michael.bumpy.Model.Accident;
import com.example.michael.bumpy.Model.Driver;
import com.example.michael.bumpy.Model.Witness;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AccidentDetailsActivity extends AppCompatActivity {
    private String serverUrl = "http://10.10.20.145:3000/acc";
    private Accident accident;
    private String secondDriver;

//    Activity _activity = this;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton addImageButton;
    Button finish;
    Button witnessesButton;
    LinearLayout imagesLayout;
    LayoutInflater inflater;

    ArrayList<Witness> witnessesList = new ArrayList<>();

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

//        addImageButton = (ImageButton) findViewById(R.id.addPhoto);
        imagesLayout = (LinearLayout) findViewById(R.id.imagesLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        finish = (Button) findViewById(R.id.finish);
        witnessesButton = (Button) findViewById(R.id.witnesses);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendDetailsActivity();
                finish();
            }
        });

        witnessesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });

        addImageButton = (ImageButton) inflater.inflate(R.layout.image_button, null);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
        addImageButton.setLayoutParams(layoutParams);
        addImageButton.setBackgroundColor(Color.TRANSPARENT);
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
                        newButton.setBackgroundColor(Color.TRANSPARENT);
                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bitmap bitmap = ((BitmapDrawable)((ImageButton)view).getDrawable()).getBitmap();
                                ((ImageView)findViewById(R.id.bigPhoto)).setImageBitmap(bitmap);
                            }
                        });
                        imagesLayout.addView(newButton,i);
                        imagesList.add(newButton);
                        i++;
                    }
                }, 500);
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
            ((ImageButton)imagesList.get(imagesList.size() - 1)).setImageBitmap(imageBitmap);

            Thread thread = new Thread(new Runnable(){
                public void run(){
                    uploadFile();
                }
            });
            thread.start();
//            Uri uri = data.getData();
//            file = new File(getRealPathFromURI(uri));
        }
        else if (resultCode == RESULT_CANCELED) {
            ImageButton ib = (ImageButton) imagesList.get(imagesList.size() - 1);
            ((ViewGroup) ib.getParent()).removeView(ib);
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

    public void showListDialog() {
        final Dialog dialog = new Dialog(this);

        View view = getLayoutInflater().inflate(R.layout.witnesses_list_dialog, null);

        ListView lv = (ListView) view.findViewById(R.id.custom_list);
//
//        witnessesList.add(new Witness("Michael","0525950412"));
//        witnessesList.add(new Witness("Dani","0521840419"));

        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapter clad = new CustomListAdapter(AccidentDetailsActivity.this, witnessesList);

        lv.setAdapter(clad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //do nothing
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(AccidentDetailsActivity.this);
        // Get the layout inflater
//        LayoutInflater inflater = AccidentDetailsActivity.this.getLayoutInflater();

        final AlertDialog ad;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        showAddDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        ad = builder.create();
        ad.show();

//        dialog.setContentView(view);

//        dialog.show();
    }

    public void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccidentDetailsActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = AccidentDetailsActivity.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.add_witnesses_dialog, null);
        final EditText name = (EditText) v.findViewById(R.id.name);
        final EditText phone = (EditText) v.findViewById(R.id.phone);

        final AlertDialog ad;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        witnessesList.add(new Witness(name.getText().toString(),phone.getText().toString()));
                        showListDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        showListDialog();
                    }
                });
        ad = builder.create();
        ad.setTitle("Add Witness");
        ad.show();
    }
}