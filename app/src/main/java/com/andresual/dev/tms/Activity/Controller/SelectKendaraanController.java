package com.andresual.dev.tms.Activity.Controller;

import android.util.Log;

import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Model.SelectKendaraanModel;
import com.andresual.dev.tms.Activity.PilihKendaraanActivity;
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
import java.util.Map;

/**
 * Created by andresual on 2/20/2018.
 */

public class SelectKendaraanController {

    private static SelectKendaraanController mInstance;
    public static SelectKendaraanController getmInstance() {
        if (mInstance == null) {
            mInstance = new SelectKendaraanController();
        }
        return mInstance;
    }

    SelectKendaraanModel selectKendaraanActive;
    ArrayList<SelectKendaraanModel> selectKendaraanModelArrayList = new ArrayList<>();

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

//    public void selectKendaraan(){
//
//        final HashMap<String, String> params = new HashMap<>();
//        params.put("f", "PostPilihKendaraan");
//        params.put("iddriver", AuthController.getmInstance().getDriverActive().getIdDriver());
//        params.put("idkendaraan", KendaraanController.getmInstance().getKendaraanActive().getInKendaraan());
//        Log.i("fetchKendaraan", params.toString());
//
//        RequestQueue queue = Volley.newRequestQueue();
//        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("RESPONSE", response);
//                        try {
//                            JSONObject obj = new JSONObject(response);
//
//                            JSONArray kendaraanArray = obj.getJSONArray("data");
//                            selectKendaraanModelArrayList = new ArrayList<>();
//                            for (int i= 0;i < kendaraanArray.length(); i++) {
//                                JSONObject hasil = kendaraanArray.getJSONObject(i);
//                                Log.i("haha", hasil.toString());
//                                SelectKendaraanModel selectKendaraanModel = new SelectKendaraanModel();
////                                kendaraanModel.setIdDriver(hasil.getString("id_driver"));
//                                selectKendaraanModel.setIdDriver(hasil.getString("iddriver"));
//                                selectKendaraanModel.setIdKendaraan(hasil.getString("idkendaraan"));
//                                selectKendaraanModelArrayList.add(selectKendaraanModel);
//                            }
//                        } catch (Throwable t){
//                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String,String> getParams(){
//                return params;
//            }
//        };
//        queue.add(sr);
//    }
}
