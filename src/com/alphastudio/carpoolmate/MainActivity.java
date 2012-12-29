package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

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
