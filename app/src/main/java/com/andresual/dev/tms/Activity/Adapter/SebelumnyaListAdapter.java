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
import com.andresual.dev.tms.Activity.MenurunkanActivity;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.OrderBaruActivity;
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
    private ArrayList<JobOrder2Model> jobOrder2ModelList;
    private List<JobOrder2Model> jobOrder2ModelListFiltered;
    private JobDetail2AdapterListener listener;

    public SebelumnyaListAdapter(Context mContext, ArrayList<JobOrder2Model> jobOrder2ModelList) {
        this.mContext = mContext;
        this.jobOrder2ModelList = jobOrder2ModelList;
        this.jobOrder2ModelListFiltered = jobOrder2ModelListFiltered;
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
//            JobOrder2Model jobOrder2Model = jobOrder2ModelList.get(getAdapterPosition());

            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
//            JobOrder2Model jobOrder2Model = OperationalController.getmInstance().getJobOrder2ModelArrayList().get(position);
            JobOrder2Model jobOrder2Model = jobOrder2ModelList.get(position);
            if (jobOrder2Model.getJobDeliverStatus() == 45 && jobOrder2Model.getJobType() != 4) {
                Intent intent = new Intent(this.ctx, OrderBaruActivity.class);
                intent.putExtra(INTENT_DATA, jobOrder2Model);
//                intent.putExtra("orderNo", jobOrder2Model.getOrderNo());
//                intent.putExtra("pelanggan", jobOrder2Model.getJobName());
//                intent.putExtra("tanggal", jobOrder2Model.getTanggal());
//                intent.putExtra("containerNo", jobOrder2Model.getContainerNo());
//                intent.putExtra("containerName", jobOrder2Model.getContainerName());
//                intent.putExtra("comodity", jobOrder2Model.getComodity());
//                intent.putExtra("origin", jobOrder2Model.getOrigin());
//                intent.putExtra("dest", jobOrder2Model.getDestination());
//                intent.putExtra("jobId", jobOrder2Model.getJobId());
//                intent.putExtra("jobStatus", jobOrder2Model.getJobDeliverStatus());
//                intent.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
//                intent.putExtra("jobType", jobOrder2Model.getJobType());
                this.ctx.startActivity(intent);

            } else if (jobOrder2Model.getJobDeliverStatus() == 1 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
            intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("orderNo", jobOrder2Model.getOrderNo());
//                intent1.putExtra("pelanggan", jobOrder2Model.getJobName());
//                intent1.putExtra("tanggal", jobOrder2Model.getTanggal());
//                intent1.putExtra("containerNo", jobOrder2Model.getContainerNo());
//                intent1.putExtra("containerName", jobOrder2Model.getContainerName());
//                intent1.putExtra("comodity", jobOrder2Model.getComodity());
//                intent1.putExtra("origin", jobOrder2Model.getOrigin());
//                intent1.putExtra("dest", jobOrder2Model.getDestination());
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("jobStatus", jobOrder2Model.getJobDeliverStatus());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
//                intent1.putExtra("jobType", jobOrder2Model.getJobType());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 2 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("orderNo", jobOrder2Model.getOrderNo());
//                intent1.putExtra("pelanggan", jobOrder2Model.getJobName());
//                intent1.putExtra("tanggal", jobOrder2Model.getTanggal());
//                intent1.putExtra("containerNo", jobOrder2Model.getContainerNo());
//                intent1.putExtra("containerName", jobOrder2Model.getContainerName());
//                intent1.putExtra("comodity", jobOrder2Model.getComodity());
//                intent1.putExtra("origin", jobOrder2Model.getOrigin());
//                intent1.putExtra("dest", jobOrder2Model.getDestination());
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("jobStatus", jobOrder2Model.getJobDeliverStatus());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
//                intent1.putExtra("jobType", jobOrder2Model.getJobType());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 3 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, MapsOrderActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 4 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, SiapAntarActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 5 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 6 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 7 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 8 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, MengantarActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 9 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 10 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 11 && jobOrder2Model.getJobType() != 4) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            }

            if (jobOrder2Model.getJobDeliverStatus() == 9 && jobOrder2Model.getJobType() == 4) {
                Intent intent = new Intent(this.ctx, OrderBaruDooringActivity.class);
                intent.putExtra(INTENT_DATA, jobOrder2Model);
//                intent.putExtra("orderNo", jobOrder2Model.getOrderNo());
//                intent.putExtra("pelanggan", jobOrder2Model.getJobName());
//                intent.putExtra("tanggal", jobOrder2Model.getTanggal());
//                intent.putExtra("containerNo", jobOrder2Model.getContainerNo());
//                intent.putExtra("containerName", jobOrder2Model.getContainerName());
//                intent.putExtra("comodity", jobOrder2Model.getComodity());
//                intent.putExtra("origin", jobOrder2Model.getOrigin());
//                intent.putExtra("dest", jobOrder2Model.getDestination());
//                intent.putExtra("jobId", jobOrder2Model.getJobId());
//                intent.putExtra("jobStatus", jobOrder2Model.getJobDeliverStatus());
//                intent.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent);

            } else if (jobOrder2Model.getJobDeliverStatus() == 1 && jobOrder2Model.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, OrderBaruDooringActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 2 && jobOrder2Model.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, MapsOrderDooringActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 3 && jobOrder2Model.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, SiapAntarDooringActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 4 && jobOrder2Model.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, MengantarDooringActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 5 && jobOrder2Model.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, MenurunkanDooringActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
//                intent1.putExtra("delivLat", jobOrder2Model.getJobDeliverLatitude());
//                intent1.putExtra("delivLng", jobOrder2Model.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (jobOrder2Model.getJobDeliverStatus() == 7 && jobOrder2Model.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, JobFinishedActivity.class);
                intent1.putExtra(INTENT_DATA, jobOrder2Model);
//                intent1.putExtra("jobId", jobOrder2Model.getJobId());
//                intent1.putExtra("latitude", jobOrder2Model.getJobPickupLatitude());
//                intent1.putExtra("longitude", jobOrder2Model.getJobPickupLongitude());
                this.ctx.startActivity(intent1);
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
//        JobOrder2Model jobOrder2Model = OperationalController.getmInstance().getJobOrder2ModelArrayList().get(position);
//        JobOrder2Model jobOrder2Model = BerandaFragment.getmInstance().getJobOrder2ModelArrayList().get(position);
//        holder.tvJobName.setText(jobOrder2Model.getJobName());
//        holder.tvFrom.setText(jobOrder2Model.getOrigin());
//        holder.tvTo.setText(jobOrder2Model.getDestination());
        holder.tvJobName.setText(jobOrder2ModelList.get(position).getJobName());
        holder.tvFrom.setText(jobOrder2ModelList.get(position).getOrigin());
        holder.tvTo.setText(jobOrder2ModelList.get(position).getDestination());
        JobOrder2Model jobOrder2Model = jobOrder2ModelList.get(position);
        if (jobOrder2Model.getJobDeliverStatus() == 1) {
            holder.tvStatus.setText("Waiting");
        } else if (jobOrder2Model.getJobDeliverStatus() == 2) {
            holder.tvStatus.setText("Assigned");
        } else if (jobOrder2Model.getJobDeliverStatus() == 3) {
            holder.tvStatus.setText("Accept");
        } else if (jobOrder2Model.getJobDeliverStatus() == 4) {
            holder.tvStatus.setText("Go to Pickup");
        } else if (jobOrder2Model.getJobDeliverStatus() == 5) {
            holder.tvStatus.setText("Waiting loading");
        } else if (jobOrder2Model.getJobDeliverStatus() == 7) {
            holder.tvStatus.setText("Start loading");
        } else if (jobOrder2Model.getJobDeliverStatus() == 6) {
            holder.tvStatus.setText("Finish loading");
        } else if (jobOrder2Model.getJobDeliverStatus() == 8) {
            holder.tvStatus.setText("Deliver");
        } else if (jobOrder2Model.getJobDeliverStatus() == 9) {
            holder.tvStatus.setText("Arrive Destination");
        } else if (jobOrder2Model.getJobDeliverStatus() == 10) {
            holder.tvStatus.setText("Start Discharge");
        } else if (jobOrder2Model.getJobDeliverStatus() == 11) {
            holder.tvStatus.setText("Finish Discharge");
        } else if (jobOrder2Model.getJobDeliverStatus() == 12) {
            holder.tvStatus.setText("Go empty to depo");
        } else if (jobOrder2Model.getJobDeliverStatus() == 13) {
            holder.tvStatus.setText("Upload empty to depo");
        } else if (jobOrder2Model.getJobDeliverStatus() == 14) {
            holder.tvStatus.setText("Start empty depo discharge");
        } else if (jobOrder2Model.getJobDeliverStatus() == 15) {
            holder.tvStatus.setText("Finish empty depo discharge");
        } else if (jobOrder2Model.getJobDeliverStatus() == 16) {
            holder.tvStatus.setText("Finish job");
        } else if (jobOrder2Model.getJobDeliverStatus() == 17) {
            holder.tvStatus.setText("Cancel pickup");
        } else if (jobOrder2Model.getJobDeliverStatus() == 18) {
            holder.tvStatus.setText("Cancel job");
        } else if (jobOrder2Model.getJobDeliverStatus() == 19) {
            holder.tvStatus.setText("Reject job");
        }
//        holder.tvStatus.setText(jobOrder2ModelList.get(position).getJobDeliverStatus());
    }

    @Override
    public int getItemCount() {
//        return OperationalController.getmInstance().getJobOrder2ModelArrayList().size();
        return jobOrder2ModelList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    jobOrder2ModelListFiltered = jobOrder2ModelList;
                } else {
                    List<JobOrder2Model> filteredList = new ArrayList<>();
                    for (JobOrder2Model row : jobOrder2ModelList) {
                        if (row.getJobName().toLowerCase().contains(charString.toLowerCase()) || row.getJobName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    jobOrder2ModelListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = jobOrder2ModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                jobOrder2ModelListFiltered = (ArrayList<JobOrder2Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface JobDetail2AdapterListener {
        void onHsCodeSelected(JobOrder2Model fetchDashboardStuffingModel);
    }
}
