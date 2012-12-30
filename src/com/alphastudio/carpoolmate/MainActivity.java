package com.alphastudio.carpoolmate;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener {
	private TabHost mTabHost;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = (TabHost)findViewById(R.id.tabhost);
		
		mTabHost.setup(getLocalActivityManager());
		
		mTabHost.addTab(mTabHost.newTabSpec("tab_checkin")
		        .setIndicator("Check In")
		        .setContent(new Intent(this, CheckInActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_history")
		        .setIndicator("History")
		        .setContent(new Intent(this, HistoryActivity.class)));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
