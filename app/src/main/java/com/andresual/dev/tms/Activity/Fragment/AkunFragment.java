package com.andresual.dev.tms.Activity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Controller.GoogleController;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.MainActivity;
import com.andresual.dev.tms.Activity.Manager.SessionDriverInfo;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.RegisterActivity;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AkunFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    SessionDriverInfo sessionDriverInfo;
    TextView tvNamaProfil, tvNamaScroll, tvEmailScroll, tvTelpScroll, tvKotaScroll, tvAlamatScroll, tvNopol;
    String idDriver, email, idKendaraan, nopol;
    String infoEmail, infoAlamat, infoTelp, infoKota, infoNama;
    GoogleApiClient mGoogleApiClient;

    public AkunFragment() {
        // Required empty public constructor
    }

    public static AkunFragment newInstance() {
        AkunFragment akunFragment = new AkunFragment();
        return akunFragment;
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_akun, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Akun");

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sessionKendaraan = new SessionKendaraan(getActivity().getApplicationContext());
        sessionDriverInfo = new SessionDriverInfo(getActivity().getApplicationContext());

        sessionKendaraan = new SessionKendaraan(getContext());
        HashMap<String, String> data = sessionKendaraan.getKendaraanDetails();
        idKendaraan = data.get(SessionKendaraan.ID_KENDARAAN);
        nopol = data.get(SessionKendaraan.NOPOL_KENDARAAN);

        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        sessionDriverInfo = new SessionDriverInfo(getContext());
        HashMap<String, String> driverInfo = sessionDriverInfo.getUserDetails();
        infoAlamat = driverInfo.get(SessionDriverInfo.ALAMAT_DRIVER);
        infoEmail = driverInfo.get(SessionDriverInfo.EMAIL_DRIVER);
        infoKota = driverInfo.get(SessionDriverInfo.KOTA_DRIVER);
        infoTelp = driverInfo.get(SessionDriverInfo.TELP_DRIVER);
        infoNama = driverInfo.get(SessionDriverInfo.NAMA_DRIVER);

        Log.i("idKendaraan", idKendaraan, null);
        Log.i("idDriver", idDriver, null);
        Log.i("email", email, null);

        tvNamaProfil = view.findViewById(R.id.tv_name_profile);
        tvNamaScroll = view.findViewById(R.id.tv_nama);
        tvEmailScroll = view.findViewById(R.id.tv_email);
        tvTelpScroll = view.findViewById(R.id.tv_telp);
        tvKotaScroll = view.findViewById(R.id.tv_kota);
        tvAlamatScroll = view.findViewById(R.id.tv_alamat);
        tvNopol = view.findViewById(R.id.tv_nopol);

        tvNamaProfil.setText(infoNama);
        tvKotaScroll.setText(infoKota);
        tvEmailScroll.setText(infoEmail);
        tvTelpScroll.setText(infoTelp);
        tvAlamatScroll.setText(infoAlamat);
        tvNopol.setText(nopol);

        Button btnLogOut = view.findViewById(R.id.btn_logout);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Intent login = new Intent(getActivity(), MainActivity.class);
                                startActivity(login);
                                getActivity().finish();
                            }
                        });
                sessionManager.logoutUser();
                sessionKendaraan.clearKendaraan();
                sessionDriverInfo.clearDriverInfo();
                logOut();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void logOut() {

        Log.i("idKendaraan", idKendaraan, null);
        Log.i("idDriver", idDriver, null);
        Log.i("email", email, null);

        final Map<String, String> params = new HashMap<>();
        params.put("f", "getlogout");
        params.put("email", email);
        params.put("idkendaraan", idKendaraan);
        params.put("iddriver", idDriver);
        Log.i("logout",params.toString());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/byamik.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("token", obj.getString("status"));
                            Log.i("message", obj.getString("message"));
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
