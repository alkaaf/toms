package com.andresual.dev.tms.Activity.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.ListDepoActivity;
import com.andresual.dev.tms.Activity.ListDermagaActivity;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.Model.PassingLocationModel;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class RejectFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    NestedScrollView nss1;
    RadioGroup rg1;
    RadioButton rbDepo;
    RadioButton rbDermaga;
    TextView tvDepo;
    TextView tvDermaga;
    RadioGroup radioGroup;
    RadioButton rb6;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    RadioButton rb5;
    EditText etAlasan;
    Button btnKirimLaporan;
    String idLokasi, tipeLokasi, email, alasan;
    SessionManager sessionManager;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    public Double lat, lng;

    public RejectFragment() {
        // Required empty public constructor
    }

    public static RejectFragment newInstance() {
        RejectFragment rejectFragment = new RejectFragment();
        return rejectFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_reject, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Report");

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        nss1 = view.findViewById(R.id.nss1);
        rg1 = view.findViewById(R.id.rg1);
        rbDepo = view.findViewById(R.id.rb_depo);
        rbDermaga = view.findViewById(R.id.rb_dermaga);
        tvDepo = view.findViewById(R.id.tv_depo);
        tvDermaga = view.findViewById(R.id.tv_dermaga);
        radioGroup = view.findViewById(R.id.radioGroup);
        rb6 = view.findViewById(R.id.rb6);
        rb1 = view.findViewById(R.id.rb1);
        rb2 = view.findViewById(R.id.rb2);
        rb3 = view.findViewById(R.id.rb3);
        rb4 = view.findViewById(R.id.rb4);
        rb5 = view.findViewById(R.id.rb5);
        etAlasan = view.findViewById(R.id.et_alasan);
        btnKirimLaporan = view.findViewById(R.id.btn_kirim_laporan);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.EMAIL_DRIVER);

        if (rb1.isActivated()) {
            etAlasan.setVisibility(View.INVISIBLE);
        } else if (rb2.isChecked()) {
            etAlasan.setVisibility(View.INVISIBLE);
        } else if (rb3.isChecked()) {
            etAlasan.setVisibility(View.INVISIBLE);
        } else if (rb4.isChecked()) {
            etAlasan.setVisibility(View.INVISIBLE);
        } else if (rb6.isChecked()) {
            etAlasan.setVisibility(View.INVISIBLE);
        } else if (rb5.isChecked()) {
            etAlasan.setVisibility(View.VISIBLE);
        }

        rbDepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDepo.setVisibility(View.VISIBLE);
                tvDermaga.setVisibility(View.INVISIBLE);
            }
        });

        rbDermaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDepo.setVisibility(View.INVISIBLE);
                tvDermaga.setVisibility(View.VISIBLE);
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAlasan.setVisibility(View.VISIBLE);
            }
        });

        tvDepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListDepoActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        tvDermaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListDermagaActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        btnKirimLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimLaporan();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                idLokasi = data.getStringExtra("iddepo");
                String namaDepo = data.getStringExtra("namadepo");

                tvDepo.setText(namaDepo);
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                idLokasi = data.getStringExtra("iddermaga");
                String namaDermaga = data.getStringExtra("namadermaga");

                tvDermaga.setText(namaDermaga);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    checkLocationAndAddToMap();
                } else Toast.makeText(getActivity(), "Locatiion Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkLocationAndAddToMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here");
        lat = location.getLatitude();
        lng = location.getLongitude();
        mMap.addMarker(markerOptions);
        Log.i("here", lat.toString());

        PassingLocationModel passingLocationModel = new PassingLocationModel();
        passingLocationModel.setLat(lat.toString());
        passingLocationModel.setLng(lng.toString());

        MapsOrderActivity mapsOrderActivity = new MapsOrderActivity();
        mapsOrderActivity.setPassingLocationModel(passingLocationModel);
        Log.i("passing", passingLocationModel.getLat());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationAndAddToMap();
    }

    public void kirimLaporan() {

        if (rb1.isChecked()) {
            alasan = rb1.getText().toString();
        } else if (rb2.isChecked()) {
            alasan = rb2.getText().toString();
        } else if (rb3.isChecked()) {
            alasan = rb3.getText().toString();
        } else if (rb4.isChecked()) {
            alasan = rb4.getText().toString();
        } else if (rb6.isChecked()) {
            alasan = rb5.getText().toString();
        } else if (rb5.isChecked()) {
            alasan = String.valueOf(etAlasan.getText());
        }

        if (rbDepo.isChecked()) {
            tipeLokasi = "1";
        } else if (rbDermaga.isChecked()) {
            tipeLokasi = "2";
        }

        final Map<String, String> params = new HashMap<>();
        params.put("f", "report_tocenter");
        params.put("email", email);
        params.put("report_note", alasan);
        params.put("latitude", lat.toString());
        params.put("longitude",lng.toString());
        params.put("idlokasi", idLokasi);
        params.put("tipelokasi", tipeLokasi);
        Log.i("rejectjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responsess", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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
}
