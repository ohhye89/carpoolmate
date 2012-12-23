package com.alphastudio.carpoolmate;

import java.io.IOException;

import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText idEditTxt;
	private EditText passwordEditTxt;
	private Button loginBtn;
	private Button findBtn;
	private Intent intent;
	final static int GET_ID = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					new SpreadSheetManager().spreadSheetTest();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				return null;
			}
	    	
		}.execute();
	    
	    
//	    startActivity(new Intent(this, LoadingActivity.class));
	    
	    idEditTxt = (EditText)findViewById(R.id.login_edittxt_id);
	    passwordEditTxt = (EditText)findViewById(R.id.login_edittxt_password);
	    loginBtn = (Button)findViewById(R.id.login_btn_login);
	    findBtn = (Button)findViewById(R.id.login_btn_find);
	    loginBtn.setOnClickListener(this);
	    findBtn.setOnClickListener(this);
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
			intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);

		    idEditTxt.setText("");
		    passwordEditTxt.setText("");
			break;
		case R.id.login_btn_find :
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("https://www.google.com/accounts/recovery"));
			startActivity(intent);
			break;
		}
	}

	
}
