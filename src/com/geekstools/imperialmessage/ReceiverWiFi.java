package com.geekstools.imperialmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.geekstools.imperialmessage.ReceiverConfigBoot.getRoot;

public class ReceiverWiFi extends BroadcastReceiver {

	String access;
	Context c;
	PowerManager PM;
	PowerManager.WakeLock WL;
	Commands cmd;
	
	@Override
	public void onReceive(Context context, Intent i) {
		c = context;
		cmd = new Commands();
						
		final WifiManager wManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE );
		if(!wManager.isWifiEnabled()){
			Toast.makeText(context, "WiFi DISABLED", Toast.LENGTH_SHORT).show();
		
			//	
		}
		
		if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(i.getAction())){
			Toast.makeText(context, "AirPlane Mod Changed", Toast.LENGTH_SHORT).show();
			
			//
		}
	}
}