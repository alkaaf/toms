package com.andresual.dev.tms.Activity.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.andresual.dev.tms.Activity.Controller.NotificationController;
import com.andresual.dev.tms.Activity.Controller.OperationalController;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Model.JobOrderModel;
import com.andresual.dev.tms.Activity.OrderBaruActivity;
import com.andresual.dev.tms.Activity.OrderBaruNotificationActivity;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by andresual on 1/23/2018.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    private static FcmMessagingService mInstance;
    public static FcmMessagingService getmInstance() {
        if (mInstance == null) {
            mInstance = new FcmMessagingService();
        }
        return mInstance;
    }

    ArrayList<JobOrderModel> jobOrderModelArrayList;
    JobOrderModel jobOrderModel;

    public static String id;
    public static String getVariable() {
        return id;
    }

    ////////////getter setter//////////////////////////////////////////////////////////////////

    public JobOrderModel getJobOrderModel() {
        return jobOrderModel;
    }

    public void setJobOrderModel(JobOrderModel jobOrderModel) {
        this.jobOrderModel = jobOrderModel;
    }

    public ArrayList<JobOrderModel> getJobOrderModelArrayList() {
        return jobOrderModelArrayList;
    }

    public void setJobOrderModelArrayList(ArrayList<JobOrderModel> jobOrderModelArrayList) {
        this.jobOrderModelArrayList = jobOrderModelArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        id = remoteMessage.getData().get("id");

        Log.d(TAG, "From: " + remoteMessage.getFrom());

//        final Map<String, String> params = new HashMap<>();
//        params.put("f", "getDetailJobOrder");
//        params.put("id", id);
//        Log.i("job", params.toString());
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("RESPONSE", response);
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            Iterator keys = obj.keys();
//                            Log.i("test", keys.toString());
//                            while(keys.hasNext()) {
//                                String oke = (String)keys.next();
//                                Log.i("oke", oke);
//
//                                JSONObject data = obj.getJSONObject(oke);
//                                Log.i("data", data.toString());
//                                jobOrderModelArrayList = new ArrayList<>();
//                                for (int i = 0; i < data.length(); i++) {
//                                    JobOrderModel jobOrderModel = new JobOrderModel();
//                                    jobOrderModel.setMessage(data.getString("message"));
//                                    jobOrderModel.setStatus(data.getInt("status"));
//                                    jobOrderModel.setId(data.getInt("id"));
//                                    jobOrderModel.setOrder_id(data.getString("order_id"));
//                                    jobOrderModel.setFleet_driver_email(data.getString("fleet_driver_email"));
//                                    jobOrderModel.setJob_type(data.getInt("job_type"));
//                                    jobOrderModel.setJob_type_name(data.getString("job_type_name"));
//                                    jobOrderModel.setJob_description(data.getString("job_description"));
//                                    jobOrderModel.setCustomer_id(data.getString("customer_id"));
//                                    jobOrderModel.setConsignee_id(data.getString("consignee_id"));
//                                    jobOrderModel.setJob_created(data.getString("job_created"));
//                                    jobOrderModel.setJob_blast(data.getString("job_blast"));
//                                    jobOrderModel.setJob_pickup_name(data.getString("job_pickup_name"));
//                                    jobOrderModel.setJob_pickup_address_id(data.getString("job_pickup_address_id"));
//                                    jobOrderModel.setJob_pickup_address(data.getString("job_pickup_address"));
//                                    jobOrderModel.setJob_pickup_latitude(data.getDouble("job_pickup_latitude"));
//                                    jobOrderModel.setJob_pickup_longitude(data.getDouble("job_pickup_longitude"));
//                                    jobOrderModel.setJob_pickup_datetime(data.getString("job_pickup_datetime"));
//                                    jobOrderModel.setJob_pickup_status(data.getString("job_pickup_status"));
//                                    jobOrderModel.setJob_pickup_reject_note(data.getString("job_pickup_reject_note"));
//                                    jobOrderModel.setJob_accept_time(data.getString("job_accept_time"));
//                                    jobOrderModel.setJob_deliver_address_id(data.getString("job_deliver_address_id"));
//                                    jobOrderModel.setJob_deliver_address(data.getString("job_deliver_address"));
//                                    jobOrderModel.setJob_deliver_latitude(data.getDouble("job_deliver_latitude"));
//                                    jobOrderModel.setJob_deliver_longitude(data.getDouble("job_deliver_longitude"));
//                                    jobOrderModel.setJob_deliver_starttime(data.getString("job_deliver_starttime"));
//                                    jobOrderModel.setJob_deliver_finishtime(data.getString("job_deliver_finishtime"));
//                                    jobOrderModel.setJob_deliver_status(data.getString("job_deliver_status"));
//                                    jobOrderModel.setJob_deliver_cancelnote(data.getString("job_deliver_cancelnote"));
//                                    jobOrderModel.setFleet_id(data.getInt("fleet_id"));
//                                    jobOrderModel.setFleet_nopol(data.getString("fleet_nopol"));
//                                    jobOrderModel.setFleet_driver_id(data.getInt("fleet_driver_id"));
//                                    jobOrderModel.setFleet_driver_name(data.getString("fleet_driver_name"));
//                                    jobOrderModel.setFleet_driver_score(data.getInt("fleet_driver_score"));
//                                    jobOrderModel.setFleet_ownership(data.getString("fleet_ownership"));
//                                    jobOrderModel.setTime_zone(data.getString("time_zone"));
//                                    jobOrderModel.setPortOrderId(data.getInt("portorderid"));
//                                    jobOrderModel.setContainer_no(data.getString("container_no"));
//                                    jobOrderModel.setContainer_sizeID(data.getInt("container_sizeid"));
//                                    jobOrderModel.setContainer_name(data.getString("container_name"));
//                                    jobOrderModel.setContainer_isfull(data.getString("container_isfull"));
//                                    jobOrderModel.setContainer_ownership(data.getString("container_ownership"));
//                                    jobOrderModel.setContainer_Cargo_ID(data.getInt("container_cargo_id"));
//                                    jobOrderModel.setContainer_Cargo_Name(data.getString("container_cargo_name"));
//                                    jobOrderModelArrayList.add(jobOrderModel);
//                                    Log.i("onResponse:", jobOrderModelArrayList.toString());
//                                }
//                            }
//                        } catch (Throwable t) {
//                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String,String> getParams(){
//                return params;
//            }
//        };
//        queue.add(sr);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Intent intent = new Intent(this, DashboardActivity.class);

//        intent.putExtra("telek", jobOrderModelArrayList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        super.onMessageReceived(remoteMessage);
    }
}
