package com.flexdecision.ak_lex.handlerthread;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UiThreadCallback {
    public static final String TAG = MainActivity.class.getSimpleName();
    private CustomHandlerThread handlerThread;
    private UiHandler uiHandler;

    private TextView mDisplayTextView;
    private ImageView imageView;

    private RecyclerView recyclerView;
    private RVAdapter adapter;

    private ImageManager imageManager;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        imageManager = ImageManager.getInstance();

        recyclerView = findViewById(R.id.imageList);
        adapter = new RVAdapter(imageManager.getImages());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        uiHandler = new UiHandler();
        uiHandler.setContext(this);

        handlerThread = new CustomHandlerThread("HandlerThread");
        handlerThread.setUiThreadCallback(this);
        handlerThread.start();

    }

    @Override
    public void changeProgress(boolean working) {
        if (uiHandler != null) {
            if (working)
                uiHandler.sendEmptyMessage(Util.MESSAGE_START_PROGRESS_BAR);
            else
                uiHandler.sendEmptyMessage(Util.MESSAGE_STOP_PROGRESS_BAR);
        }
    }

    public void startTasks(View v){

        String[] urlsArray = getResources().getStringArray(R.array.urls);
        if (urlsArray.length > 0){
            handlerThread.addMessage(Util.createMessage(Util.MESSAGE_START_PROGRESS_BAR));
        }

        for(int i=0; i< urlsArray.length; i++) {
            Log.d(TAG, "url: " + urlsArray[i]);
            handlerThread.addMessage(Util.createMessage(Util.MESSAGE_JOB_STARTED_ID, urlsArray[i]));

        }
    }

    public void cancelTasks(View v){
        cancelTasks();

    }

    @Override
    public void publishToUiThread(String url, Bitmap bitmap) {
        if (uiHandler != null){
            uiHandler.sendMessage(Util.createMessage(Util.MESSAGE_JOB_FINISHED_ID, url, bitmap));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTasks();
    }

    private void cancelTasks(){
        if (handlerThread != null){
            handlerThread.quit();
            handlerThread.interrupt();
            if (progressBar != null){
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private static class UiHandler extends Handler {
        private WeakReference<MainActivity> mWeakRefContext;

        public void setContext(MainActivity context){
            mWeakRefContext = new WeakReference<>(context);
        }

        // simply show a toast message
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Util.MESSAGE_JOB_FINISHED_ID:
                    Bundle bundle1 = msg.getData();
                    String url = bundle1.getString(Util.MESSAGE_URL);
                    Bitmap bitmap = bundle1.getParcelable(Util.MESSAGE_BITMAP);

                    ImageManager imageManager = ImageManager.getInstance();
                    List<Image> images = imageManager.getImages();
                    images.add(new Image(url, bitmap));
                    int lastElement = images.size() - 1;
                    if(mWeakRefContext != null && mWeakRefContext.get() != null){
                        mWeakRefContext.get().adapter.notifyDataSetChanged();
                        mWeakRefContext.get().recyclerView.scrollToPosition(lastElement);
                    }
                    break;
                case Util.MESSAGE_STOP_PROGRESS_BAR:
                    if(mWeakRefContext != null && mWeakRefContext.get() != null){
                        mWeakRefContext.get().progressBar.setVisibility(View.INVISIBLE);
                    }
                    break;
                case Util.MESSAGE_START_PROGRESS_BAR:
                    if(mWeakRefContext != null && mWeakRefContext.get() != null){
                        mWeakRefContext.get().progressBar.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
