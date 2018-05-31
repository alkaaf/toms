package com.andresual.dev.tms.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Controller.AuthController;
import com.andresual.dev.tms.Activity.Controller.KendaraanController;
import com.andresual.dev.tms.Activity.Controller.SelectKendaraanController;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Fragment.BerandaFragment;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Model.ReturnModel;
import com.andresual.dev.tms.Activity.Model.SelectKendaraanModel;
import com.andresual.dev.tms.Activity.PilihKendaraanActivity;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            sessionKendaraan = new SessionKendaraan(mContext);
            KendaraanModel kendaraanModel = kendaraanModelList.get(position);
            idKendaraan = kendaraanModel.getIdKendaraan();
            nopol = kendaraanModel.getIdNopol();
            Log.i("ya", idKendaraan);
            sessionKendaraan.createLoginSession(idKendaraan, nopol);
            selectKendaraan();
            Intent intent = new Intent(this.ctx, DashboardActivity.class);
            this.ctx.startActivity(intent);
        }
    }

    public void selectKendaraan(){

        sessionManager = new SessionManager(mContext.getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id = user.get(SessionManager.ID_DRIVER);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "PostPilihKendaraan");
        params.put("iddriver", id);
        params.put("idkendaraan", idKendaraan);
        Log.i("fetchKendaraan", params.toString());

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray kendaraanArray = obj.getJSONArray("data");
                            selectKendaraanModelArrayList = new ArrayList<>();
                            for (int i= 0;i < kendaraanArray.length(); i++) {
                                JSONObject hasil = kendaraanArray.getJSONObject(i);
                                Log.i("haha", hasil.toString());
                                SelectKendaraanModel selectKendaraanModel = new SelectKendaraanModel();
//                                kendaraanModel.setIdDriver(hasil.getString("id_driver"));
                                selectKendaraanModel.setIdDriver(hasil.getString("iddriver"));
                                selectKendaraanModel.setIdKendaraan(hasil.getString("idkendaraan"));
                                selectKendaraanModelArrayList.add(selectKendaraanModel);
                            }
                        } catch (Throwable t){
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }
}
