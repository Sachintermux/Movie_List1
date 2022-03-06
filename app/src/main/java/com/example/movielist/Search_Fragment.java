package com.example.movielist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Search_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textView;

    public void setData( String data ) {
        this.data = data;
        arrayList = new ArrayList<>();
        imageUrl = new ArrayList<>();
        jsonObjectArrayList = new ArrayList<>();
        setGridView();
    }
private void setGridView(){
        loadNameAndSet(data);
    loadImagesAndSet(arrayList,imageUrl);

    adapter = new GridView_adapter(requireActivity(),arrayList);
    gridView.setAdapter(adapter);

}
    private String data;
    ArrayList<MovieName_Image> arrayList = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    GridView gridView;
    Bitmap bitmap;
    GridView_adapter adapter;
    CountDownTimer countDownTimer;
    String TAG  = "Hello";

    public ArrayList<JSONObject> getJsonObjectArrayList() {
        return jsonObjectArrayList;
    }

    ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
    private String mParam1;
    private String mParam2;

    public Search_Fragment() {
    }

    public static Search_Fragment newInstance( String param1, String param2 ) {
        Search_Fragment fragment = new Search_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragment_search_, container, false);
 gridView = view.findViewById(R.id.fragment_searchMovie);
        return view;
    }


    private void loadNameAndSet( String data ) {
        try {
            arrayList.addAll(setArrayList(data));
            adapter = new GridView_adapter(requireActivity(), arrayList);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<MovieName_Image> setArrayList( String data ) throws Exception {
        JSONArray jsonArray1 = new JSONArray(data);
        jsonObjectArrayList = new ArrayList<>();
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

    private void loadImagesAndSet(ArrayList<MovieName_Image> arrayList,ArrayList<String> imageUrl) {

        countDownTimer = new CountDownTimer(500, 100) {
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
                        adapter = new GridView_adapter(requireActivity(), arrayList);
                        gridView.setAdapter(adapter);
                        if (i == imageUrl.size() - 1) {
                            countDownTimer.cancel();
                        }
                    }
                }
            }
        }.start();

    }



}