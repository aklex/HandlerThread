package com.flexdecision.ak_lex.handlerthread;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;

public class Util {
    public static final int MESSAGE_JOB_STARTED_ID = 1;
    public static final int MESSAGE_JOB_FINISHED_ID = 2;
    public static final int MESSAGE_START_PROGRESS_BAR = 4;
    public static final int MESSAGE_STOP_PROGRESS_BAR = 5;
    public static final String MESSAGE_URL = "MESSAGE_URL";
    public static final String MESSAGE_BITMAP = "MESSAGE_BITMAP";

    public static Message createMessage(int id){
        Message message = new Message();
        message.what = id;
        return message;
    }
    public static Message createMessage(int id, String url){
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_URL, url);
        message.what = id;
        message.setData(bundle);
        return message;
    }
    public static Message createMessage(int id, String url, Bitmap bitmap){
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_URL, url);
        bundle.putParcelable(MESSAGE_BITMAP, bitmap);
        message.what = id;
        message.setData(bundle);
        return message;
    }
}
