package com.maid.gardeningfriend.clima;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maid.gardeningfriend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import com.bumptech.glide.Glide;

public class clima extends AppCompatActivity {
    private EditText etCity;
    private TextView tvResult;
    private ImageView weatherIcon;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "466d16634da02465cc9c56127129c567";
    private DecimalFormat df = new DecimalFormat("#.##");
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
        weatherIcon = findViewById(R.id.weatherIcon);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedCity = preferences.getString("city", "");
        etCity.setText(savedCity);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeatherDetails(null);
                handler.postDelayed(this, 60000);
            }
        };
        handler.postDelayed(runnable, 60000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city", etCity.getText().toString().trim());
        editor.apply();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 60000);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("El campo ciudad no puede estar vacío!");
        }else{
            try {
                String encodedCity = URLEncoder.encode(city + ",AR", "UTF-8");
                tempUrl = url + "?q=" + encodedCity + "&appid=" + appid;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                tvResult.setText("Error al codificar la ciudad.");
                return;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");

                        // Obtener el código del ícono
                        JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                        String iconCode = weatherArray.getJSONObject(0).getString("icon");

                        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                        // Cargar el ícono en la ImageView usando Glide
                        Glide.with(clima.this).load(iconUrl).into(weatherIcon);
                        tvResult.setTextColor(Color.rgb(0,0,0));
                        output += "Clima actual de " + cityName + " (" + countryName + ")"
                                + "\nTemperatura: " + df.format(temp) + " °C"
                                + "\nSensación térmica de: " + df.format(feelsLike) + " °C"
                                + "\nHumedad: " + humidity + "%"
                                + "\nViento: " + wind + "m/s (metros por segundo)"
                                + "\nNubosidad: " + clouds + "%";

                        tvResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
