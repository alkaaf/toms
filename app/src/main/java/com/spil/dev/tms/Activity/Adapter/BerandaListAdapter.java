package com.spil.dev.tms.Activity.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.spil.dev.tms.Activity.ArriveDestinationActivity;
import com.spil.dev.tms.Activity.FinishDischargeActivity;
import com.spil.dev.tms.Activity.FinishStuffPickupActivity;
import com.spil.dev.tms.Activity.GoEmptyToDepoActivity;
import com.spil.dev.tms.Activity.MapsOrderActivity;
import com.spil.dev.tms.Activity.MengantarActivity;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.OrderBaruActivity;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProses1And2;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProses3;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProsesFrom4To7;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProsesMoreThan8;
import com.spil.dev.tms.Activity.ReadyToStuffPickupActivity;
import com.spil.dev.tms.Activity.SiapAntarActivity;
import com.spil.dev.tms.Activity.StartDischargeActivity;
import com.spil.dev.tms.Activity.StartStuffPickupActivity;
import com.spil.dev.tms.Activity.UploadEmptyToDepoActivity;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.Dooring.MapsOrderDooringActivity;
import com.spil.dev.tms.Dooring.MengantarDooringActivity;
import com.spil.dev.tms.Dooring.OrderBaruDooringActivity;
import com.spil.dev.tms.Dooring.SiapAntarDooringActivity;
import com.spil.dev.tms.R;

import org.json.JSONException;
import org.json.JSONObject;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView tvJobName, tvFrom, tvTo, tvStatus, tvto2;
        public RelativeLayout rl1;
        Context ctx;
        ImageView iv;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);
             view.setOnLongClickListener(this);
            tvJobName = (TextView) view.findViewById(R.id.tv_nama);
            tvFrom = (TextView) view.findViewById(R.id.tv_from1);
            tvTo = (TextView) view.findViewById(R.id.tv_to1);
            iv = view.findViewById(R.id.iv_img);
            tvto2 = view.findViewById(R.id.tv_to);
            tvStatus = view.findViewById(R.id.tv_status);
            rl1 = view.findViewById(R.id.rl1);

            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            SimpleJob simpleJob = simpleJobList.get(position);
            if (simpleJob.getJobDeliverStatus() < 14) {
                if (simpleJob.getJobType() <= 2) {
                    ActivityProses1And2.startProses(mContext, simpleJob);
                } else if (simpleJob.getJobType() <= 3) {
                    ActivityProses3.startProses(mContext, simpleJob);
                } else if (simpleJob.getJobType() <= 7) {
                    ActivityProsesFrom4To7.startProses(mContext, simpleJob);
                } else {
                    ActivityProsesMoreThan8.startProses(mContext, simpleJob);
                }
            } else {
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
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(mContext).setMessage("Reset job to Accept?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LocationServices.getFusedLocationProviderClient(mContext).getLastLocation().addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    new Netter(mContext).webService(Request.Method.POST, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject obj = new JSONObject(response);
//                                                        toast(obj.getString("message"));
                                                        if (obj.getInt("status") == 200) {
                                                            notifyDataSetChanged();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, Netter.getDefaultErrorListener(mContext, new Runnable() {
                                                @Override
                                                public void run() {
                                                }
                                            }), Netter.Webservice.JOB_ACCEPT, new StringHashMap()
                                                    .putMore("idjob", Integer.toString(simpleJobList.get(getAdapterPosition()).getJobId()))
                                                    .putMore("email", new Pref(mContext).getDriverModel().getEmail())
                                                    .putMore("lat", Double.toString(location.getLatitude()))
                                                    .putMore("lng", Double.toString(location.getLongitude()))
                                    );
                                }
                            });
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
            return false;
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
        holder.tvStatus.setText(simpleJob.getStringDeliverStatus());
        if ((simpleJob.getJobType() == 1 || simpleJob.getJobType() == 2) && simpleJob.getJobDeliverStatus() < 8 ) {
            holder.tvTo.setVisibility(View.INVISIBLE);
            holder.tvto2.setVisibility(View.INVISIBLE);
        } else {
            holder.tvTo.setVisibility(View.VISIBLE);
            holder.tvto2.setVisibility(View.VISIBLE);
        }
        holder.tvStatus.setBackgroundColor(simpleJob.getStatusColor());
        holder.iv.setColorFilter(simpleJob.getStatusColor());
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