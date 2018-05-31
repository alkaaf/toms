package com.andresual.dev.tms.Dooring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Maps.DirectionFinder;
import com.andresual.dev.tms.Activity.Maps.DirectionFinderListener;
import com.andresual.dev.tms.Activity.MapsController;
import com.andresual.dev.tms.Activity.MenurunkanActivity;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.Model.PassingLocationModel;
import com.andresual.dev.tms.Activity.Model.RouteModel;
import com.andresual.dev.tms.Activity.TolakOrderActivity;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MengantarDooringActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    Integer jobId;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    String idDriver, email, idKendaraan;
    String pickLat, pickLong, delivLat, delivLong = null;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    PassingLocationModel passingLocationModel;
    public static String lat;
    public static String lng;
    private Button btnFindPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mengantar);

        fetchJobOrder();

        jobId = this.getIntent().getIntExtra("jobId", 0);
        delivLat = this.getIntent().getStringExtra("delivLat");
        delivLong = this.getIntent().getStringExtra("delivLng");
        Log.i("onCreate: ", delivLat);

        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("Cari lokasi pengantaran ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendRequest();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MengantarDooringActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).create();

        dialog.show();

        sessionManager = new SessionManager(getApplicationContext());
        sessionKendaraan = new SessionKendaraan(getApplicationContext());

        sessionKendaraan = new SessionKendaraan(this);
        HashMap<String, String> data = sessionKendaraan.getKendaraanDetails();
        idKendaraan = data.get(SessionKendaraan.ID_KENDARAAN);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        jobId = this.getIntent().getIntExtra("jobId", 0);
        final String delivLat = this.getIntent().getStringExtra("delivLat");
        final String delivLong = this.getIntent().getStringExtra("delivLng");

        Button btnTolak = findViewById(R.id.btn_tolak);
        Button btnMengantar = (Button) findViewById(R.id.btn_mengantar);

        btnMengantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LocationModel locationModel = new LocationModel();
//                locationModel.setLatitude(lat);
//                locationModel.setLongitude(lng);
//                OperationalController.getmInstance().setLocationActive(locationModel);
//                OperationalController.getmInstance().mengantarJob(MengantarActivity.this);
                mengantarJob();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MengantarDooringActivity.this, TolakOrderActivity.class);
                intent.putExtra("jobId", jobId);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        sendRequest();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
                }
            });
//            sendRequest();
        }
    }

    private void sendRequest() {

//        if (delivLat.equals("")) {
//
//            Dialog dialog = new AlertDialog.Builder(this)
//                    .setMessage("Lokasi pengantaran tidak ditemukan, silahkan hubungi koordinator anda")
//                    .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Intent intent = new Intent(MengantarActivity.this, DashboardActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                    }).create();
//
//            dialog.show();

//        } else {
            jobId = this.getIntent().getIntExtra("jobId", 0);
            delivLat = this.getIntent().getStringExtra("delivLat");
            delivLong = this.getIntent().getStringExtra("delivLng");
            String origin = lat + "," + lng;
            String destination = this.getIntent().getStringExtra("delivLat") + "," + this.getIntent().getStringExtra("delivLng");
//            String destination = delivLat + "," + delivLong;
            try {
                new DirectionFinder(this, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//        }
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //get response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction...", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<RouteModel> routeModels) {

        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (RouteModel route : routeModels) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.been_here_marker_red))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(route.startLocation);
            builder.include(route.endLocation);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            mMap.animateCamera(cu);
        }

    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                MapsController mapsController = new MapsController();
                routes = mapsController.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mengantarJob() {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "deliverjob");
        params.put("email", email);
        params.put("idjob", jobId.toString());
        params.put("latitude", lat.toString());
        params.put("longitude", lng.toString());
        Log.i("mengantarjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));

                            Intent intent = new Intent(MengantarDooringActivity.this, MenurunkanDooringActivity.class);
                            intent.putExtra("jobId", jobId);
                            intent.putExtra("latitude", pickLat);
                            intent.putExtra("longitude", pickLong);
                            intent.putExtra("delivLat", delivLat);
                            intent.putExtra("delivLng", delivLong);
                            startActivity(intent);
                            finish();

                        } catch (Throwable t) {
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

    public void fetchJobOrder() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.TOKEN_KEY);
        String idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "getDetailJobOrder2");
        params.put("id_driver", idDriver);
        Log.i("beranda", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray job = obj.getJSONArray("job");
//                            jobOrder2ModelArrayList = new ArrayList<>();
                            for (int i = 0; i < job.length(); i++) {
                                JSONObject hasil = job.getJSONObject(i);
                                Log.i("haha", hasil.toString());
                                JobOrder2Model jobOrder2Model = new JobOrder2Model();
                                jobOrder2Model.setOrderNo(hasil.getString("order_id"));
                                jobOrder2Model.setTanggal(hasil.getString("job_blast"));
                                jobOrder2Model.setContainerNo(hasil.getString("container_no"));
                                jobOrder2Model.setContainerName(hasil.getString("container_name"));
                                jobOrder2Model.setComodity(hasil.getString("container_cargo_name"));
                                jobOrder2Model.setJobName(hasil.getString("job_pickup_name"));
                                jobOrder2Model.setOrigin(hasil.getString("job_pickup_address"));
                                jobOrder2Model.setDestination(hasil.getString("job_deliver_address"));
                                jobOrder2Model.setJobId(hasil.getInt("id"));
                                jobOrder2Model.setJobDeliverStatus(hasil.getInt("job_deliver_status"));
                                jobOrder2Model.setJobPickupLatitude(hasil.getString("job_pickup_latitude"));
                                jobOrder2Model.setJobPickupLongitude(hasil.getString("job_pickup_longitude"));
                                jobOrder2Model.setJobDeliverLatitude(hasil.getString("job_deliver_latitude"));
                                delivLat = hasil.getString("job_deliver_latitude");
                                jobOrder2Model.setJobDeliverLongitude(hasil.getString("job_deliver_longitude"));
                                delivLong = hasil.getString(hasil.getString("job_deliver_longitude"));
                                Log.i("onResponses:", jobOrder2Model.getJobDeliverLongitude());
//                                jobOrder2ModelArrayList.add(jobOrder2Model);
//                                Log.i("array", jobOrder2ModelArrayList.toString());

//                                mAdapter = new BerandaListAdapter(getActivity(), jobOrder2ModelArrayList);
//                                rvBeranda.setAdapter(mAdapter);
//                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (Throwable t) {
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }

//                        mAdapter.notifyDataSetChanged();
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