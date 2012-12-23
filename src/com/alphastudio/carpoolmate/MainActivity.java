package com.alphastudio.carpoolmate;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnClickListener {
	private TabHost mTabHost;
	private TextView mainCheckinEditTxtDate;
	private TextView mainCheckinEditTxtName;
	private Button sendBtn;
	private Intent intent;
	final static int GET_ID = 0;
	
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
		mainCheckinEditTxtName.setText(sbName.toString());
		
		sendBtn = (Button)findViewById(R.id.main_checkin_btn_send);
		sendBtn.setOnClickListener(this);
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
		}
		
	}
}
