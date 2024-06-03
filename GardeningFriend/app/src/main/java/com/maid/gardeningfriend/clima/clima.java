package com.maid.gardeningfriend.clima;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;
import com.maid.gardeningfriend.favoritos.FavoritosSeccion;
import com.maid.gardeningfriend.inicio.Inicio;
import com.maid.gardeningfriend.login.Login;
import com.maid.gardeningfriend.perfil.Perfil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class clima extends MainActivity {
    private EditText etCity;
    private TextView tvResult;
    private ImageView weatherIcon;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "466d16634da02465cc9c56127129c567";
    private DecimalFormat df = new DecimalFormat("#.##");
    private Handler handler;
    private Runnable runnable;
    private static final String TAG = "clima";

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

        //Inicia logica para la barra de navegacion de abajo
        BottomNavigationView bottomNavigationView = findViewById(R.id.barraMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.inicio) {
                    Intent inicio = new Intent(clima.this, Inicio.class);
                    startActivity(inicio);
                    return true;
                } else if(item.getItemId() == R.id.perfil) {
                    Intent perfil = new Intent(clima.this, Perfil.class);
                    startActivity(perfil);
                    return true;
                } else if (item.getItemId() == R.id.favoritos) {
                    Intent fav = new Intent(clima.this, FavoritosSeccion.class);
                    startActivity(fav);
                    return true;
                } else if (item.getItemId() == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent logout = new Intent(clima.this, Login.class);
                    startActivity(logout);
                    finish();
                    return true;
                }
                return false;
            }
        });

        //Termina logica para la barra de navegacion de abajo
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
                //SSLUtil.allowAllSSL(); // Permitir todas las versiones de TLS

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

                        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                        // Verificar URL del ícono
                        Log.d(TAG, "Icon URL: " + iconUrl);

                         //Cargar el ícono en la ImageView usando Picasso
                                 Picasso.get()
                                .load(iconUrl)
                                .into(weatherIcon);

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
