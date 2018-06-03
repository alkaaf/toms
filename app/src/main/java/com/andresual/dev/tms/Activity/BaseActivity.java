package com.andresual.dev.tms.Activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void longToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}