package com.example.movielist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GridView_adapter.click, BottomNavigationView.OnNavigationItemSelectedListener {
    ArrayList<MovieName_Image> arrayList = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    GridView gridView;
    Bitmap bitmap;
    GridView_adapter adapter;
    CountDownTimer countDownTimer;
    String TAG = "Hello";
    ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
    private GetDataFromAPI fromAPI;
    private TextView textView;
    private String data;
    private SearchView searchView;
    private Search_Fragment search_fragment;
    private View fragmentView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadNameAndSet(data);
        loadImagesAndSet(arrayList, imageUrl);
        data = loadNewData("https://api.tvmaze.com/search/shows?q=B");
        data = loadNewData("https://api.tvmaze.com/search/shows?q=C");

    }

    private void initView() {
        data = getIntent().getStringExtra("Data");
        textView = findViewById(R.id.textView);
        fromAPI = new GetDataFromAPI();
        gridView = findViewById(R.id.gridView_allMovie);
        searchView = findViewById(R.id.search_viewMain);
        bottomNavigationView = findViewById(R.id.bottom_navigationMain);
        search_fragment = new Search_Fragment();
        fragmentView = findViewById(R.id.fragment_search_main);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_search_main, search_fragment);
        ft.commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.home_tab);
        fragmentView.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
    }

    private String loadNewData( String url ) {
        String data = "";
        try {
            URL url1 = new URL(url);
            data = fromAPI.makeHttpRequest(url1);
            loadNameAndSet(data);
            loadImagesAndSet(arrayList, imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void loadNameAndSet( String data ) {
        try {
            arrayList.addAll(setArrayList(data));
            adapter = new GridView_adapter(MainActivity.this, arrayList);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<MovieName_Image> setArrayList( String data ) throws Exception {
        JSONArray jsonArray1 = new JSONArray(data);
        ArrayList<MovieName_Image> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject object1 = (JSONObject) jsonArray1.get(i);
            jsonObjectArrayList.add(object1);
            MovieName_Image movieName_image = new MovieName_Image(null, object1.getJSONObject("show").getString("name"));
            arrayList.add(movieName_image);
            try {
                JSONObject object2 = object1.getJSONObject("show").getJSONObject("image");
                String imageUrl1 = object2.getString("medium");

                imageUrl.add(imageUrl1);
            } catch (Exception e) {

                imageUrl.add(null);
            }

        }
        return arrayList;
    }

    private Bitmap loadImages( String url ) throws IOException {
        URL url1 = new URL(url);
        InputStream inputStream = (InputStream) url1.getContent();
        return BitmapFactory.decodeStream(inputStream);
    }

    private void loadImagesAndSet( ArrayList<MovieName_Image> arrayList, ArrayList<String> imageUrl ) {

        countDownTimer = new CountDownTimer(2000, 500) {
            @Override
            public void onTick( long l ) {

            }

            @Override
            public void onFinish() {
                for (int i = 0; i < imageUrl.size(); i++) {
                    if (imageUrl.get(i) != null && arrayList.get(i).getBitmap() == null) {
                        try {
                            bitmap = loadImages(imageUrl.get(i));
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        String name = arrayList.get(i).getName();
                        arrayList.remove(i);
                        MovieName_Image movieName_image = new MovieName_Image(bitmap, name);
                        arrayList.add(i, movieName_image);
                        adapter = new GridView_adapter(MainActivity.this, arrayList);
                        gridView.setAdapter(adapter);
                        if (i == imageUrl.size() - 1) {
                            countDownTimer.cancel();
                        }
                    }
                }
            }
        }.start();

    }


    @Override
    public boolean onNavigationItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()) {
            case R.id.home_tab:
                showHomeTab();
                return true;

            case R.id.search_tab:
                showSearchTab();
                return true;
        }
        return false;
    }

    private void showSearchTab() {
        gridView.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit( String query ) {
                String search_url = "https://api.tvmaze.com/search/shows?q=" + query;
                search_fragment.setData(loadNewData(search_url));
                fragmentView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange( String newText ) {

                return false;
            }
        });
    }

    private void showHomeTab() {
        searchView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        fragmentView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

    }

    @Override
    public void imageClick( int position ) {
        if (gridView.getVisibility() == View.VISIBLE)
            DataShareActivity.setJsonObject(jsonObjectArrayList.get(position));
        else
            DataShareActivity.setJsonObject(search_fragment.getJsonObjectArrayList().get(position));

        startActivity(new Intent(MainActivity.this, Detailed_Activity.class));
    }


}