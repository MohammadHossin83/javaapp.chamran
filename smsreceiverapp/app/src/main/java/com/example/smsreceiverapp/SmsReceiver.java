package com.example.smsreceiverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    // In method vaghti ejra mishe ke yek signal (broadcast)e SMS, az system daryaft mishe
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras(); // Check mikone ke aya broadcast baraye ye SMS hast ya na. Agebood, ba bundle dataye kham ro daryaft mikone

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format"); // Age dataye daryafti null nabood, ba estefade az "pdus" dataye kham SMS ro daryaft mikone

                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format); // Halghe for dar tamam bakhsh haye SMS daryafti gardesh mikone va har PDUye kham ro be payam ghabel khoondan tabdil mikone
                        String message = smsMessage.getMessageBody(); // Ye moteghayere String baraye zakhire SMS misaze va ba method getMessageBody() badane matn ro migire

                        ((MainActivity) context).updateSmsEditText(message); // Methode updateSmsEditText() dar Main ro seda mizane va meghdar moteghayere message ro behesh pass mide
                    }
                }
            }
        }
    }

}