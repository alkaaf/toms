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

import com.andresual.dev.tms.Activity.Model.DepoModel;
import com.andresual.dev.tms.Activity.Model.DermagaModel;
import com.andresual.dev.tms.R;

import java.util.ArrayList;

public class DermagaListAdapter extends RecyclerView.Adapter<DermagaListAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<DermagaModel> dermagaModelArrayList;
    private ArrayList<DermagaModel> dermagaModelArrayListFlitered;

    public DermagaListAdapter(Context mContext, ArrayList<DermagaModel> dermagaModelArrayList) {
        this.mContext = mContext;
        this.dermagaModelArrayList = dermagaModelArrayList;
        this.dermagaModelArrayListFlitered = dermagaModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dermaga, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DermagaModel dermagaModel = dermagaModelArrayListFlitered.get(position);
        holder.tvDermaga.setText(dermagaModel.getNama());
    }

    @Override
    public int getItemCount() {
        return dermagaModelArrayListFlitered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDermaga;
        Context ctx;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);
            tvDermaga = view.findViewById(R.id.tv_dermaga);
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DermagaModel dermagaModel = dermagaModelArrayListFlitered.get(position);
            Intent intent = new Intent();
            intent.putExtra("iddermaga", dermagaModel.getId());
            intent.putExtra("namadermaga", dermagaModel.getNama());
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
                    dermagaModelArrayListFlitered = dermagaModelArrayList;
                } else {
                    ArrayList<DermagaModel> filteredList = new ArrayList<>();
                    for (DermagaModel row : dermagaModelArrayList) {
                        if (row.getNama().toLowerCase().contains(charString)){
                            filteredList.add(row);
                        }
                    }
                    dermagaModelArrayListFlitered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dermagaModelArrayListFlitered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dermagaModelArrayListFlitered = (ArrayList<DermagaModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
