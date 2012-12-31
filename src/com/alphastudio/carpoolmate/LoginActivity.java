package com.alphastudio.carpoolmate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener, RadioGroup.OnCheckedChangeListener {

	private RadioGroup radioBtnGroup;
	private EditText idEditTxt;
	private TextView idTxt;
	private Button loginBtn;
	private Button findBtn;
	private Intent intent;
	private Boolean isMate = true;
	private Boolean isLog = false;
	private SharedPreferences nickName;
	private SharedPreferences isLogIn;
	private SharedPreferences.Editor nickEditor;
	private SharedPreferences.Editor isLogInEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// startActivity(new Intent(this, LoadingActivity.class));
		nickName = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		isLogIn = getSharedPreferences("login", Activity.MODE_PRIVATE);
		nickEditor = nickName.edit();
		isLogInEditor = isLogIn.edit();

		radioBtnGroup = (RadioGroup)findViewById(R.id.login_radiogroup);
		idEditTxt = (EditText)findViewById(R.id.login_edittxt_id);
		idTxt = (TextView)findViewById(R.id.login_txt_id);
		loginBtn = (Button)findViewById(R.id.login_btn_login);
		findBtn = (Button)findViewById(R.id.login_btn_find);
		radioBtnGroup.setOnCheckedChangeListener(this);
		loginBtn.setOnClickListener(this);
		findBtn.setOnClickListener(this);

		isLog = isLogIn.getBoolean("login", false);

		if(isLog) {
			intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_loading, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.login_btn_login :
			if(isMate) {
				String nick = idEditTxt.getText().toString();
				nickEditor.putString("value", nick);
				isLogInEditor.putBoolean("login", true);
				nickEditor.commit();
				isLogInEditor.commit();
				
				intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();
			}
			else {
				intent = new Intent(LoginActivity.this, MainCarActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
			break;
			
		case R.id.login_btn_find :
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("https://www.google.com/accounts/recovery"));
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId) {
		case R.id.login_radiobtn_car :
			isMate = false;
			idEditTxt.setVisibility(View.GONE);
			idTxt.setVisibility(View.GONE);
			break;

		case R.id.login_radiobtn_mate :
			isMate = true;
			idEditTxt.setVisibility(View.VISIBLE);
			idTxt.setVisibility(View.VISIBLE);
			break;
		}

	}
}
