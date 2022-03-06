package com.example.movielist;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class GridView_adapter extends BaseAdapter {
ArrayList<MovieName_Image> movieName_images = new ArrayList<>();
private Context context;
private click listener;
String TAG = "Hello";
    public GridView_adapter( Context context, ArrayList<MovieName_Image> movieName_images ) {
        this.context =context;
        this.movieName_images = movieName_images;
        listener = (click) context;
    }

    @Override
    public int getCount() {
        return movieName_images.size();
    }

    @Override
    public Object getItem( int i ) {
        return null;
    }

    @Override
    public long getItemId( int i ) {
        return 0;
    }

    @Override
    public View getView( int i, View convertView, ViewGroup viewGroup ) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.singal_movie_icon, viewGroup, false);
        } else {
            gridView = convertView;
        }
        ImageView imageView = gridView.findViewById(R.id.movie_icon_child);
        TextView textView = gridView.findViewById(R.id.movie_name_txt);
        ProgressBar progressBar = gridView.findViewById(R.id.movie_progress_child);

        if(movieName_images.get(i).getBitmap() == null) {
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(movieName_images.get(i).getBitmap());
        }

        textView.setText(movieName_images.get(i).getName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
              listener.imageClick(i);

            }
        });

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                listener.imageClick(i);
            }
        });

        return gridView;
    }
    public interface click{
        void imageClick(int position);
    }
}
