package com.interview.androidlib;

import android.widget.ImageView;

public class ImageAPI {
    private String name;
    private String url;
    public ImageAPI(String name, String url){
        this.name = name;
        this.url = url;
    }
    void setImage(ImageView imageView){
        new DownloadImage((ImageView) imageView).execute(url);
    }
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getImageResource() {return 0; }
}
