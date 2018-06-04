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

            ////////////////////////////////////////////////////////////////////////////////////////
            //job type 1
            ////////////////////////////////////////////////////////////////////////////////////////

            if (simpleJob.getJobDeliverStatus() == 45 && simpleJob.getJobType() == 1) {
                Intent intent = new Intent(this.ctx, OrderBaruActivity.class);
                intent.putExtra("orderNo", simpleJob.getOrderNo());
                intent.putExtra("pelanggan", simpleJob.getJobName());
                intent.putExtra("tanggal", simpleJob.getTanggal());
                intent.putExtra("containerNo", simpleJob.getContainerNo());
                intent.putExtra("containerName", simpleJob.getContainerName());
                intent.putExtra("comodity", simpleJob.getComodity());
                intent.putExtra("origin", simpleJob.getOrigin());
                intent.putExtra("dest", simpleJob.getDestination());
                intent.putExtra("jobId", simpleJob.getJobId());
                intent.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent);

            } else if (simpleJob.getJobDeliverStatus() == 1 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 2 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 3 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, MapsOrderActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 4 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, SiapAntarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 5 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 6 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 7 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 8 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, MengantarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 9 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 10 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 11 && simpleJob.getJobType() == 1) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            //job type 2
            ////////////////////////////////////////////////////////////////////////////////////////

            if (simpleJob.getJobDeliverStatus() == 45 && simpleJob.getJobType() == 2) {
                Intent intent = new Intent(this.ctx, OrderBaruActivity.class);
                intent.putExtra("orderNo", simpleJob.getOrderNo());
                intent.putExtra("pelanggan", simpleJob.getJobName());
                intent.putExtra("tanggal", simpleJob.getTanggal());
                intent.putExtra("containerNo", simpleJob.getContainerNo());
                intent.putExtra("containerName", simpleJob.getContainerName());
                intent.putExtra("comodity", simpleJob.getComodity());
                intent.putExtra("origin", simpleJob.getOrigin());
                intent.putExtra("dest", simpleJob.getDestination());
                intent.putExtra("jobId", simpleJob.getJobId());
                intent.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent);

            } else if (simpleJob.getJobDeliverStatus() == 1 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 2 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 3 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, MapsOrderActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 4 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, SiapAntarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 5 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 6 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 7 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 8 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, MengantarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 9 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 10 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 11 && simpleJob.getJobType() == 2) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            //JOB TYPE 3////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////

            if (simpleJob.getJobDeliverStatus() == 45 && simpleJob.getJobType() == 3) {
                Intent intent = new Intent(this.ctx, OrderBaruActivity.class);
                intent.putExtra("orderNo", simpleJob.getOrderNo());
                intent.putExtra("pelanggan", simpleJob.getJobName());
                intent.putExtra("tanggal", simpleJob.getTanggal());
                intent.putExtra("containerNo", simpleJob.getContainerNo());
                intent.putExtra("containerName", simpleJob.getContainerName());
                intent.putExtra("comodity", simpleJob.getComodity());
                intent.putExtra("origin", simpleJob.getOrigin());
                intent.putExtra("dest", simpleJob.getDestination());
                intent.putExtra("jobId", simpleJob.getJobId());
                intent.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent);

            } else if (simpleJob.getJobDeliverStatus() == 1 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 2 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 3 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, MapsOrderActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 4 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, SiapAntarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 5 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 6 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 7 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 8 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, MengantarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 9 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 10 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 11 && simpleJob.getJobType() == 3) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            //JOB TYPE 4////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////

            if (simpleJob.getJobDeliverStatus() == 45 && simpleJob.getJobType() == 4) {
                Intent intent = new Intent(this.ctx, OrderBaruActivity.class);
                intent.putExtra("orderNo", simpleJob.getOrderNo());
                intent.putExtra("pelanggan", simpleJob.getJobName());
                intent.putExtra("tanggal", simpleJob.getTanggal());
                intent.putExtra("containerNo", simpleJob.getContainerNo());
                intent.putExtra("containerName", simpleJob.getContainerName());
                intent.putExtra("comodity", simpleJob.getComodity());
                intent.putExtra("origin", simpleJob.getOrigin());
                intent.putExtra("dest", simpleJob.getDestination());
                intent.putExtra("jobId", simpleJob.getJobId());
                intent.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent);

            } else if (simpleJob.getJobDeliverStatus() == 1 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 2 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 3 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, MapsOrderActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 4 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, SiapAntarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 5 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 6 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 7 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 8 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, MengantarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 9 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 10 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 11 && simpleJob.getJobType() == 4) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            //JOB TYPE 9////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////

            if (simpleJob.getJobDeliverStatus() == 45 && simpleJob.getJobType() == 9) {
                Intent intent = new Intent(this.ctx, OrderBaruDooringActivity.class);
                intent.putExtra("orderNo", simpleJob.getOrderNo());
                intent.putExtra("pelanggan", simpleJob.getJobName());
                intent.putExtra("tanggal", simpleJob.getTanggal());
                intent.putExtra("containerNo", simpleJob.getContainerNo());
                intent.putExtra("containerName", simpleJob.getContainerName());
                intent.putExtra("comodity", simpleJob.getComodity());
                intent.putExtra("origin", simpleJob.getOrigin());
                intent.putExtra("dest", simpleJob.getDestination());
                intent.putExtra("jobId", simpleJob.getJobId());
                intent.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent);

            } else if (simpleJob.getJobDeliverStatus() == 1 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, OrderBaruDooringActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 2 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, OrderBaruDooringActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 3 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, MapsOrderDooringActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 4 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, SiapAntarDooringActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 5 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 6 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 7 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 8 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, MengantarDooringActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 9 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 10 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 11 && simpleJob.getJobType() == 9) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            //JOB TYPE 8, DEPO CUSTOMER PORT////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////

            if (simpleJob.getJobDeliverStatus() == 45 && simpleJob.getJobType() == 8) {
                Intent intent = new Intent(this.ctx, OrderBaruActivity.class);
                intent.putExtra("orderNo", simpleJob.getOrderNo());
                intent.putExtra("pelanggan", simpleJob.getJobName());
                intent.putExtra("tanggal", simpleJob.getTanggal());
                intent.putExtra("containerNo", simpleJob.getContainerNo());
                intent.putExtra("containerName", simpleJob.getContainerName());
                intent.putExtra("comodity", simpleJob.getComodity());
                intent.putExtra("origin", simpleJob.getOrigin());
                intent.putExtra("dest", simpleJob.getDestination());
                intent.putExtra("jobId", simpleJob.getJobId());
                intent.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent);

            } else if (simpleJob.getJobDeliverStatus() == 1 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 2 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, OrderBaruActivity.class);
                intent1.putExtra("orderNo", simpleJob.getOrderNo());
                intent1.putExtra("pelanggan", simpleJob.getJobName());
                intent1.putExtra("tanggal", simpleJob.getTanggal());
                intent1.putExtra("containerNo", simpleJob.getContainerNo());
                intent1.putExtra("containerName", simpleJob.getContainerName());
                intent1.putExtra("comodity", simpleJob.getComodity());
                intent1.putExtra("origin", simpleJob.getOrigin());
                intent1.putExtra("dest", simpleJob.getDestination());
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("jobStatus", simpleJob.getJobDeliverStatus());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                intent1.putExtra("jobType", simpleJob.getJobType());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 3 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, MapsOrderActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 4 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, SiapAntarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 5 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, ReadyToStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 6 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, StartStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 7 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, FinishStuffPickupActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 8 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, MengantarActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 9 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, ArriveDestinationActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 10 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, StartDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 11 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, FinishDischargeActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);

            } else if (simpleJob.getJobDeliverStatus() == 12 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, GoEmptyToDepoActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
            } else if (simpleJob.getJobDeliverStatus() == 13 && simpleJob.getJobType() == 8) {
                Intent intent1 = new Intent(this.ctx, UploadEmptyToDepoActivity.class);
                intent1.putExtra("jobId", simpleJob.getJobId());
                intent1.putExtra("latitude", simpleJob.getJobPickupLatitude());
                intent1.putExtra("longitude", simpleJob.getJobPickupLongitude());
                intent1.putExtra("delivLat", simpleJob.getJobDeliverLatitude());
                intent1.putExtra("delivLng", simpleJob.getJobDeliverLongitude());
                this.ctx.startActivity(intent1);
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