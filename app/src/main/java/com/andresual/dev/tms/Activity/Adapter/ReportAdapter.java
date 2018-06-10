package com.andresual.dev.tms.Activity.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Model.Report;
import com.andresual.dev.tms.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAdapter extends BaseRecyclerAdapter<Report, ReportAdapter.ViewHolder> {
    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        Report report = getData().get(position);
        holder.tvTime.setText(report.getParsedReportData());
        holder.tvLocation.setText(report.getLokasi());
        holder.tvRemarks.setText(report.getCatatan());
        holder.tvVehicle.setText(report.getNopol() + " - " + report.getKode());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.tvVehicle)
        TextView tvVehicle;
        @BindView(R.id.tvRemarks)
        TextView tvRemarks;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
