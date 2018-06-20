package com.spil.dev.tms.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.spil.dev.tms.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangelogActivity extends BaseActivity {
    @BindView(R.id.tvChangelog)
    TextView tvChangeLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("ToMS Changelog");

        InputStream is = getResources().openRawResource(R.raw.changelog);
//        BufferedReader br = new BufferedReader(is);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String buff;
        try {
            while ((buff = br.readLine()) != null) {
                tvChangeLog.append(buff);
                tvChangeLog.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
