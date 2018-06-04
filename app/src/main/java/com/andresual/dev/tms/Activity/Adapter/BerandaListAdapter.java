package com.andresual.dev.tms.Activity.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.ArriveDestinationActivity;
import com.andresual.dev.tms.Activity.FinishDischargeActivity;
import com.andresual.dev.tms.Activity.FinishStuffPickupActivity;
import com.andresual.dev.tms.Activity.GoEmptyToDepoActivity;
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
import com.andresual.dev.tms.Activity.UploadEmptyToDepoActivity;
import com.andresual.dev.tms.Dooring.MapsOrderDooringActivity;
import com.andresual.dev.tms.Dooring.MengantarDooringActivity;
import com.andresual.dev.tms.Dooring.OrderBaruDooringActivity;
import com.andresual.dev.tms.Dooring.SiapAntarDooringActivity;
import com.andresual.dev.tms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresual on 1/30/2018.
 */

public class BerandaListAdapter extends RecyclerView.Adapter<BerandaListAdapter.ViewHolder> implements Filterable {


    private Context mContext;
    private ArrayList<SimpleJob> simpleJobList;
    private List<SimpleJob> simpleJobListFiltered;
    private JobDetail2AdapterListener listener;

    public BerandaListAdapter(Context mContext, ArrayList<SimpleJob> simpleJobList) {
        this.mContext = mContext;
        this.simpleJobList = simpleJobList;
        this.simpleJobListFiltered = simpleJobListFiltered;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvJobName, tvFrom, tvTo, tvStatus, tvto2;
        public RelativeLayout rl1;
        Context ctx;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);
            tvJobName = (TextView) view.findViewById(R.id.tv_nama);
            tvFrom = (TextView) view.findViewById(R.id.tv_from1);
            tvTo = (TextView) view.findViewById(R.id.tv_to1);
            tvto2 = view.findViewById(R.id.tv_to);
            tvStatus = view.findViewById(R.id.tv_status);
            rl1 = view.findViewById(R.id.rl1);

            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            SimpleJob simpleJob = simpleJobList.get(position);
            if (simpleJob.getJobType() <= 2) {
                ActivityProses1And2.startProses(mContext, simpleJob);
            } else if (simpleJob.getJobType() <= 3) {
                ActivityProses3.startProses(mContext, simpleJob);
            } else if (simpleJob.getJobType() <= 7) {
                ActivityProsesFrom4To7.startProses(mContext, simpleJob);
            } else {
                ActivityProsesMoreThan8.startProses(mContext, simpleJob);
            }
        }
    }

    @Override
    public BerandaListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_beranda, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(BerandaListAdapter.ViewHolder holder, int position) {

        holder.tvJobName.setText(simpleJobList.get(position).getJobName());
        holder.tvFrom.setText(simpleJobList.get(position).getOrigin());
        holder.tvTo.setText(simpleJobList.get(position).getDestination());
        SimpleJob simpleJob = simpleJobList.get(position);
        if (simpleJob.getJobDeliverStatus() == 1) {
            holder.tvStatus.setText("Waiting");
        } else if (simpleJob.getJobDeliverStatus() == 2) {
            holder.tvStatus.setText("Assigned");
        } else if (simpleJob.getJobDeliverStatus() == 3) {
            holder.tvStatus.setText("Accept");
        } else if (simpleJob.getJobDeliverStatus() == 4) {
            holder.tvStatus.setText("Go to Pickup");
        } else if (simpleJob.getJobDeliverStatus() == 5) {
            holder.tvStatus.setText("Waiting loading");
        } else if (simpleJob.getJobDeliverStatus() == 7) {
            holder.tvStatus.setText("Finish Loading");
        } else if (simpleJob.getJobDeliverStatus() == 6) {
            holder.tvStatus.setText("Start Loading");
        } else if (simpleJob.getJobDeliverStatus() == 8) {
            holder.tvStatus.setText("Deliver");
        } else if (simpleJob.getJobDeliverStatus() == 9) {
            holder.tvStatus.setText("Arrive Destination");
        } else if (simpleJob.getJobDeliverStatus() == 10) {
            holder.tvStatus.setText("Start Discharge");
        } else if (simpleJob.getJobDeliverStatus() == 11) {
            holder.tvStatus.setText("Finish Discharge");
        } else if (simpleJob.getJobDeliverStatus() == 12) {
            holder.tvStatus.setText("Go empty to depo");
        } else if (simpleJob.getJobDeliverStatus() == 13) {
            holder.tvStatus.setText("Upload empty to depo");
        } else if (simpleJob.getJobDeliverStatus() == 14) {
            holder.tvStatus.setText("Start empty depo discharge");
        } else if (simpleJob.getJobDeliverStatus() == 15) {
            holder.tvStatus.setText("Finish empty depo discharge");
        } else if (simpleJob.getJobDeliverStatus() == 16) {
            holder.tvStatus.setText("Finish job");
        } else if (simpleJob.getJobDeliverStatus() == 17) {
            holder.tvStatus.setText("Cancel pickup");
        } else if (simpleJob.getJobDeliverStatus() == 18) {
            holder.tvStatus.setText("Cancel job");
        } else if (simpleJob.getJobDeliverStatus() == 19) {
            holder.tvStatus.setText("Reject job");
        }
        if (simpleJob.getJobType() == 1) {
            holder.tvTo.setVisibility(View.INVISIBLE);
            holder.tvto2.setVisibility(View.INVISIBLE);
        } else {
            holder.tvTo.setVisibility(View.VISIBLE);
            holder.tvto2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
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