package com.example.michael.bumpy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.michael.bumpy.Model.Driver;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends Activity implements
        NfcAdapter.CreateNdefMessageCallback {
    Driver driver;
    EditText textIn;
    EditText textOut;
    private static final String TAG = "MainActivity";

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textIn = (EditText) findViewById(R.id.info);
        textOut = (EditText)findViewById(R.id.textout);
        driver = Driver.getInstance();

        Log.d(TAG, "onCreate: ");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter==null){
            Toast.makeText(MainActivity.this,
                    "nfcAdapter==null, no NFC adapter exists",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this,
                    "Set Callback(s)",
                    Toast.LENGTH_LONG).show();
            try {
                AccidentDetailsActivity activity = new AccidentDetailsActivity();
                nfcAdapter.setNdefPushMessageCallback(this, activity);
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
            textOut.setText(inMsg);
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