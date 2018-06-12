package com.spil.dev.tms.Activity.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.spil.dev.tms.Activity.Controller.AuthController;
import com.spil.dev.tms.Activity.Controller.KendaraanController;
import com.spil.dev.tms.Activity.Controller.SelectKendaraanController;
import com.spil.dev.tms.Activity.DashboardActivity;
import com.spil.dev.tms.Activity.Fragment.BerandaFragment;
import com.spil.dev.tms.Activity.Manager.SessionKendaraan;
import com.spil.dev.tms.Activity.Manager.SessionManager;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Model.ReturnModel;
import com.spil.dev.tms.Activity.Model.SelectKendaraanModel;
import com.spil.dev.tms.Activity.PilihKendaraanActivity;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andresual on 2/20/2018.
 */

public class PilihKendaraanAdapter extends RecyclerView.Adapter<PilihKendaraanAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private List<KendaraanModel> kendaraanModelList;
    private SessionKendaraan sessionKendaraan;
    private String idKendaraan, nopol;
    SessionManager sessionManager;
    ProgressDialog pd;

    DriverModel driverModel;
    Pref pref;
    SelectKendaraanModel selectKendaraanActive;
    ArrayList<SelectKendaraanModel> selectKendaraanModelArrayList = new ArrayList<>();

    //getter setter/////////////////////////////////////////////////////////////////////////////////

    public SelectKendaraanModel getSelectKendaraanActive() {
        return selectKendaraanActive;
    }

    public void setSelectKendaraanActive(SelectKendaraanModel selectKendaraanActive) {
        this.selectKendaraanActive = selectKendaraanActive;
    }

    public ArrayList<SelectKendaraanModel> getSelectKendaraanModelArrayList() {
        return selectKendaraanModelArrayList;
    }

    public void setSelectKendaraanModelArrayList(ArrayList<SelectKendaraanModel> selectKendaraanModelArrayList) {
        this.selectKendaraanModelArrayList = selectKendaraanModelArrayList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public PilihKendaraanAdapter(Context mContext, List<KendaraanModel> kendaraanModelList) {
        this.mContext = mContext;
        this.kendaraanModelList = kendaraanModelList;
        pd = new ProgressDialog(mContext);
        pd.setMessage("Memilih kendaraan");
        pd.setCancelable(false);
        pref = new Pref(mContext);
        driverModel = pref.getDriverModel();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kendaraan, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KendaraanModel kendaraanModel = kendaraanModelList.get(position);
        holder.tvNopol.setText(kendaraanModel.getIdNopol());
    }

    @Override
    public int getItemCount() {
        return kendaraanModelList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNopol;
        Context ctx;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);
            tvNopol = view.findViewById(R.id.tv_nopol);
            this.ctx = ctx;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View v) {
            LocationServices.getFusedLocationProviderClient(mContext).getLastLocation().addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    int position = getAdapterPosition();
                    final KendaraanModel kendaraan = kendaraanModelList.get(position);
                    pd.show();
                    new Netter(mContext).webService(Request.Method.POST, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                String msg = obj.getString("message");
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                if (status == 200) {
                                    pref.putKendaraan(kendaraan);
                                    mContext.startActivity(new Intent(mContext, DashboardActivity.class));
                                } else if (status == 300) {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, Netter.getDefaultErrorListener(mContext, new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    }), Netter.Webservice.POSTPILIHKENDARAAN, new StringHashMap().putMore("id_kendaraan",
                            kendaraan.getIdKendaraan())
                            .putMore("id_driver", driverModel.getIdDriver())
                            .putMore("lat", location.getLatitude())
                            .putMore("lng", location.getLongitude()));
                }
            });

        }
    }
}
