package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.MalformedURLException;
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
	
	String USERONE = "jaehoonlee1010";


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
		
		sb.append("��� ���� �Ϸ� *^^* \n")
		.append("������ ")
		.append(cal.get(Calendar.MONTH)+1)
		.append("�� ")
		.append(cal.get(Calendar.DATE))
		.append("�� �Դϴ�.");
		mainCheckinEditTxtDate.setText(sb.toString());

		String name = "������/������";
		StringBuilder sbName = new StringBuilder();
		sbName.append("\""+name+"\"�� ���� Ÿ�÷ƴϱ�?");
		// mainCheckinEditTxtName.setText(sbName.toString());
		
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

//				mainHistoryViewTxtName.setText(historyName.toString() + "�� !!");
				mainHistoryViewTxtName.setText(USERNAME +"��!!");
				mainHistoryViewTxtCount.setText(historyCount.toString() + "ȸ �̿� �ϼ̱� ������,");
				mainHistoryViewTxtTotal.setText("�� " + historyTotal.toString() + "�� ���ο��");
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.main_checkin_btn_send :
			new AlertDialog.Builder(this)
			.setTitle("����")
			.setMessage("�����Ͻðڽ��ϱ�?")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new SendToGooleDocs().execute();
					Toast.makeText(MainActivity.this, "���۵Ǿ����ϴ�.", 
							Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "��ҵǾ����ϴ�.", 
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
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mainCheckinEditTxtName.setText("���?");
		}

		public void send() throws AuthenticationException, MalformedURLException, IOException, ServiceException {
			SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");
			service.setUserCredentials(USERNAME, PASSWORD);
			// ��û URL����. �ٲ��� �ʴ´�.
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
		    
		    // ��
		    ListEntry row = listFeed.getEntries().get(28);
		    String currentCountString = row.getCustomElements().getValue(USERONE);
		    int currentCountInt = Integer.parseInt(currentCountString);
		    String setCount = String.valueOf(currentCountInt+1);
    		row.getCustomElements().setValueLocal(USERONE, setCount);
		    row.update();
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
