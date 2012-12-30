package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class HistoryActivity extends Activity implements OnClickListener {
	
	private TextView historyTxtViewMonth;
	private TextView historyTxtViewName;
	private TextView historyTxtViewCount;
	private TextView historyTxtViewAmount;
	private ProgressBar progress;
	private Button detailBtn;
	private Intent intent;
	
	private String historyCount = "";
	private String historyAmount = "";
	
	String MAINUSER = "ohej92@gmail.com";
	String PASSWORD = "carpoolmate";

	Calendar cal = Calendar.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_history);

	    historyTxtViewMonth = (TextView)findViewById(R.id.history_txtview_month);
	    historyTxtViewName = (TextView)findViewById(R.id.history_txtview_name);
	    historyTxtViewCount = (TextView)findViewById(R.id.history_txtview_count);
	    historyTxtViewAmount = (TextView)findViewById(R.id.history_txtview_total);
		progress = (ProgressBar)findViewById(R.id.history_prg_loading);
		detailBtn = (Button)findViewById(R.id.history_btn_detail);
		
		detailBtn.setOnClickListener(this);
		
		new AsyncTask<Void, Integer, Void>() {

			protected Void doInBackground(Void... params) {
				try {
					detailViewGoogleDocs();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onProgressUpdate(Integer... progressing){
				//				progress.setProgress(progressing[0]);
			}

			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				progress.setVisibility(View.INVISIBLE);
				historyTxtViewMonth.setText( "[" + (cal.get(Calendar.MONTH)+1) + "��] CarPool ����");
				historyTxtViewName.setText( "- Mate : " + GoogleID.getID());
				historyTxtViewCount.setText("- �̿� Ƚ�� : " + historyCount + "��");
				historyTxtViewAmount.setText("- ���� �ݾ� : " + Integer.parseInt(historyAmount) + "��");
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.history_btn_detail :
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("https://docs.google.com/spreadsheet/ccc?key=0AhAUXFpCrNTedGYyS3FVV0ZhY1NYRGNPWVllNF9FbHc#gid=0"));
			startActivity(intent);
			break;
		}
	}
	
	public void detailViewGoogleDocs() throws AuthenticationException, MalformedURLException, IOException, ServiceException {

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
		
		// 0�� �ε��� spreadsheet ���
		SpreadsheetEntry mySpreadsheet = spreadsheets.get(0);			
		
		// Make a request to the API to fetch information about all worksheets in the spreadsheet.
		List<WorksheetEntry> worksheets = mySpreadsheet.getWorksheets();
		
		// 0�� �ε��� worksheet Title ���
		WorksheetEntry myWorksheet = worksheets.get(0);
	   
		URL listFeedUrl = myWorksheet.getListFeedUrl();
	    ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		// Iterate through each row, printing its cell values.
	    ListEntry rowCount = listFeed.getEntries().get(31);
	    historyCount = rowCount.getCustomElements().getValue( GoogleID.getID() );
	    ListEntry rowAmount = listFeed.getEntries().get(32);
	    historyAmount = rowAmount.getCustomElements().getValue( GoogleID.getID() );
	}

}