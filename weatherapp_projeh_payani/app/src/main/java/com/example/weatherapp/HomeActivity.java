package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    private TextView cityNameText, temperatureText, humidityText, descriptionText, windText;
    private ImageView weatherIcon;
    private EditText cityNameInput;
    private SystemReceiver internetReceiver;
    private static final String API_KEY = "93e2322d3103f8d17414b56c90b592fb";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cityNameText = findViewById(R.id.cityNameText);
        temperatureText = findViewById(R.id.temperatureText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);
        descriptionText = findViewById(R.id.descriptionText);
        weatherIcon = findViewById(R.id.weatherIcon);
        Button refreshButton = findViewById(R.id.fetchWeatherButton);
        cityNameInput = findViewById(R.id.cityNameInput);

        refreshButton.setOnClickListener(view -> {
            String cityName = cityNameInput.getText().toString();
            if (!cityName.isEmpty()) {
                fetchWeatherData(cityName);
            } else {
                cityNameInput.setError("Please enter a city name");
            }
        });


        internetReceiver = new SystemReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetReceiver, filter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Internet is available", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onLost(Network network) {
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Internet is lost", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (internetReceiver != null) {
            unregisterReceiver(internetReceiver);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(new ConnectivityManager.NetworkCallback());
        }
    }

    private void fetchWeatherData(String cityName) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/weather/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        Call<WeatherResponse> call = weatherApi.getWeather(cityName, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(HomeActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateUI(WeatherResponse weatherResponse) {
        if (weatherResponse != null) {
            double temperature = weatherResponse.main.temp;
            double humidity = weatherResponse.main.humidity;
            double windSpeed = weatherResponse.wind.speed;
            String description = weatherResponse.weather.get(0).description;
            String iconCode = weatherResponse.weather.get(0).icon;

            String resourceName = "ic_" + iconCode;
            int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            weatherIcon.setImageResource(resId);

            cityNameText.setText(weatherResponse.name);
            temperatureText.setText(String.format("%.0f°", temperature));
            humidityText.setText(String.format("%.0f%%", humidity));
            windText.setText(String.format("%.0f km/h", windSpeed));
            descriptionText.setText(description);
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }
    }
}
