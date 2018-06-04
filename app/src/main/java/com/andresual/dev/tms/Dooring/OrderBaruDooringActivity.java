package com.andresual.dev.tms.Dooring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Adapter.SebelumnyaListAdapter;
import com.andresual.dev.tms.Activity.Controller.OperationalController;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Maps.DirectionFinder;
import com.andresual.dev.tms.Activity.Maps.DirectionFinderListener;
import com.andresual.dev.tms.Activity.MapsController;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.Activity.Model.RouteModel;
import com.andresual.dev.tms.Activity.TolakOrderActivity;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class OrderBaruDooringActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    Integer jobId;
    private GoogleMap mMap;
    TextView tvOrderNo, tvPelanggan, tvTanggal, tvEstimasiJarak, tvEstimasiWaktu, tvOrigin, tvContSize, tvContType, tvComodity, tvJobStatus;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    String idDriver, email, idKendaraan;
    String pickLat, pickLng, delivLat, delivLng;
    public static String lat;
    public static String lng;
    private static final int LOCATION_REQUEST = 500;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    String latitude, longitude;
    String jobType;
    SimpleJob modelData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_baru);
        modelData = getIntent().getParcelableExtra(SebelumnyaListAdapter.INTENT_DATA);

        sendRequest();

        sessionManager = new SessionManager(getApplicationContext());
        sessionKendaraan = new SessionKendaraan(getApplicationContext());

        sessionKendaraan = new SessionKendaraan(this);
        HashMap<String, String> data = sessionKendaraan.getKendaraanDetails();
        idKendaraan = data.get(SessionKendaraan.ID_KENDARAAN);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        tvOrderNo = findViewById(R.id.tv_order_no1);
        tvPelanggan = findViewById(R.id.tv_pelanggan1);
        tvTanggal = findViewById(R.id.tv_waktu);
        tvEstimasiJarak = findViewById(R.id.tv_estimasi_jarak1);
        tvEstimasiWaktu = findViewById(R.id.tv_estimasi_waktu1);
        tvOrigin = findViewById(R.id.tv_origin);
        tvContSize = findViewById(R.id.tv_container_size);
        tvContType = findViewById(R.id.container_type);
        tvComodity = findViewById(R.id.tv_comodity);

//        tvOrderNo.setText(this.getIntent().getStringExtra("orderNo"));
//        tvPelanggan.setText(this.getIntent().getStringExtra("pelanggan"));
//        tvOrigin.setText(this.getIntent().getStringExtra("origin"));
//        tvTanggal.setText(this.getIntent().getStringExtra("tanggal"));
//        tvContSize.setText(this.getIntent().getStringExtra("containerNo"));
//        tvContType.setText(this.getIntent().getStringExtra("containerName"));
//        tvComodity.setText(this.getIntent().getStringExtra("comodity"));
//        jobId = this.getIntent().getIntExtra("jobId", 0);

        tvOrderNo.setText(modelData.getOrderNo());
        tvPelanggan.setText(modelData.getPelanggan());
        tvOrigin.setText(modelData.getOrigin());
        tvTanggal.setText(modelData.getTanggal());
        tvContSize.setText(modelData.getContainerNo());
        tvContType.setText(modelData.getContainerName());
        tvComodity.setText(modelData.getComodity());
        jobId = modelData.getJobId();
        jobType = modelData.getJobType().toString();

        //PASSING//
//        latitude = this.getIntent().getStringExtra("latitude");
//        longitude = this.getIntent().getStringExtra("longitude");
//        final String delivLat = this.getIntent().getStringExtra("delivLat");
//        final String delivLng = this.getIntent().getStringExtra("delivLng");
//        jobType = this.getIntent().getStringExtra("jobType");
        latitude = modelData.getJobPickupLatitude();
        longitude = modelData.getJobPickupLongitude();
        final String delivLat = modelData.getJobDeliverLatitude();
        final String delivLng = modelData.getJobDeliverLongitude();
        Log.i("jobType", jobType);

        SimpleJob simpleJob = new SimpleJob();
        simpleJob.setJobId(jobId);

        OperationalController.getmInstance().setSimpleJob(simpleJob);

        Button btnTerima = findViewById(R.id.btn_terima);
        Button btnTolak = findViewById(R.id.btn_tolak);

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptJob();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderBaruDooringActivity.this, TolakOrderActivity.class);
                intent.putExtra("jobId", jobId);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void acceptJob() {

        final Map<String, String> params = new HashMap<>();
        params.put("f", "acceptjob");
        params.put("email", email);
        params.put("idjob", jobId.toString());
        Log.i("acceptjob", params.toString());

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

                            Intent intent = new Intent(OrderBaruDooringActivity.this, MapsOrderDooringActivity.class);
                            intent.putExtra("jobId", jobId);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("delivLat", delivLat);
                            intent.putExtra("delivLng", delivLng);
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

    @Override
    public void onDirectionFinderStart() {

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

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (RouteModel route : routeModels) {
            ((TextView) findViewById(R.id.tv_estimasi_waktu1)).setText(route.duration.text);
            Log.i("route", route.duration.text);
            ((TextView) findViewById(R.id.tv_estimasi_jarak1)).setText(route.distance.text);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

                    sendRequest();
                }
            });
        }
    }

    private void sendRequest() {
//        pickLat = this.getIntent().getStringExtra("latitude");
//        pickLng = this.getIntent().getStringExtra("longitude");
        pickLat = modelData.getJobPickupLatitude();
        pickLng = modelData.getJobPickupLongitude();
        String origin = lat + "," + lng;
        String destination = pickLat + "," + pickLng;
        Log.i("sendRequest: ", origin);
        Log.i("sendRequest: ", destination);
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
}
