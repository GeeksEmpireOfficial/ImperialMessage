package com.geekstools.imperialmessage;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class ReceiverConfigBoot extends BroadcastReceiver {

	String access;
	Context c;
	PowerManager PM;
	PowerManager.WakeLock WL;
	Commands cmd;
	
	@Override
	public void onReceive(Context context, Intent i) {
		c = context;
		cmd = new Commands();
		
		final SharedPreferences root = PreferenceManager.getDefaultSharedPreferences(context);
		
		if (Intent.ACTION_BOOT_COMPLETED.equals(i.getAction())){
			if(root.getBoolean("root", false) == true){
				//get root access
				getRoot access = new getRoot();
				access.execute();
			}
			
			Intent r = new Intent();
			r.setClassName("com.geekstools.imperialmessage", "com.geekstools.imperialmessage.StartUpCheckPoint");
			r.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(r);
		}
		
		final LocationManager locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE );
		if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Toast.makeText(context, "GPS DISABLED", Toast.LENGTH_SHORT).show();
		
			//	
		}
	}
	
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
			       // Preform su to get root privledges  
			       p = Runtime.getRuntime().exec("su");   
			         
			       // Attempt to write a file to a root-only   
			       DataOutputStream os = new DataOutputStream(p.getOutputStream());   
			       os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  
			         
			       // Close the terminal  
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
			            	   
			            	   access = "NO ROOT";
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
				Toast.makeText(c, ("ROOT ACCESS: " + v), Toast.LENGTH_SHORT).show();
				System("Root Access On Boot: " + v);
			}
		}
	}
	
	public void System(String s){
		System.out.println(s);
	}

	public void SetAlarm(Context context){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverConfigBoot.class);
        intent.putExtra("CONFIG", Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10800000, pi);
    }
}
