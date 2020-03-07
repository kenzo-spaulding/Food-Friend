package com.example.foodie_friend.frontend.dependencies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.firebase.database.core.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

// Downloads the image from the url
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bitMap;

    public DownloadImage(ImageView bitMap){
        this.bitMap = bitMap;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urls_to_display = urls[0];
        Bitmap connect = null;
        URI uri = null;
        try {
            uri = new URI(urls_to_display);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try{
            if (uri != null) {
                InputStream in = new java.net.URL(uri.toString()).openStream();
                connect = BitmapFactory.decodeStream(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    protected void onPostExecute(Bitmap map){
        if (map != null)
            bitMap.setImageBitmap(map);
    }
}