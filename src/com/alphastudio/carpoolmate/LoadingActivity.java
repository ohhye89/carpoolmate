package com.alphastudio.carpoolmate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Activity;

public class LoadingActivity extends Activity {

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        
        Handler h = new Handler() {
        	public void handleMessage(Message msg) {
        		Log.e("TAG", "TEST LOGGGGGG");
        		finish();
        	}
        };
        h.sendEmptyMessageDelayed(0, 3000);
    }    
    
}
