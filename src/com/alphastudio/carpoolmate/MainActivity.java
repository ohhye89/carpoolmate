package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
public class MainActivity extends TabActivity implements OnClickListener {
	private TabHost mTabHost;
	private TextView mainCheckinEditTxtDate;
	private TextView mainCheckinEditTxtName;
	private TextView mainHistoryViewTxtName;
	private TextView mainHistoryViewTxtCount;
	private TextView mainHistoryViewTxtTotal;
	private ProgressBar progress;
	private Button sendBtn;
	private Button detailBtn;
	private Intent intent;

	private String historyName;
	private String historyCount;
	private String historyTotal;
	final static int GET_ID = 0;

	String USERNAME = "ohej92@gmail.com";
	String PASSWORD = "carpoolmate";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = getTabHost();
		mTabHost.addTab(mTabHost.newTabSpec("tab1")
				.setContent(R.id.main_tap_checkin)
				.setIndicator("Check in"));
		mTabHost.addTab(mTabHost.newTabSpec("tab2")
				.setContent(R.id.main_tap_history)
				.setIndicator("History"));

		mainCheckinEditTxtDate = (TextView)findViewById(R.id.main_chekckin_txtview_date);
		mainCheckinEditTxtName = (TextView)findViewById(R.id.main_checkin_txtview_name);
		mainHistoryViewTxtName = (TextView)findViewById(R.id.main_history_txtview_name);
		mainHistoryViewTxtCount = (TextView)findViewById(R.id.main_history_txtview_count);
		mainHistoryViewTxtTotal = (TextView)findViewById(R.id.main_history_txtview_total);
		progress = (ProgressBar)findViewById(R.id.main_history_prg_loading);

		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();

		sb.append("기분 좋은 하루 *^^* \n")
		.append("오늘은 ")
		.append(cal.get(Calendar.MONTH)+1)
		.append("월 ")
		.append(cal.get(Calendar.DATE))
		.append("일 입니다.");
		mainCheckinEditTxtDate.setText(sb.toString());

		String name = "이재훈/오혜성";
		StringBuilder sbName = new StringBuilder();
		sbName.append("\""+name+"\"님 같이 타시렵니까?");
		mainCheckinEditTxtName.setText(sbName.toString());

		sendBtn = (Button)findViewById(R.id.main_checkin_btn_send);
		detailBtn = (Button)findViewById(R.id.main_history_btn_detail);

		sendBtn.setOnClickListener(this);
		detailBtn.setOnClickListener(this);

		new AsyncTask<Void, Integer, Void>() {

			@Override
			protected void onPreExecute(){
				//				publishProgress(0);
			}

			protected Void doInBackground(Void... params) {
				try {
					spreadSheetTest();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}

				//				for(int i = 1; i <= 10 ; i++) {
				//
				//					try {
				//						publishProgress(i);
				//						Thread.sleep(100);
				//					} catch (InterruptedException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					}
				//				}

				return null;
			}

			protected void onProgressUpdate(Integer... progressing){
				//				progress.setProgress(progressing[0]);
			}

			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Toast.makeText(MainActivity.this, R.string.LoadingSuccess, Toast.LENGTH_LONG).show();
				progress.setVisibility(View.INVISIBLE);

//				mainHistoryViewTxtName.setText(historyName.toString() + "님 !!");
				mainHistoryViewTxtName.setText(USERNAME +"님!!");
				mainHistoryViewTxtCount.setText(historyCount.toString() + "회 이용 하셨기 때문에,");
				mainHistoryViewTxtTotal.setText("총 " + historyTotal.toString() + "원 납부요망");
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.main_checkin_btn_send :
			new AlertDialog.Builder(this)
			.setTitle("전송")
			.setMessage("전송하시겠습니까?")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "전송되었습니다.", 
							Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "취소되었습니다.", 
							Toast.LENGTH_SHORT).show();						
				}
			}).create().show();
			break;
		case R.id.main_history_btn_detail :
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("https://docs.google.com/spreadsheet/ccc?key=0AhAUXFpCrNTedGYyS3FVV0ZhY1NYRGNPWVllNF9FbHc#gid=0"));
			startActivity(intent);
			break;

		}
	}

	public void spreadSheetTest() throws IOException, ServiceException{
		SpreadsheetService service = auth();
		int i = 1;
		int j = 1;

		URL SPREADSHEET_FEED_URL = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		if (spreadsheets.size() == 0) {
			// TODO: There were no spreadsheets, act accordingly.
		}

		// TODO: Choose a spreadsheet more intelligently based on your
		// app's needs.
		SpreadsheetEntry spreadsheet = spreadsheets.get(0);
		System.out.println(spreadsheet.getTitle().getPlainText());

		// Get the first worksheet of the first spreadsheet.
		// TODO: Choose a worksheet more intelligently based on your
		// app's needs.
		WorksheetFeed worksheetFeed = service.getFeed(
				spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);

		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		// Iterate through each row, printing its cell values.
		
		for (ListEntry row : listFeed.getEntries()){
			if(j == 32){
				for (String tag : row.getCustomElements().getTags()) {
					if(i == 2)
						historyCount = row.getCustomElements().getValue(tag).toString();
					i++;
				}
				i = 1;
			}
			if(j == 33){
				for (String tag : row.getCustomElements().getTags()) {
					if(i == 2)
						historyTotal = row.getCustomElements().getValue(tag).toString();
					i++;
				}
				i = 1;
			}
			j++;
		}

	}

	private SpreadsheetService auth() throws AuthenticationException {
		SpreadsheetService service = 
				new SpreadsheetService("MySpreadsheetIntegration-v1");

		service.setUserCredentials(USERNAME, PASSWORD);

		return service;
	}

}
