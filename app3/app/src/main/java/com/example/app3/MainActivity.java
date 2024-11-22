package com.example.app3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SMS_PERMISSION = 1;
    private EditText editText;
    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS_PERMISSION);
        } else {
            displayLastSms();
            registerSmsReceiver();
        }
    }

    private void displayLastSms() {
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER + " LIMIT 1");

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String messageBody = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
            editText.setText(messageBody);
            cursor.close();
        } else {
            Toast.makeText(this, "پیامکی یافت نشد", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerSmsReceiver() {
        smsReceiver = new SmsReceiver(editText);
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayLastSms();
                registerSmsReceiver();
            } else {
                Toast.makeText(this, "اجازه خواندن پیامک داده نشد", Toast.LENGTH_SHORT).show();
            }
        }
    }
}