package com.example.michael.bumpy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AccidentDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton addImageButton;
    Button finish;
    LinearLayout imagesLayout;
    LayoutInflater inflater;

    int i = 0;

    ArrayList imagesList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accident_details);

//        addImageButton = (ImageButton) findViewById(R.id.addPhoto);
        imagesLayout = (LinearLayout) findViewById(R.id.imagesLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        finish = (Button) findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addImageButton = (ImageButton) inflater.inflate(R.layout.image_button, null);
        imagesLayout.addView(addImageButton,i);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // duplicate + button
//                ImageButton newButton = addImageButton;
//                newButton.setId(0);
                ImageButton newButton = (ImageButton) inflater.inflate(R.layout.image_button, null, true);
                newButton.setMinimumWidth(100);
                newButton.setMinimumHeight(100);
                imagesLayout.addView(newButton,i);
                imagesList.add(newButton);
                i++;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageButton)imagesList.get(i - 1)).setImageBitmap(imageBitmap);
        }
    }
}
