package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class MainCarActivity extends Activity implements OnClickListener {
	
	private String MAINUSER = "ohej92@gmail.com";
	private String PASSWORD = "carpoolmate";
	
	private TextView maincarTxtViewMonth;
	private ProgressBar progress;
	private Button maincarBtnReset;
	
	private ListView listView;
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	
	private String[] carpoolData = {null, null};
	private String[] maincarNickName = {null, null};
	private String[] maincarCount = {null, null};
	private String[] maincarAmount = {null, null};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_maincar);
	
	    maincarTxtViewMonth = (TextView)findViewById(R.id.maincar_txtview_month);
	    progress = (ProgressBar)findViewById(R.id.maincar_prg_loading);
	    maincarBtnReset = (Button)findViewById(R.id.maincar_btn_reset);
	    maincarBtnReset.setOnClickListener(this);
	    
	    listView = (ListView)findViewById(R.id.listView);
	    list = new ArrayList<String>();

	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
	    listView.setAdapter(adapter);
	    listView.setDivider(new ColorDrawable(Color.BLUE));
	    listView.setDividerHeight(2);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.maincar_btn_reset :
			maincarBtnReset.setVisibility(View.INVISIBLE);
			list.clear();
			refresh();
		}
	}
	private void refresh() {
		new AsyncTask<Void, Integer, Void>() {
	
			protected Void doInBackground(Void... params) {
				try {
					getViewGoogleDocs();
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
				Calendar cal = Calendar.getInstance();
				StringBuilder sb = new StringBuilder();
				sb.append("[ ")
				  .append(cal.get(Calendar.MONTH)+1)
				  .append("월 ] ")
				  .append("Mate 현황");
				maincarTxtViewMonth.setText(sb.toString());
			}

			protected void onProgressUpdate(Integer... progressing){
	
			}
	
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				
				for(int i=0; i<2; i++) {
			    	carpoolData[i] = "Mate : " + maincarNickName[i] + "\n" +
				                      maincarCount[i] + "번 / " + maincarAmount[i] + "원";
			    	list.add(carpoolData[i]);
			    	adapter.notifyDataSetChanged();
			    }
				
				maincarBtnReset.setVisibility(View.VISIBLE);
				progress.setVisibility(View.INVISIBLE);
				  
				
			}
		}.execute();
	}
	
	public void getViewGoogleDocs() throws AuthenticationException, MalformedURLException, IOException, ServiceException {

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

		// Iterate through each row, printing its cell values.
	    int tagCheck=0;
	    ListEntry rowTag = listFeed.getEntries().get(0);
	    for (String tag : rowTag.getCustomElements().getTags()) {
    		if(tagCheck==1 || tagCheck==2) {
    			maincarNickName[(tagCheck-1)] = tag;
    		}
    		tagCheck++;
    	}
	    
	    for(int i=0; i<2; i++) {
	    	ListEntry row = listFeed.getEntries().get(31+i);
	    	for(int j=0; j<2; j++) {
	    		if(i==0) {
	    			maincarCount[j] = row.getCustomElements().getValue(maincarNickName[j]);
	    		}
	    		else { 
	    			maincarAmount[j] = row.getCustomElements().getValue(maincarNickName[j]);
	    		}
	    	}
	    } 	
    	
	}

}
