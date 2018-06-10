package com.andresual.dev.tms.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Adapter.ReportAdapter;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.Report;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportListActivity extends BaseActivity {
    SearchView sv;
    List<Report> reportList = new ArrayList<>();
    List<Report> reportListFiltered = new ArrayList<>();
    ReportAdapter adapter = new ReportAdapter();
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.vNoData)
    View vNoData;

    DriverModel driver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        driver = new Pref(this).getDriverModel();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daftar laporan");
            getSupportActionBar().setSubtitle(driver.getUsername());
        }

        adapter.setData(reportListFiltered);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                vNoData.setVisibility(reportListFiltered.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        adapter.notifyDataSetChanged();
        fetchData();
    }

    public void fetchData() {
        swipe.setRefreshing(true);
        new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipe.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        List<Report> temp = new Gson().fromJson(obj.getString("data"), new TypeToken<List<Report>>() {
                        }.getType());
                        reportList.clear();
                        reportList.addAll(temp);
                        filter(sv.getQuery().toString());
                    } else {
                        Toast.makeText(ReportListActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, Netter.getSilentErrorListener(this, new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        }), Netter.Webservice.REPORTLIST, new StringHashMap().putMore("email", driver.getEmail()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        sv = ((SearchView) menu.findItem(R.id.action_search).getActionView());
        sv.setQueryHint("Cari laporan...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void filter(String query) {
        reportListFiltered.clear();
        if (TextUtils.isEmpty(query)) {
            reportListFiltered.addAll(reportList);
        } else {
            for (int i = 0; i < reportList.size(); i++) {
                if (reportList.get(i).getLokasi().toLowerCase().contains(query.toLowerCase()) ||
                        reportList.get(i).getCatatan().toLowerCase().contains(query.toLowerCase()) ||
                        reportList.get(i).getNopol().toLowerCase().contains(query.toLowerCase())
                        ) {
                    reportListFiltered.add(reportList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
