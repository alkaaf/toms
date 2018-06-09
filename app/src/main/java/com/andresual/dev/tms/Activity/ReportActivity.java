package com.andresual.dev.tms.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.andresual.dev.tms.R;

public class ReportActivity extends BaseActivity {
    SearchView sv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Daftar laporan");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        sv = ((SearchView) menu.getItem(R.id.action_search).getActionView());
//        sv.setQueryHint("Cari laporan...");
        return super.onCreateOptionsMenu(menu);
    }

}
