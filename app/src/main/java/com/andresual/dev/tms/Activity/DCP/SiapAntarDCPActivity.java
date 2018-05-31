package com.andresual.dev.tms.Activity.DCP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.MainActivity;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Maps.DirectionFinder;
import com.andresual.dev.tms.Activity.Maps.DirectionFinderListener;
import com.andresual.dev.tms.Activity.MapsController;
import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.Model.PassingLocationModel;
import com.andresual.dev.tms.Activity.Model.RouteModel;
import com.andresual.dev.tms.Activity.ReadyToStuffPickupActivity;
import com.andresual.dev.tms.Activity.TolakOrderActivity;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

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

public class SiapAntarDCPActivity extends FragmentActivity implements
        OnMapReadyCallback,
        DirectionFinderListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Integer jobId;
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    String idDriver, email, idKendaraan;
    String pickLat, pickLong;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    Button btnTolak, btnSiapAntar;
    public Double lat;
    public Double lng;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private PendingIntent pendingIntent;
    private boolean isMonitoring = false;
    protected ArrayList<Geofence> mGeofenceList;
    private static final String TAG = "SiapAntar";
    private GeofencingRequest geofencingRequest;
    private MarkerOptions markerOptions;
    public static final String GEOFENCE_ASAL = "geofenceAsal";
    public static final float GEOFENCE_RADIUS_IN_METERS = 350;
    public static final HashMap<String, LatLng> AREA_ASAL = new HashMap<String, LatLng>();
    LatLng latLng;
    List<LatLng> decodedPath;
    Location location;
    String delivLat, delivLng;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siap_antar);

        mGeofenceList = new ArrayList<Geofence>();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

//        fetchPolyLine();

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
        pickLat = this.getIntent().getStringExtra("latitude");
        pickLong = this.getIntent().getStringExtra("longitude");
        delivLat = this.getIntent().getStringExtra("delivLat");
        delivLng = this.getIntent().getStringExtra("delivLng");

        btnTolak = findViewById(R.id.btn_tolak);
        btnSiapAntar = findViewById(R.id.btn_siap_antar);

        btnSiapAntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delivLat == null) {
                    Intent intent = new Intent(SiapAntarDCPActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    siapAntarJob();
                }
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SiapAntarDCPActivity.this, TolakOrderActivity.class);
                intent.putExtra("jobId", jobId);
                intent.putExtra("email", email);
                Log.i("emaill", email);
                startActivity(intent);
            }
        });

        listPoints = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!googleApiClient.isConnecting() || !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopGeoFencing();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "Google Api Client Connected");
        isMonitoring = true;
//        checkLocationAndAddToMap();
//        sendRequest();
//        startGeofencing();
//        startLocationMonitor();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("oke", "Google Api Client failed" + connectionResult.getErrorMessage());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationAndAddToMap();
        sendRequest();
        fetchPolyLine();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        LatLng latLng = AREA_ASAL.get(GEOFENCE_ASAL);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("AREA PENJEMPUTAN"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(GEOFENCE_RADIUS_IN_METERS)
                .strokeColor(Color.RED)
                .strokeWidth(4f));

        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(16)
                            .bearing(90)
                            .tilt(40)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    if (location == null) {
                        lng = (location.getLongitude());
                        lat = (location.getLatitude());
                    }
                }
            });
        }
    }

    private void checkLocationAndAddToMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        lat = location.getLatitude();
        lng = location.getLongitude();
        Log.i("here", lat.toString());

        PassingLocationModel passingLocationModel = new PassingLocationModel();
        passingLocationModel.setLat(lat.toString());
        passingLocationModel.setLng(lng.toString());

        MapsOrderActivity mapsOrderActivity = new MapsOrderActivity();
        mapsOrderActivity.setPassingLocationModel(passingLocationModel);
        Log.i("passing", passingLocationModel.getLat());
    }

    private void sendRequest() {

        jobId = this.getIntent().getIntExtra("jobId", 0);
        pickLat = this.getIntent().getStringExtra("latitude");
        pickLong = this.getIntent().getStringExtra("longitude");
        String origin = lat + "," + lng;
        String destination = pickLat + "," + pickLong;

        //convert to double
        double pickLatd = Double.parseDouble(pickLat);
        double pickLngd = Double.parseDouble(pickLong);

        //input area penjemputan ke hashmap
        AREA_ASAL.put(GEOFENCE_ASAL, new LatLng(pickLatd, pickLngd));
//        getGeofence();

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
            for (Polyline polyline : polylinePaths) {
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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i( "pitrik", latLng.toString());

            isPointInPolygon(latLng, decodedPath);
            boolean coba = isPointInPolygon(latLng, decodedPath);
            if (!coba) {
                btnSiapAntar.setVisibility(View.GONE);
            } else if (coba){
                btnSiapAntar.setVisibility(View.VISIBLE);
            }
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

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

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
                polylineOptions.width(17);
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

    public void siapAntarJob() {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "readyjob");
        params.put("email", email);
        params.put("idjob", jobId.toString());
        Log.i("siapantar", params.toString());

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

                            Intent intent = new Intent(SiapAntarDCPActivity.this, ReadyToStuffPickupActivity.class);
                            intent.putExtra("jobId", jobId);
                            intent.putExtra("latitude", pickLat);
                            intent.putExtra("longitude", pickLong);
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
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(sr);
    }

    public void fetchPolyLine() {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "ShowGeofencelokasi");
        params.put("idjob", jobId.toString());
        Log.i("geofence", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject obj1 = obj.getJSONObject("depo");
                            JSONArray asal = obj1.getJSONArray("asal");
                            Log.i("onResponse: ", asal.toString());
                            for (int i = 0; i < asal.length(); i++) {
                                JSONObject hasil = asal.getJSONObject(i);
                                String polyLine = hasil.getString("path");

                                decodedPath = PolyUtil.decode(polyLine);
                                PolygonOptions polygonOptions = new PolygonOptions()
                                        .addAll(decodedPath)
                                        .strokeColor(Color.BLUE);

                                mMap.addPolygon(polygonOptions);
                            }
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
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(sr);
    }

    private boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }
        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }
}
