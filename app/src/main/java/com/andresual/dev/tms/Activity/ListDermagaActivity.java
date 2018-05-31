package com.andresual.dev.tms.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andresual.dev.tms.Activity.Adapter.DepoListAdapter;
import com.andresual.dev.tms.Activity.Adapter.DermagaListAdapter;
import com.andresual.dev.tms.Activity.Model.DepoModel;
import com.andresual.dev.tms.Activity.Model.DermagaModel;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListDermagaActivity extends AppCompatActivity {

    RecyclerView rvDermaga;
    DermagaListAdapter mAdapter;
    SearchView searchView;
    ArrayList<DermagaModel> dermagaModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dermaga);

        fetchDermaga();
        rvDermaga = findViewById(R.id.rv1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDermaga.setLayoutManager(mLayoutManager);
        rvDermaga.setHasFixedSize(true);
    }

    public void fetchDermaga() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "GetListDermaga");
        Log.i("fetchdermaga", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray dermagaArray = obj.getJSONArray("dermaga");
                            dermagaModelArrayList = new ArrayList<>();
                            for (int i = 0; i < dermagaArray.length(); i++) {
                                JSONObject hasil = dermagaArray.getJSONObject(i);
                                Log.i("haha", hasil.toString());
                                DermagaModel dermagaModel = new DermagaModel();
                                dermagaModel.setId(hasil.getString("id"));
                                dermagaModel.setNama(hasil.getString("nama"));
                                dermagaModelArrayList.add(dermagaModel);
                                Log.i("iddepo", dermagaModel.getId());

                                mAdapter = new DermagaListAdapter(ListDermagaActivity.this, dermagaModelArrayList);
                                rvDermaga.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
