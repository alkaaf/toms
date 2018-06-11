package com.spil.dev.tms.Activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public  interface OnItemClickListener{
        void onItemClick(int pos, View v);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    List<T> data;

    public BaseRecyclerAdapter(List<T> data) {
        this.data = data;
    }

    public BaseRecyclerAdapter() {
        this.data = new ArrayList<>();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
