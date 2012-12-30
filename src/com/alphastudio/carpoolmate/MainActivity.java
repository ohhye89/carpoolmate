package com.alphastudio.carpoolmate;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener {
	private TabHost mTabHost;
		
	private SharedPreferences nickName;
	private SharedPreferences isLogIn;
	private SharedPreferences.Editor nickEditor;
	private SharedPreferences.Editor isLogInEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = (TabHost)findViewById(R.id.tabhost);
		
		mTabHost.setup(getLocalActivityManager());
		
		Drawable checkInIcon = getResources().getDrawable(R.drawable.checkin);
		Drawable historyIcon = getResources().getDrawable(R.drawable.history);
		
		mTabHost.addTab(mTabHost.newTabSpec("tab_checkin")
		        .setIndicator("Check In", checkInIcon)
		        .setContent(new Intent(this, CheckInActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_history")
		        .setIndicator("History", historyIcon)
		        .setContent(new Intent(this, HistoryActivity.class)));
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_logout :
			nickName = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			isLogIn = getSharedPreferences("login", Activity.MODE_PRIVATE);
			
			nickEditor = nickName.edit();
		    isLogInEditor = isLogIn.edit();
		    
			nickEditor.remove("pref");
			nickEditor.commit();
			isLogInEditor.remove("login");
			isLogInEditor.commit();
			
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		
	}

}
