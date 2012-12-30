package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private Button resetBtn;
	private Intent intent;
	private SharedPreferences nickName;
	private SharedPreferences.Editor nickEditor;

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
		resetBtn = (Button)findViewById(R.id.history_btn_reset);
		
		nickName = getSharedPreferences("pref", Activity.MODE_PRIVATE);
	    nickEditor = nickName.edit();

		detailBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		new AsyncTask<Void, Integer, Void>() {

			protected void onPreExecute() {
				progress.setVisibility(View.VISIBLE);
				historyTxtViewMonth.setVisibility(View.GONE);
				historyTxtViewName.setVisibility(View.GONE);
				historyTxtViewCount.setVisibility(View.GONE);
				historyTxtViewAmount.setVisibility(View.GONE);
				detailBtn.setVisibility(View.GONE);
				resetBtn.setVisibility(View.GONE);
			};

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

			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				String nick = nickName.getString("value", "");

				progress.setVisibility(View.GONE);
				historyTxtViewMonth.setVisibility(View.VISIBLE);
				historyTxtViewName.setVisibility(View.VISIBLE);
				historyTxtViewCount.setVisibility(View.VISIBLE);
				historyTxtViewAmount.setVisibility(View.VISIBLE);
				detailBtn.setVisibility(View.VISIBLE);
				resetBtn.setVisibility(View.VISIBLE);

				historyTxtViewMonth.setText( "[" + (cal.get(Calendar.MONTH)+1) + "월] CarPool 내역");
				historyTxtViewName.setText( "- Mate : " + nick);
				historyTxtViewCount.setText("- 이용 횟수 : " + historyCount + "번");
				historyTxtViewAmount.setText("- 납부 금액 : " + Integer.parseInt(historyAmount) + "원");
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.history_btn_detail :
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(
					"https://docs.google.com/spreadsheet/ccc?key=0AhAUXFpCrNTedGYyS3FVV0ZhY1NYRGNPWVllNF9FbHc#gid=0"));
			startActivity(intent);
			break;
		case R.id.history_btn_reset :
			refresh();
			break;
		}
	}

	public void detailViewGoogleDocs() throws AuthenticationException, MalformedURLException, IOException, ServiceException {

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

		// Iterate through each row, printing its cell values.
		ListEntry rowCount = listFeed.getEntries().get(31);
		historyCount = rowCount.getCustomElements().getValue( nick );
		ListEntry rowAmount = listFeed.getEntries().get(32);
		historyAmount = rowAmount.getCustomElements().getValue( nick );
	}

}
