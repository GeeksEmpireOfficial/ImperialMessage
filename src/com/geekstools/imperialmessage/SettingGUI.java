package com.geekstools.imperialmessage;

import java.io.File;
import java.io.FileOutputStream;

import com.geekstools.imperialmessage.PassDial.RemoteSetPassDial;
import com.geekstools.imperialmessage.PassDial.SetPassDial;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class SettingGUI extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	SwitchPreference passSW;
	SwitchPreference remotePassSW;
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		addPreferencesFromResource(R.xml.setting);
		
		passSW = (SwitchPreference)findPreference("pass");
		passSW.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// Admin Pass
				
				
				return true;
			}
		});
		
		remotePassSW = (SwitchPreference)findPreference("passRemote");
		remotePassSW.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// Remote Admin Pass
				
				
				return true;
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		getPreferenceScreen().getSharedPreferences()
										.registerOnSharedPreferenceChangeListener(this);
		
		File pass = new File("//data//data//com.geekstools.imperialmessage//files//.pass");
		if(!pass.exists()){
			passSW.setChecked(false);
		}
		
		File passRemote = new File("//data//data//com.geekstools.imperialmessage//files//.remotepass");
		if(!passRemote.exists()){
			remotePassSW.setChecked(false);
		}
	}
	
	@Override
	public void onPause(){
		getPreferenceScreen().getSharedPreferences()
        						.unregisterOnSharedPreferenceChangeListener(this);
		
		File pass = new File("//data//data//com.geekstools.imperialmessage//files//.pass");
		if(pass.exists()){
			passSW.setChecked(true);
		}
		
		File passRemote = new File("//data//data//com.geekstools.imperialmessage//files//.remotepass");
		if(passRemote.exists()){
			remotePassSW.setChecked(true);
		}
		
		super.onPause();
	}
	
	@Override
    public void onBackPressed(){
    	super.onBackPressed();
    	finish();
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		SharedPreferences passPref = PreferenceManager.getDefaultSharedPreferences(this);
		File pass = new File("//data//data//com.geekstools.imperialmessage//files//.pass");
	
		if(passPref.getBoolean("pass", false) == true){
			if(!pass.exists()){
				PassDial p = new PassDial(SettingGUI.this);
				SetPassDial sPass = p.new SetPassDial(SettingGUI.this);
				sPass.show();
			
				passSW.setChecked(false);
			}
		}
		
		File passRemote = new File("//data//data//com.geekstools.imperialmessage//files//.remotepass");
		
		if(passPref.getBoolean("passRemote", false) == true){
			if(!passRemote.exists()){
				PassDial p = new PassDial(SettingGUI.this);
				RemoteSetPassDial r = p.new RemoteSetPassDial(SettingGUI.this);
				r.show();
			
				remotePassSW.setChecked(false);
			}
		}
	}

	
	private void Save(String toS, String file){
		try{
			FileOutputStream fout = openFileOutput(file, MODE_PRIVATE);
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
	public void System(String s){
		System.out.println(s);
	}
	public void Toast(String s){
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
}
