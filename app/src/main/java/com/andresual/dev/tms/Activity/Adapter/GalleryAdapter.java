package com.andresual.dev.tms.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andresual.dev.tms.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends BaseRecyclerAdapter<String, GalleryAdapter.ViewHolder> {

    Context context;

    public GalleryAdapter(List<String> data) {
        super(data);
    }

    public GalleryAdapter() {
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(getData().get(position)).into(holder.img);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!= null){
                        onItemClickListener.onItemClick(getAdapterPosition(),v);
                    }
                }
            });
        }
    }
}
