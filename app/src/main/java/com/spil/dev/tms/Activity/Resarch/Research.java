package com.spil.dev.tms.Activity.Resarch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.spil.dev.tms.Activity.BaseActivity;
import com.spil.dev.tms.R;

import java.util.List;

public class Research extends BaseActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    SupportMapFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);

        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setup();
    }

    Marker me;
    String encodedPolyline;
    List<LatLng> polyList;

    @SuppressLint("MissingPermission")
    public void setup() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (me != null) {
                    me.remove();
                }
                me = googleMap.addMarker(new MarkerOptions().position(latLng));

                boolean res = PolyUtil.containsLocation(latLng, polyList, true);
                if(res){
                    Toast.makeText(Research.this, "Inside", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Research.this, "Outside", Toast.LENGTH_SHORT).show();
                }
            }
        });


        encodedPolyline = "nq`k@ii`oTLS~@`@ABj@?ATq@C";

        polyList = PolyUtil.decode(encodedPolyline);
        PolygonOptions polygonOptions = new PolygonOptions().addAll(polyList).fillColor(0x7700ff00);
        googleMap.addPolygon(polygonOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polyList.get(0), 15));

    }
}
