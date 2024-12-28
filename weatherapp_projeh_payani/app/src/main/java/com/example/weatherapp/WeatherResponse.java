package com.example.weatherapp;

import java.util.List;

public class WeatherResponse {
    public Main main;
    public List<Weather> weather;
    public Wind wind;
    public String name;

    public class Main {
        public double temp;
        public double humidity;
    }

    public class Weather {
        public String description;
        public String icon;
    }

    public class Wind {
        public double speed;
    }
}
