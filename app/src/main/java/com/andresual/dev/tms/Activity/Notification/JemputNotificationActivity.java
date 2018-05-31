package com.andresual.dev.tms.Activity.Notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andresual.dev.tms.Activity.Controller.NotificationController;
import com.andresual.dev.tms.Activity.Controller.OperationalController;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.MengantarActivity;
import com.andresual.dev.tms.Activity.OrderBaruNotificationActivity;
import com.andresual.dev.tms.Activity.TolakOrderActivity;
import com.andresual.dev.tms.R;

public class JemputNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jemput_notification);

        Button btnJemput = findViewById(R.id.btn_terima);
        Button btnTolak = findViewById(R.id.btn_tolak);

        btnJemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationController.getmInstance().jemputJob(JemputNotificationActivity.this);
                Intent intent = new Intent(JemputNotificationActivity.this, SiapAntarNotificationActivity.class);
                startActivity(intent);
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JemputNotificationActivity.this, TolakOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}
