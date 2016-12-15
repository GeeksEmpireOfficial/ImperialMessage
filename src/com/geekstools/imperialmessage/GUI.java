package com.geekstools.imperialmessage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.geekstools.imperialmessage.PassDial.SetPassDial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GUI extends ActionBarActivity {

	private EditText name;
	private EditText number;
	private EditText email;
	private EditText address;
	private Button grant;
	
	private DBHelper iDB ;
	int id_To_Update = 0;
	
	private String getAccess;
	private String whoAccess;
	
	private String num;
	private String msg;
	private String access;
	
	private Context context;
	
	Coordinates gps;
	ReceiverConfigBoot configR;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences root = PreferenceManager.getDefaultSharedPreferences(this);
		if(root.getBoolean("root", false) == true){
			getRoot access = new getRoot();
			access.execute();
		}

		configR = new ReceiverConfigBoot();
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(configR != null){
					configR.SetAlarm(getApplicationContext());
				}
			}
		}, 100);
				
		iDB = new DBHelper(this);
		
		name = (EditText)findViewById(R.id.name);
		number = (EditText)findViewById(R.id.number);
		email = (EditText)findViewById(R.id.email);
		address = (EditText)findViewById(R.id.address);
		grant = (Button)findViewById(R.id.grant);
		
		grant.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				//Save Number
				String fNum = ".grantAccess";
				String numAccess = number.getText().toString();
				
				//Save Email
				String fMail = ".email";
				String emailAccess = email.getText().toString();
				
				if((numAccess.length() > 0) && (emailAccess.contains("@"))){
					Save(numAccess, fNum);
					Save(emailAccess, fMail);
					InsertDB();
				}
				else{
					Toast.makeText(getApplicationContext(), "Please Enter Valid E-mail & Phone-Number", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.show) {
	         Intent l = new Intent(GUI.this, ListAccess.class);
	         startActivity(l);
			
			return true;
		}
		if(id == R.id.setting){
			Intent s = new Intent(GUI.this, SettingGUI.class);
			startActivity(s);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**********************
	 ***** Functions ******
	 ***********************/
	
	public class getRoot extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			access = "NULL";
		}
		
		@Override
		protected String doInBackground(String... params) {
			 Process p;   
			    try {
			       p = Runtime.getRuntime().exec("su");   
			         
			       DataOutputStream os = new DataOutputStream(p.getOutputStream());   
			       os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  

			       os.writeBytes("exit\n");   
			       os.flush();   
			       try {   
			          p.waitFor();   
			               if (p.exitValue() != 255) {   
			            	   System("ROOT ACCESS PERMITTED");
			            	   
			            	   access = "GRANTED";
			               }   
			               else {     
			            	   System("NO ROOT");
			            	   
			            	   access = "DENIED";
			               }   
			       } catch (InterruptedException e) {   
			    	   System("doBack InterruptedException: " + e);   
			       }   
			    } catch (IOException e) {
			    	System("doBack IOException: " + e);   
			    }  
			
			return access;
		}
		
		@Override
		protected void onPostExecute(String v){
			super.onPostExecute(v);
			
			if(v.equals("GRANTED")){
				Toast("ROOT ACCESS: " + v);
				System("postEXEC\n" + "ROOT ACCESS: " + v);
			}
			else{
				Toast.makeText(getApplicationContext(), "Go To SuperUser Setting & Grant Access For iM",
						Toast.LENGTH_LONG).show();
				System("postEXEC\n" + "ROOT ACCESS: " + v);
				GUI.this.finish();
			}
		}
	}
	public void System(String s){
		System.out.println(s);
	}
	public void Toast(String s){
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
	
	public void InsertDB(){
		if(!number.getText().toString().isEmpty()){
			if(iDB.insertContact(name.getText().toString(), number.getText().toString(), email.getText().toString(), address.getText().toString())){
	    		Toast.makeText(getApplicationContext(), "Imperial Access Granted To:\n" + number.getText().toString(),
	    				Toast.LENGTH_SHORT).show();	
	    	}		
	    	else{
	    		Toast.makeText(getApplicationContext(), "ERROR\n" + "Please Retry", Toast.LENGTH_SHORT).show();	
	    	}
		}
		else{
			Toast.makeText(getApplicationContext(), "Phone Number is Mandatory", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void Save(String toS, String file){
		File f = new File("//data//data//com.geekstools.imperialmessage//files//.grantAccess");
		toS = toS + "\n";
		
		try{
			FileOutputStream fout = openFileOutput(file, MODE_APPEND|MODE_PRIVATE);
			fout.write(toS.getBytes());
			
			fout.flush();
			fout.close();
			
			System("Access Granted to: " + toS);
			Toast("Access Granted to: " + toS);
		}
		catch(Exception e){
			System("ERROR: Saving Process\n" + e);
			Toast("ERROR: Saving Process\n" + e);
		}
	}
}
