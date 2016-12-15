package com.geekstools.imperialmessage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class CommandsHandler extends Activity{

	Coordinates gps;
	Commands cmd;
	Context context;
	
	private String num;
	private String msg;
	private String command;
	
	double Lat;
	double Long;
	private String city;
	
	private DevicePolicyManager devicePolicyManager;
	private ComponentName DeviceAdmin;
	
	static final int ACTIVATION_REQUEST = 37;
	
	private static final String regexPassR = "\\d{3}";
	private static final String regexPassD = "\\((.*?)\\)";
	
	private static final String regexPassRemoteOld = "\\d{3}\\*";
	private static final String regexPassRemoteNew = "\\*\\d{3}";
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		setContentView(R.layout.background);
		
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		DeviceAdmin = new ComponentName(this, CommandsHandler.class);
		
		cmd = new Commands();
		context = getApplicationContext();
		gps = new Coordinates(context);
		
		Intent i = getIntent();
		
		if(i.getExtras() != null){
			msg = i.getStringExtra("cmd");
			num = i.getStringExtra("num");
			System.out.println("MSG Commands Check: " + msg);
			Toast.makeText(getApplicationContext(), "MSG Commands Check:\n" + msg, Toast.LENGTH_SHORT).show();
			
			msg = msg.toUpperCase();
			System.out.println("upCase MSG: " + msg);
			
			if(msg.contains("SET ADMIN")){
				try{
					cmd.setAdmin(CommandsHandler.this, DeviceAdmin, ACTIVATION_REQUEST);
				}
				catch(Exception e){
					System("ERROR: " + e);
				}
				
				finish();
			}
			if(msg.contains("REBOOT")){
				Pattern p = Pattern.compile(regexPassR);
				Matcher m = p.matcher(msg);
				
				if(m.find()){
					int pass = Integer.parseInt(m.group());
					try{
						cmd.Reboot(context, pass);
					}
					catch(Exception e){
						System("ERROR: " + e);
					}
					
					finish();
				}
			}
			if(msg.contains("LOCK DOWN")){
				try{
					cmd.LockDown(devicePolicyManager);
				}
				catch(Exception e){
					System("ERROR: " +  e);
				}
				
				finish();
			}
			if(msg.contains("WIPE SYSTEM")){
				Pattern p = Pattern.compile(regexPassR);
				Matcher m = p.matcher(msg);
				
				if(m.find()){
					int pass = Integer.parseInt(m.group());
					try{
						cmd.WipeInternal(devicePolicyManager, ACTIVATION_REQUEST, pass);
					}
					catch(Exception e){
						System("ERROR: " + e);
					}
					
					finish();
				}
			}
			if(msg.contains("WIPE MEMORY")){
				Pattern p = Pattern.compile(regexPassR);
				Matcher m = p.matcher(msg);
				
				if(m.find()){
					int pass = Integer.parseInt(m.group());
					try{
						cmd.WipeExternal(devicePolicyManager, pass);
					}
					catch(Exception e){
						System("ERROR: " + e);
					}
					
					finish();
				}
			}
			if(msg.contains("RESET PASSWORD")){
				String newDevicePass = "ImperialMessage";
				Pattern n = Pattern.compile(regexPassD);
				Matcher r = n.matcher(msg);
				
				if(r.find()){
					newDevicePass = r.group(1);
				}
				
				Pattern p = Pattern.compile(regexPassR);
				Matcher m = p.matcher(msg);
				
				if(m.find()){
					int pass = Integer.parseInt(m.group());
					try{
						cmd.ResetDevicePass(devicePolicyManager, newDevicePass, pass);
					}
					catch(Exception e){
						System("ERROR: " + e);
					}
					
					finish();
				}
			}
			if(msg.contains("SET NEW ADMIN PASS")){
				String Old = "old";
				String New = "new";
				
				int oldP = 111;
				int newP = 999;
				
				Pattern pOld = Pattern.compile(regexPassRemoteOld);
				Matcher mOld = pOld.matcher(msg);
				if(mOld.find()){
					Old = mOld.group().replaceAll("\\*", "");
					
					oldP = Integer.parseInt(Old);
				}
								
				Pattern pNew = Pattern.compile(regexPassRemoteNew);
				Matcher mNew = pNew.matcher(msg);
				if(mNew.find()){
					New = mNew.group().replaceAll("\\*", "");
					
					newP = Integer.parseInt(New);
				}
				
				try{
					cmd.SetNewPass(oldP, newP, CommandsHandler.this);
				}
				catch(Exception e){
					System("ERROR: " + e);
				}
				
				finish();
			}
			if(msg.contains("WIFI OFF")){
				cmd.WiFiOFF(context);
				
				finish();
			}
			if(msg.contains("WIFI ON")){
				cmd.WiFiON(context);
				
				finish();
			}
			if(msg.contains("GET LOCATION")){
				//getLoc & SendBack
				sendLocation getLoc = new sendLocation();
				getLoc.execute();
				
				finish();
			}
			
			/****************************************/
			String regexSound = "RING" + "\\s" + "\\d{1}";
			Pattern p = Pattern.compile(regexSound);
			Matcher m = p.matcher(msg);
			
			if(m.find()){
				System.out.println("regexFull" + m.group());
				
				Pattern n = Pattern.compile("\\d{1}");
				Matcher mn = n.matcher(msg);
				
				if(mn.find()){
					System.out.println("regexLevel " + mn.group());
					
					int v = Integer.parseInt(mn.group());
					cmd.ringLevel(CommandsHandler.this, v);
					
					finish();
				}
			}
		}
		else{
			finish();
		}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVATION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(getApplicationContext(), "ImperialMessage Administration Enabled",
						Toast.LENGTH_SHORT).show();
			} 
			else{
				Toast.makeText(getApplicationContext(), "ERROR:\n" + "During Setting Admin\n" + "Please Retry",
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**********************************************************/	
	public class sendLocation extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			Lat = gps.getLatitude();
			Long = gps.getLongitude();

			if(Long != 0 && Lat != 0){
				Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
				List<Address> addresses = null;
				try {
					addresses = gcd.getFromLocation(Lat, Long, 1);
					if (addresses.size() > 0){
						System.out.println(addresses.get(0).getLocality());
						city = addresses.get(0).getLocality();
					}
				} catch (IOException e1) {
					System("ERROR doBack: " + e1);
				}
			}
			
			String Location = "City: " + city + "\n" + "Latitude: " + Lat + " " + "Lonitude: " + Long;
			return Location;
		}
		@Override
		protected void onPostExecute(String Loc){
			super.onPostExecute(Loc);

			if(!Loc.isEmpty()){
				sendSMSMessage(num, Loc);
				
				System("postEXEC: " + num + "\n" + Loc);
			}
		}
	}
			
	protected void sendSMSMessage(String phoneNo, String message) {
	      System("Number: " + phoneNo + " Msg: " + message);

	      try {
	         SmsManager smsManager = SmsManager.getDefault();
	         smsManager.sendTextMessage(phoneNo, null, message, null, null);
	      } 
	      catch (Exception e) {
	         System("ERROR Sending SMS: " + e);
	      }
	}
	public void System(String s){
		System.out.println(s);
	}

}
