package com.example.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.widget.Toast;

public class SystemReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // Check for active network capabilities
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                    Toast.makeText(context, "اینترنت متصل است", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "اتصال اینترنت برقرار نیست", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "اتصال اینترنت برقرار نیست", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
