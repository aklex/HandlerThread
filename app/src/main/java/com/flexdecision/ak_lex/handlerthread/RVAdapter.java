package com.flexdecision.ak_lex.handlerthread;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    public static final String TAG = RVAdapter.class.getSimpleName();
    private List<Image> urls;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    public RVAdapter(List<Image> urls) {
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image item = urls.get(position);
        if (!item.url.isEmpty() && item.bitmap != null) {
            holder.imageView.setImageBitmap(item.bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }
}
