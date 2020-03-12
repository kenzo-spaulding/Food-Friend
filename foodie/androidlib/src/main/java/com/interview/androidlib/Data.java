package com.interview.androidlib;

import android.widget.ImageView;

public class Data {
    boolean liked;
    String category;

    public Data(String category, Boolean liked) {
        this.liked = liked;
        this.category = category;
    }

    public boolean getLiked() {
        return liked;
    }

    public String getCategory() {
        return category;
    }


}
