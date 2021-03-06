package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class CheckInActivity extends Activity implements OnClickListener {
	
	private TextView checkInTxtViewDate;
	private TextView checkInTxtViewName;
	private Button sendBtn;
	
	private SharedPreferences nickName;
	private SharedPreferences isLogIn;
	private SharedPreferences.Editor nickEditor;
	private SharedPreferences.Editor isLogInEditor;
	
		
	String MAINUSER = "ohej92@gmail.com";
	String PASSWORD = "carpoolmate";
	
	Calendar cal = Calendar.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_checkin);
	    
	    checkInTxtViewDate = (TextView)findViewById(R.id.chekckin_txtview_date);
	    checkInTxtViewName = (TextView)findViewById(R.id.checkin_txtview_name);
	    sendBtn = (Button)findViewById(R.id.checkin_btn_send);
	    
	    nickName = getSharedPreferences("pref", Activity.MODE_PRIVATE);
	    // nickEditor = nickName.edit();
	    
	    sendBtn.setOnClickListener(this);
	    
	    StringBuilder sb = new StringBuilder();
		sb.append("Today is..\n")
		  .append(cal.get(Calendar.YEAR))
		  .append(".")
		  .append(cal.get(Calendar.MONTH)+1)
		  .append(".")
		  .append(cal.get(Calendar.DATE));
		checkInTxtViewDate.setText(sb.toString());
		
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
			
			isLogIn.getBoolean("login", false);
			Intent intent = new Intent(CheckInActivity.this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.checkin_btn_send :
			new AlertDialog.Builder(this)
			.setTitle("탑승")
			.setMessage("탑승하시겠습니까?")
			.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Calendar cal = Calendar.getInstance();
					StringBuilder sb = new StringBuilder();
					sb.append("방금 탑승시간은,\n")
					  .append(cal.get(Calendar.HOUR))
					  .append("시 ")
					  .append(cal.get(Calendar.MINUTE))
					  .append("분 입니다.");
					checkInTxtViewName.setText("");
					checkInTxtViewName.setText(sb.toString());
					
					new SendToGooleDocs().execute();
					
					Toast.makeText(CheckInActivity.this, "탑승처리 되었습니다.", 
							Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(CheckInActivity.this, "취소되었습니다.", 
							Toast.LENGTH_SHORT).show();						
				}
			}).create().show();
			
			break;
		}
	}
	
private class SendToGooleDocs extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				send();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		public void send() throws AuthenticationException, MalformedURLException, IOException, ServiceException {
			String nick = nickName.getString("value", "");
			SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");
			service.setUserCredentials(MAINUSER, PASSWORD);
			// Request URL Define. Never Change.
			URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
			// Make a request to the API and get all spreadsheets.
			SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
			if (spreadsheets.size() == 0) {      
				// TODO: There were no spreadsheets, act accordingly.
			}
			
			// 0번 인덱스 spreadsheet 출력
			SpreadsheetEntry mySpreadsheet = spreadsheets.get(0);			
			
			// Make a request to the API to fetch information about all worksheets in the spreadsheet.
			List<WorksheetEntry> worksheets = mySpreadsheet.getWorksheets();
			
			// 0번 인덱스 worksheet Title 출력
			WorksheetEntry myWorksheet = worksheets.get(0);
		   
			URL listFeedUrl = myWorksheet.getListFeedUrl();
		    ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		    
		    // 
		    ListEntry row = listFeed.getEntries().get( cal.get(Calendar.DATE)-1 );
		    String currentCountString = row.getCustomElements().getValue( nick );
		    int currentCountInt = Integer.parseInt(currentCountString);
		    String setCount = String.valueOf(currentCountInt+1);
    		row.getCustomElements().setValueLocal( nick, setCount );
		    row.update();
		}
	}

}
