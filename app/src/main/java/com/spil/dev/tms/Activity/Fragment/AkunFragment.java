package com.spil.dev.tms.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.spil.dev.tms.Activity.DashboardActivity;
import com.spil.dev.tms.Activity.MainActivity;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AkunFragment extends Fragment {
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    TextView tvNamaProfil, tvEmailScroll, tvTelpScroll, tvKotaScroll, tvAlamatScroll, tvNopol;
    ImageView imgProfile;
    Pref pref;
    DriverModel driver;
    KendaraanModel kendaraan;
    ProgressDialog pd;
    public AkunFragment() {
        // Required empty public constructor
    }

    public static AkunFragment newInstance() {
        AkunFragment akunFragment = new AkunFragment();
        return akunFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_akun, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Akun");
        ButterKnife.bind(this, view);
        pref = new Pref(getContext());
        driver = pref.getDriverModel();
        kendaraan = pref.getKendaraan();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Logging you out");
        imgProfile = view.findViewById(R.id.iv_profile_picture);
        tvNamaProfil = view.findViewById(R.id.tv_name_profile);
        tvEmailScroll = view.findViewById(R.id.tv_email);
        tvTelpScroll = view.findViewById(R.id.tv_telp);
        tvKotaScroll = view.findViewById(R.id.tv_kota);
        tvAlamatScroll = view.findViewById(R.id.tv_alamat);
        tvNopol = view.findViewById(R.id.tv_nopol);

        imgProfile.setImageDrawable(TextDrawable.builder()
                .buildRound(driver.getInitial(), ColorGenerator.MATERIAL.getColor(driver.getIdDriver())));
        tvNamaProfil.setText(driver.getUsername());
        tvEmailScroll.setText(driver.getEmail());
        tvTelpScroll.setText(driver.getTelp());
        tvAlamatScroll.setText(driver.getAlamat());
        tvNopol.setText(kendaraan.getIdNopol());
        tvKotaScroll.setText(driver.getKota());

        try {
            PackageInfo pi = getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            tvVersion.setText(getString(R.string.app_name)+" "+pi.versionName+" ("+pi.versionCode+")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            new AlertDialog.Builder(getContext())
                    .setTitle("Keluar aplikasi")
                    .setMessage("Apakah anda yakin ingin keluar aplikasi?")
                    .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logOut();
                        }
                    })
                    .setNegativeButton("TIDAK", null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        pd.show();
        StringHashMap shm = new StringHashMap()
                .putMore("id_kendaraan", kendaraan.getIdKendaraan())
                .putMore("id_driver", driver.getIdDriver())
                .putMore("email", driver.getEmail())                ;
        new Netter(getContext()).byAmik(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String message = obj.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    if(status == 200){
                        pref.clearDriver();
                        pref.clearKendaraan();
                        startActivity(new Intent(getContext(),MainActivity.class));
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(getContext(), new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }), Netter.Byamik.GETLOGOUT, shm);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
