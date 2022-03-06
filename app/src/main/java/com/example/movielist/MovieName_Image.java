package com.example.movielist;

import android.graphics.Bitmap;

public class MovieName_Image {
    Bitmap mBitmap;
    String mName;

    public MovieName_Image( Bitmap Bitmap, String Name ) {
        this.mBitmap = Bitmap;
        this.mName = Name;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap( Bitmap mBitmap ) {
        this.mBitmap = mBitmap;
    }

    public String getName() {
        return mName;
    }

    public void setName( String mName ) {
        this.mName = mName;
    }
}
