package com.example.michael.bumpy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.michael.bumpy.Model.Driver;

import java.io.File;

public class MainActivity extends AppCompatActivity implements
        NfcAdapter.CreateNdefMessageCallback {
    //String DuduDriverId   = "572dd43f8db0ddb6563c6d3a";
    //String LeonidDriverId = "572dd9c8a89f5d2358443878";
    private Driver driver;
    private static final String TAG = "MainActivity";

    private NfcAdapter nfcAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_accidents: {
                Intent intent = new Intent(this, MyAccidentsActivity.class);
                startActivity(intent);

                return true;
            }
            case R.id.edit_details: {
                Intent intent = new Intent(this, EditDetailsActivity.class);
                startActivity(intent);

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        driver = Driver.getInstance();
        //driver.setId(DuduDriverId);
//        Intent newIntent = new Intent(this, AccidentDetailsActivity.class);
//        startActivity(newIntent);
        Intent intent = getIntent();
        String action = intent.getAction();

        if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(
                            NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage)parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String inMsg = new String(NdefRecord_0.getPayload());

            Intent newIntent2 = new Intent(this, AccidentDetailsActivity.class);
            newIntent2.putExtra("secondDriver", inMsg);
            startActivity(newIntent2);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickListener(View v)
    {
        Log.d(TAG, "onCreate: ");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter==null){
            Toast.makeText(MainActivity.this,
                    "nfcAdapter==null, no NFC adapter exists",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this,
                    "Touch a near device!",
                    Toast.LENGTH_LONG).show();
            try {
                nfcAdapter.setNdefPushMessageCallback(this, this);
            }
            catch (Exception ex){

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(
                            NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage)parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String inMsg = new String(NdefRecord_0.getPayload());

            Intent newIntent = new Intent(this, AccidentDetailsActivity.class);
            intent.putExtra("secondDriver", inMsg);
            startActivity(newIntent);
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        Log.d(TAG, "createNdefMessage: ");
        byte[] bytesOut = driver.getId().getBytes();

        NdefRecord ndefRecordOut = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[] {},
                bytesOut);

        NdefMessage ndefMessageout = new NdefMessage(ndefRecordOut);
        return ndefMessageout;
    }
}