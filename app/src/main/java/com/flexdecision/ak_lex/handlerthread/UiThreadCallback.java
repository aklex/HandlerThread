package com.flexdecision.ak_lex.handlerthread;

import android.graphics.Bitmap;
import android.os.Message;

public interface UiThreadCallback {
    void publishToUiThread(String url, Bitmap bitmap);
    void changeProgress(boolean working);
}
