package com.andresual.dev.tms.Activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.ArriveDestinationActivity;
import com.andresual.dev.tms.Activity.FinishDischargeActivity;
import com.andresual.dev.tms.Activity.FinishStuffPickupActivity;
import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.MengantarActivity;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.Activity.OrderBaruActivity;
import com.andresual.dev.tms.Activity.ProsesActivity.ActivityProses1And2;
import com.andresual.dev.tms.Activity.ProsesActivity.ActivityProses3;
import com.andresual.dev.tms.Activity.ProsesActivity.ActivityProsesFrom4To7;
import com.andresual.dev.tms.Activity.ProsesActivity.ActivityProsesMoreThan8;
import com.andresual.dev.tms.Activity.ReadyToStuffPickupActivity;
import com.andresual.dev.tms.Activity.SiapAntarActivity;
import com.andresual.dev.tms.Activity.StartDischargeActivity;
import com.andresual.dev.tms.Activity.StartStuffPickupActivity;
import com.andresual.dev.tms.Dooring.MapsOrderDooringActivity;
import com.andresual.dev.tms.Dooring.MengantarDooringActivity;
import com.andresual.dev.tms.Dooring.MenurunkanDooringActivity;
import com.andresual.dev.tms.Dooring.OrderBaruDooringActivity;
import com.andresual.dev.tms.Dooring.SiapAntarDooringActivity;
import com.andresual.dev.tms.JobFinishedActivity;
import com.andresual.dev.tms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresual on 2/28/2018.
 */

public class SebelumnyaListAdapter extends RecyclerView.Adapter<SebelumnyaListAdapter.ViewHolder> implements Filterable {
    public static final String INTENT_DATA = "job.order.data";
    private Context mContext;
    private ArrayList<SimpleJob> simpleJobList;
    private List<SimpleJob> simpleJobListFiltered;
    private JobDetail2AdapterListener listener;

    public SebelumnyaListAdapter(Context mContext, ArrayList<SimpleJob> simpleJobList) {
        this.mContext = mContext;
        this.simpleJobList = simpleJobList;
        this.simpleJobListFiltered = simpleJobListFiltered;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvJobName, tvFrom, tvTo, tvStatus;
        Context ctx;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);
            tvJobName = (TextView) view.findViewById(R.id.tv_nama);
            tvFrom = (TextView) view.findViewById(R.id.tv_from1);
            tvTo = (TextView) view.findViewById(R.id.tv_to1);
            tvStatus = view.findViewById(R.id.tv_status);
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            SimpleJob simpleJob = simpleJobList.get(position);
            if (simpleJob.getJobType() <= 2) {
                ActivityProses1And2.startPreview(mContext, simpleJob);
            } else if (simpleJob.getJobType() <= 3) {
                ActivityProses3.startPreview(mContext, simpleJob);
            } else if (simpleJob.getJobType() <= 7) {
                ActivityProsesFrom4To7.startPreview(mContext, simpleJob);
            } else {
                ActivityProsesMoreThan8.startPreview(mContext, simpleJob);
            }
        }
    }

    @Override
    public SebelumnyaListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sebelumnya, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(SebelumnyaListAdapter.ViewHolder holder, int position) {
//        SimpleJob simpleJob = OperationalController.getmInstance().getSimpleJobArrayList().get(position);
//        SimpleJob simpleJob = BerandaFragment.getmInstance().getSimpleJobArrayList().get(position);
//        holder.tvJobName.setText(simpleJob.getJobName());
//        holder.tvFrom.setText(simpleJob.getOrigin());
//        holder.tvTo.setText(simpleJob.getDestination());
        holder.tvJobName.setText(simpleJobList.get(position).getJobName());
        holder.tvFrom.setText(simpleJobList.get(position).getOrigin());
        holder.tvTo.setText(simpleJobList.get(position).getDestination());
        holder.tvStatus.setText(simpleJobList.get(position).getStringDeliverStatus());

//        holder.tvStatus.setText(simpleJobList.get(position).getJobDeliverStatus());
    }

    @Override
    public int getItemCount() {
//        return OperationalController.getmInstance().getSimpleJobArrayList().size();
        return simpleJobList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    simpleJobListFiltered = simpleJobList;
                } else {
                    List<SimpleJob> filteredList = new ArrayList<>();
                    for (SimpleJob row : simpleJobList) {
                        if (row.getJobName().toLowerCase().contains(charString.toLowerCase()) || row.getJobName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    simpleJobListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = simpleJobListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                simpleJobListFiltered = (ArrayList<SimpleJob>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface JobDetail2AdapterListener {
        void onHsCodeSelected(SimpleJob fetchDashboardStuffingModel);
    }
}
