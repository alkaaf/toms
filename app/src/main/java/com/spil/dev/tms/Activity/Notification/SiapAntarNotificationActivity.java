package com.spil.dev.tms.Activity.Notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spil.dev.tms.Activity.Controller.NotificationController;
import com.spil.dev.tms.Activity.TolakOrderActivity;
import com.spil.dev.tms.R;

public class SiapAntarNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siap_antar_notification);

        Button btnTolak = findViewById(R.id.btn_tolak);
        Button btnSiapAntar = findViewById(R.id.btn_siap_antar);
        btnSiapAntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationController.getmInstance().siapAntarJob(SiapAntarNotificationActivity.this);
                Intent intent = new Intent(SiapAntarNotificationActivity.this, MengantarNotificationActivity.class);
                startActivity(intent);
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SiapAntarNotificationActivity.this, TolakOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}
