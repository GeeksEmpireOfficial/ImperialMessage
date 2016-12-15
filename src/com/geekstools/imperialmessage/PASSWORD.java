package com.geekstools.imperialmessage;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PASSWORD extends Activity{

	PassDial dial;
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);

		SharedPreferences passSW = PreferenceManager.getDefaultSharedPreferences(this);
		File pass = new File("//data//data//com.geekstools.imperialmessage//files//.pass");
		
		if((passSW.getBoolean("pass", false) == true) || pass.exists()){
			if(pass.exists()){
				System.out.println("Password Check");
				try{
					dial = new PassDial(PASSWORD.this);
					dial.show();
				}
				catch(Exception e){
					System.out.println(e);
				}
		
				dial.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						dial.dismiss();
						finish();
					}
				});
				dial.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						dial.cancel();
						finish();
					}
				});
				return;
			}
			else{
				System.out.println("Password File Doesn't Exist");
				Toast.makeText(getApplicationContext(), "Password Identifier is ON\n" + "But There is no Password Data\n"
									+ "Contact Administrator",
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
		else if((passSW.getBoolean("pass", false) == false) && !pass.exists()){
			Intent i = new Intent(PASSWORD.this, GUI.class);
			startActivity(i);
			
			finish();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		SharedPreferences passSW = PreferenceManager.getDefaultSharedPreferences(this);
		File pass = new File("//data//data//com.geekstools.imperialmessage//files//.pass");
		
		if((passSW.getBoolean("pass", false) == true) || pass.exists()){
			if(pass.exists()){
				System.out.println("Password Check");
				try{
					dial = new PassDial(PASSWORD.this);
					dial.show();
				}
				catch(Exception e){
					System.out.println(e);
				}
		
				dial.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
					}
				});
				return;
			}
			else{
				System.out.println("Password File Doesn't Exist");
				Toast.makeText(getApplicationContext(), "Password Identifier is ON\n" + "But There is no Password Data\n"
									+ "Contact Administrator",
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
		else if((passSW.getBoolean("pass", false) == false) && !pass.exists()){
			Intent i = new Intent(PASSWORD.this, GUI.class);
			startActivity(i);
			
			finish();
		}
	}
	
	@Override
	public void onPause(){
		finish();
		
		super.onPause();
	}
}
