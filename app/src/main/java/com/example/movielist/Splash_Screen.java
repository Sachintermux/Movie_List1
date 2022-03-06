package com.example.movielist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;

import java.net.MalformedURLException;
import java.net.URL;

public class Splash_Screen extends AppCompatActivity {
GetDataFromAPI fromAPI;
String data;
CountDownTimer countDownTimer;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        fromAPI = new GetDataFromAPI();
        URL url = null;
        try {
            url = new URL("https://api.tvmaze.com/search/shows?q=All");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL finalUrl = url;
      countDownTimer =  new CountDownTimer(1000,500){

            @Override
            public void onTick( long l ) {

            }

            @Override
            public void onFinish() {
                data = fromAPI.makeHttpRequest(finalUrl);
                Intent intent = new Intent(Splash_Screen.this,MainActivity.class);
                intent.putExtra("Data",data);
                startActivity(intent);
                countDownTimer.cancel();
                finish();
            }
        }.start();

    }


}
