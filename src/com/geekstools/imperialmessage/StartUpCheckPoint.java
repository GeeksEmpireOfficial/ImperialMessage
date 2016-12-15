package com.geekstools.imperialmessage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.location.LocationManager;
import android.os.Bundle;
											/**Check Point for GPS, WiFi & Necessary Services**/
public class StartUpCheckPoint extends Activity{

	PassDial dial;
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		
		dial = new PassDial(StartUpCheckPoint.this);
		
		final LocationManager locManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
		
		if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			dial.show();
		}
		
		dial.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				finish();
			}
		});
	}
}
