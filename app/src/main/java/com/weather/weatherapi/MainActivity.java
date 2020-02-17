package com.weather.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.weather.weatherapi.Network.ConnectorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText city;
    TextView temp,weather_desc,precip,humidity,wind_speed;
    ImageView weather_icon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.city);
        temp = findViewById(R.id.temp);
        weather_desc = findViewById(R.id.weather_desc);
        weather_icon = findViewById(R.id.weather_icon);
        precip = findViewById(R.id.precip);
        humidity = findViewById(R.id.humidity);
        wind_speed = findViewById(R.id.wind_speed);

        ColorStateList originalColor = weather_desc.getTextColors();

        city.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getCurrentWeather(city.getText().toString());
                    return true;
                }
                return false;
            }
        });

        getCurrentWeather(city.getText().toString());
    }

    public void getCurrentWeather(String city_q){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectorService.BaseURL)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        ConnectorService connectorService = retrofit.create(ConnectorService.class);

        connectorService.getWeather("1579ec14fe24d755677ee975151f1b19",city_q).enqueue(new Callback<com.weather.weatherapi.Model.MainActivity>() {
            @Override
            public void onResponse(Call<com.weather.weatherapi.Model.MainActivity> call, Response<com.weather.weatherapi.Model.MainActivity> response) {
                Log.d("TTT", "onResponse: "+call.request().url());
                if (response.isSuccessful()){
                    Log.d("RRR", "onResponse: "+response.code());
                    Log.d("TTT", "onResponse: "+city.getText()+"");
                    if (response.body().getRequest() == null){
                        Toast.makeText(MainActivity.this,"please enter a valid city name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    city.setText(response.body().getRequest().getQuery());
                    temp.setText(response.body().getCurrent().getTemperature()+"");
                    weather_desc.setText(response.body().getCurrent().getWeatherDescriptions().get(0)+"");
                    Picasso.get().load(response.body().getCurrent().getWeatherIcons().get(0)).fit().into(weather_icon);
                    precip.setText(response.body().getCurrent().getPrecip()+"%");
                    humidity.setText(response.body().getCurrent().getHumidity()+"%");
                    wind_speed.setText(response.body().getCurrent().getWindSpeed()+" km/h");

                }
            }

            @Override
            public void onFailure(Call<com.weather.weatherapi.Model.MainActivity> call, Throwable t) {
                Log.d("TTT", "onFailure: "+t.getMessage());
            }
        });

    }
}
