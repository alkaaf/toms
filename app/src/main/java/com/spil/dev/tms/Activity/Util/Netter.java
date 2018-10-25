package com.spil.dev.tms.Activity.Util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spil.dev.tms.App;

import java.util.Map;

public class Netter {
//    public static final String WS_ADDR = "http://manajemenkendaraan.com/tms2/";
//    public static final String WS_ADDR = "http://35.240.211.87/toms/api/mobile/";
//    public static final String WS_ADDR = "http://toms.app-spil.com/";
    //    public static final String WS_ADDR = "http://dmas.app-spil.com/toms/api/mobile/";
    public static final String WS_ADDR = "http://toms.app-spil.com/api/mobile/";
//    public static final String WS_ADDR = "http://www.software-ekspedisi.com/toms/api/mobile/";
//    public static final String BYAMIK = "byamik.asp";
    public static final String BYAMIK = "";
//    public static final String WS = "webservice.asp";
    public static final String WS = "";
    Context context;

    public enum Byamik {

        LOGINAPP("loginapp"),
        GETLOGOUT("getlogout");
        String func;

        Byamik(String s) {
            this.func = s;
        }
    }

    public enum Webservice {
        SEND_TIME_ESTIMATE("estimasi_job"),

        JOB_ACCEPT("acceptjob"),
        JOB_PICKUP("pickupjob"),
        JOB_READYJOB("readyjob"),
        JOB_STARTJOBDEPARTURE("startjobdeparture"),
        JOB_FINISHJOBDEPARTURE("finishjobdeparture"),
        JOB_DELIVERJOB("deliverjob"),
        JOB_ARRIVEDESTINATION("arrivedestination"),
        JOB_STARTJOBARRIVAL("startjobarrival"),
        JOB_FINISHJOBARRIVAL("finishjobarrival"),
        JOB_FINISHJOB("finishjob"),
        JOB_GOEMPTYDEPO("goemptytoDepo"),
        JOB_UPLOADEMPTYDEPO("uploademptytodepo"),

        GETDATAUSER("GetDataUser"),
        UPDATEPASSWORD("SaveUpdatePassword"),
        REPORTLIST("ShowListInfoDriver"),
        REJECT("rejectjob"),
        DETAILPICKUP("getDetailPengangkutan"),
        REPORT("report_tocenter"),
        GETDEPO("GetListDepo"),
        GETDERMAGA("GetListDermaga"),
        UPDATELOKASIDRIVER("UpdateLokasiDriver"),
        UPDATELOKASIDRIVERWITHDISTANCE("UpdateLokasiDriverWithDistance"),
        GETLISTKENDARAANDRIVER("getListKendaraanDriver"),
        POSTPILIHKENDARAAN("PostPilihKendaraan"),
        GETDASHBOARDDRIVER("GetDashboardDriver"),
        GETJOBHARIINI("getDetailJobOrderHariIni"),
        GETJOBPAST("getDetailJobOrderPast");


        String func;

        Webservice(String s) {
            this.func = s;
        }

    }

    public Netter(Context context) {
        this.context = context;
    }

    public void byAmik(int method, Response.Listener<String> listener, Response.ErrorListener errorListener, Byamik func, final StringHashMap param) {
        if (context != null) {
            RequestQueue queue = App.getRequestQueueInstance();
            param.putMore("f", func.func);

            queue.add(new StringRequest(method, WS_ADDR + BYAMIK, listener, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return param;
                }
            });
        }
    }

    public void webService(int method, Response.Listener<String> listener, Response.ErrorListener errorListener, Webservice func, final StringHashMap param) {
        if (context != null) {
            RequestQueue queue = App.getRequestQueueInstance();
            param.putMore("f", func.func);
            queue.add(new StringRequest(method, WS_ADDR + WS, listener, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return param;
                }
            });
        }
    }

    public static Response.ErrorListener getDefaultErrorListener(final Context context, final Runnable after) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (!TextUtils.isEmpty(error.getLocalizedMessage())) {
                Toast.makeText(context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
//                }
                if (after != null) after.run();
            }
        };
    }

    public static Response.ErrorListener getSilentErrorListener(final Context context, final Runnable after) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (after != null) after.run();
            }
        };
    }
}
