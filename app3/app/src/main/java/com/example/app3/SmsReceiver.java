package com.example.app3;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;
import android.widget.EditText;

import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {

    private final EditText editText;

    public SmsReceiver(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER + " LIMIT 1");
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String messageBody = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                editText.setText(messageBody);
                cursor.close();
            }
        }
    }
}