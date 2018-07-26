package com.flexdecision.ak_lex.handlerthread;

import java.util.ArrayList;
import java.util.List;

public class ImageManager {
    public List<Image> getImages() {
        return images;
    }

    private List<Image> images;

    public static final String TAG = ImageManager.class.getSimpleName();
    private static ImageManager instance;
    private ImageManager(){
        images = new ArrayList<>();
    }
    public static ImageManager getInstance(){
        if (instance == null){
            synchronized (ImageManager.class){
                instance = new ImageManager();
            }
        }
        return instance;
    }
}
