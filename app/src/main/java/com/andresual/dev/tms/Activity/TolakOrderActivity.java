package com.andresual.dev.tms.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Controller.OperationalController;
import com.andresual.dev.tms.Activity.Model.AlasanModel;
import com.andresual.dev.tms.Activity.Model.TolakModel;
import com.andresual.dev.tms.R;

public class TolakOrderActivity extends AppCompatActivity {

    RadioButton rb1, rb2, rb3, rb4, rb5;
    RadioGroup rgAlasan;
    String alasan, email;
    Integer jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tolak_order);

//        rgAlasan = findViewById(R.id.rg1);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);
        Button btnSubmit = (Button) findViewById(R.id.btn_kirim_alasan);

        jobId = this.getIntent().getIntExtra("jobId", 0);
        email = this.getIntent().getStringExtra("email");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rb1.isChecked()) {
                    alasan = rb1.getText().toString();
                } else if (rb2.isChecked()) {
                    alasan = rb2.getText().toString();
                } else if (rb3.isChecked()) {
                    alasan = rb3.getText().toString();
                } else if (rb4.isChecked()) {
                    alasan = rb4.getText().toString();
                } else if (rb5.isChecked()) {
                    alasan = rb5.getText().toString();
                }

                TolakModel tolakModel = new TolakModel();
                tolakModel.setEmail(email);
                tolakModel.setIdJob(jobId.toString());
                tolakModel.setRejectNote(alasan);
                Toast.makeText(TolakOrderActivity.this, alasan ,Toast.LENGTH_SHORT).show();

                OperationalController.getmInstance().setTolakModel(tolakModel);
                OperationalController.getmInstance().rejectJob(TolakOrderActivity.this);
                Intent intent = new Intent(TolakOrderActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
