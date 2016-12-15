package com.geekstools.imperialmessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import com.geekstools.imperialmessage.PassDial.RemoteSetPassDial;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.os.PowerManager;
import android.widget.Toast;

public class Commands {

	public void WakeUp(Context context){
		PowerManager PM;
		PowerManager.WakeLock WL;
		
		PM = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WL = PM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CONFIG");
		System.out.println("CMD: WakeUp");
		
		//TODO 
		
		WL.acquire();
		WL.release();
	}
	
	public void WiFiON(Context context){
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		System.out.println("CMD: WiFiON");
	}
	
	public void WiFiOFF(Context context){
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(false);
		System.out.println("CMD: WiFiOFF");
	}
	
	public void ringMax(Context context){
		AudioManager amanager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		int i = amanager.getStreamMaxVolume(AudioManager.STREAM_RING);
		amanager.setStreamVolume(AudioManager.STREAM_RING, i, AudioManager.FLAG_PLAY_SOUND);
		System.out.println("CMD: ringMax");
	}
	
	public void ringLevel(Context context, int level){
		AudioManager amanager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		int i = level;
		amanager.setStreamVolume(AudioManager.STREAM_RING, i, AudioManager.FLAG_PLAY_SOUND);
		System.out.println("CMD: ringLevel");
	}
	
	public void setAdmin(Activity activity, Parcelable AdminValue, int ACTIVATION_REQUEST) throws Exception{
		Intent a = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		a.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, AdminValue);
		a.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "ImerialMessage Commands You!");
		activity.startActivityForResult(a, ACTIVATION_REQUEST);
		System.out.println("CMD: setAdmin");
	}
	
	public void Reboot(Context context, int password) throws Exception{
		int pass = Password();
		
		if(pass == password){
			try {
				System.out.println("CMD: Reboot");
			
				Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
				proc.waitFor();
			} catch (Exception ex) {
				System.out.println("Error Reboot " + ex);
			}
		}
		else{
			Toast.makeText(context, "Illegal Access Denied", Toast.LENGTH_LONG).show();
			System.out.println("Illegal Access Detected");
		}
	}
	
	public void LockDown(DevicePolicyManager devicePolicyManager) throws Exception{
		devicePolicyManager.lockNow();
		
		System.out.println("CMD: LockDown");
	}
	
	public void WipeInternal(DevicePolicyManager devicePolicyManager, int ACTIVATION_REQUEST, int password) throws Exception{
		int pass = Password();
		
		if(password == pass){
			System.out.println("CMD: WipeInternal");
			
			devicePolicyManager.wipeData(ACTIVATION_REQUEST);
		}
		else{
			System.out.println("Illegal Access Detected");
		}
	}
	
	public void WipeExternal(DevicePolicyManager devicePolicyManager, int password) throws Exception{
		int pass = Password();
		
		if(password == pass){
			System.out.println("CMD: WipeExternal");
			
			devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}
		else{
			System.out.println("Illegal Access Detected");
		}
	}
	
	public void ResetDevicePass(DevicePolicyManager devicePolicyManager, String newPass, int remotePass) throws Exception{
		System.out.println("CMD: ResetDevicePass");
		int p = Password();
		if(remotePass == p){
			devicePolicyManager.resetPassword(newPass, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
		}
		else{
			System.out.println("Illegal Access Detected");
		}
	}
	
	public void SetNewPass(int oldPass, int newPass, Activity act){
		//set new pass for app admin /**Change pass file entry**/
		System.out.println("CMD: SetNewPass");
		int i = Password();
		
		if(oldPass == i){
			String filePassRemote = ".remotepass";
			String newPassWord = "" + newPass;
			
			Save(newPassWord, filePassRemote, act);
		}
		else{
			System.out.println("Illegal Access Detected");
		}
	}
	
	/**********************************/
	private int Password(){
		int p = 000;
		File passRemote = new File("//data//data//com.geekstools.imperialmessage//files//.remotepass");
		
		try{
			  FileInputStream fin = new FileInputStream(passRemote);
			  BufferedReader br = new BufferedReader(new InputStreamReader(fin, "UTF-8"), 1024);
			  
			  int c;
			  String temp = "";
			  while( (c = br.read()) != -1){
				  temp = temp + Character.toString((char)c);
			  }
			 String PASS = temp;
			 p = Integer.parseInt(PASS);
		  }
		  catch(Exception e){
			  System.out.println("ERROR Reading Password" + e);
		  }
		
		return p;
	}
	private void Save(String toS, String file, Activity act){
		try{
			FileOutputStream fout = act.openFileOutput(file, act.MODE_PRIVATE);
			fout.write(toS.getBytes());
			
			fout.flush();
			fout.close();

			Toast.makeText(act, "PASSWORD SET SUCCESSFULLY", Toast.LENGTH_SHORT).show();
		}
		catch(Exception e){
			System.out.println("ERROR: Saving Process\n" + e);
		}
	}
}
