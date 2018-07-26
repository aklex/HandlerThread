package com.flexdecision.ak_lex.handlerthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomHandlerThread extends HandlerThread {
    CustomHandler handler;
    private WeakReference<UiThreadCallback> uiThreadCallback;
    public static final String TAG = CustomHandlerThread.class.getSimpleName();

    public CustomHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new CustomHandler(getLooper());
    }

    public void setUiThreadCallback(UiThreadCallback callback){
        this.uiThreadCallback = new WeakReference<>(callback);
    }

    public void addMessage(Message message){
        if (handler != null){
            handler.sendMessage(message);
            //handler.sendMessageAtFrontOfQueue()
            //handler.sendMessageAtTime()
            //handler.sendMessageDelayed()
            //handler.sendMessage()
        }
    }

    private class CustomHandler extends Handler{
        public CustomHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Util.MESSAGE_JOB_STARTED_ID:
                    Bundle bundle = msg.getData();
                    String url = bundle.getString(Util.MESSAGE_URL);
                    loadBitmap(url);
                    break;
                case Util.MESSAGE_START_PROGRESS_BAR:
                    if(uiThreadCallback != null & uiThreadCallback.get() != null){
                        uiThreadCallback.get().changeProgress(true);
                        Log.d(TAG, "Started");
                    }
                    break;
                default:
                        break;
            }
        }
    }

    private void loadBitmap(String strUrl){
        try {
            Log.d(TAG, "Load is starting");
            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                Log.d(TAG, "OK_STATUS");
                InputStream in = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                if(uiThreadCallback != null & uiThreadCallback.get() != null){
                    uiThreadCallback.get().publishToUiThread(strUrl, bitmap);
                    if (!this.handler.hasMessages(Util.MESSAGE_JOB_STARTED_ID)){
                        uiThreadCallback.get().changeProgress(false);
                        Log.d(TAG, "Finished");
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
