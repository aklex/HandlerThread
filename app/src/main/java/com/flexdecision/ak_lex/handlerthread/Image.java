package com.flexdecision.ak_lex.handlerthread;

import android.graphics.Bitmap;

public class Image {
    String url;
    Bitmap bitmap;

    public Image(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
