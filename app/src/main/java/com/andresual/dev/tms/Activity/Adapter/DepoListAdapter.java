package com.andresual.dev.tms.Activity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.ListDepoActivity;
import com.andresual.dev.tms.Activity.Model.DepoModel;
import com.andresual.dev.tms.R;

import java.util.ArrayList;
import java.util.List;

public class DepoListAdapter extends RecyclerView.Adapter<DepoListAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<DepoModel> depoModelArrayList;
    private ArrayList<DepoModel> depoModelArrayListFiltered;

    public DepoListAdapter(Context mContext, ArrayList<DepoModel> depoModelArrayList) {
        this.mContext = mContext;
        this.depoModelArrayList = depoModelArrayList;
        this.depoModelArrayListFiltered = depoModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_depo, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DepoModel depoModel = depoModelArrayListFiltered.get(position);
        holder.tvDepo.setText(depoModel.getNama());
    }

    @Override
    public int getItemCount() {
        return depoModelArrayListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDepo;
        Context ctx;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);
            tvDepo = view.findViewById(R.id.tv_depo);
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DepoModel depoModel = depoModelArrayListFiltered.get(position);
            Intent intent = new Intent();
            intent.putExtra(ListDepoActivity.INTENT_DATA, depoModel);
            ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
            ((Activity) mContext).finish();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    depoModelArrayListFiltered = depoModelArrayList;
                } else {
                    ArrayList<DepoModel> filteredList = new ArrayList<>();
                    for (DepoModel row : depoModelArrayList) {
                        if (row.getNama().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
                    }
                    depoModelArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = depoModelArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                depoModelArrayListFiltered = (ArrayList<DepoModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
