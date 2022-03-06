package com.example.movielist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Detailed_Activity extends AppCompatActivity {
    String TAG = "Hello";
    String data;
    String imageUrl = null;
    private ImageView imageView;
    private TextView name, rating, summary, premiered, ended, country, language, type, status;
    private String officialSiteLink = "";
    private ProgressBar progressBar;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        initView();
        JSONObject jsonObject1 = DataShareActivity.getJsonObject();
        try {
            JSONObject showObject = jsonObject1.getJSONObject("show");
            name.setText(showObject.getString("name"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                summary.setText(Html.fromHtml(summary.getText() + showObject.getString("summary"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                summary.setText(Html.fromHtml(summary.getText() + showObject.getString("summary")));
            }
            type.setText(type.getText() + showObject.getString("type"));
            language.setText(language.getText() + showObject.getString("language"));
            status.setText(status.getText() + showObject.getString("status"));
            premiered.setText(premiered.getText() + showObject.getString("premiered"));
            ended.setText(ended.getText() + showObject.getString("ended"));
            officialSiteLink = showObject.getString("officialSite");
            rating.setText(rating.getText() + showObject.getJSONObject("rating").getString("average"));
            country.setText(country.getText() + showObject.getJSONObject("network").getJSONObject("country").getString("name"));
            imageUrl = showObject.getJSONObject("image").getString("original");
//            if(imageUrl == null){
//                imageUrl = showObject.getJSONObject("image").getString("medium");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: " + e);
        }
        new CountDownTimer(100, 100) {

            @Override
            public void onTick( long l ) {

            }

            @Override
            public void onFinish() {
                setImage(imageUrl);
            }
        }.start();
    }

    private Bitmap loadImages( String url ) throws IOException {
        URL url1 = new URL(url);
        InputStream inputStream = (InputStream) url1.getContent();
        return BitmapFactory.decodeStream(inputStream);
    }

    private void setImage( String imageUrl ) {
        try {
            if (imageUrl != null) {
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Bitmap bitmap = loadImages(imageUrl);
                imageView.setImageBitmap(bitmap);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private void initView() {
        imageView = findViewById(R.id.movieIcon_detailedA);
        name = findViewById(R.id.movieName_txt_detailed);
        rating = findViewById(R.id.movieRating_txt_detailed);
        summary = findViewById(R.id.movieSummary_txt_detailed);
        premiered = findViewById(R.id.moviePremiered_txt_detailed);
        ended = findViewById(R.id.movieEnded_txt_detailed);
        country = findViewById(R.id.movieCountry_txt_detailed);
        language = findViewById(R.id.movieLanguage_txt_detailed);
        type = findViewById(R.id.movieType_txt_detailed);
        status = findViewById(R.id.movieStatus_txt_detailed);
        progressBar = findViewById(R.id.movieProgress_detailed);
        imageView.setVisibility(View.GONE);
    }

    public void GotoSite( View view ) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(officialSiteLink));
        startActivity(intent);
    }
}