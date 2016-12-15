package com.geekstools.imperialmessage;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ReceiverAdmin extends DeviceAdminReceiver {
	
	static final String TAG = "DeviceAdminReceiver";

	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		Toast.makeText(context, "Enabled",
				Toast.LENGTH_LONG).show();
		Log.d(TAG, "onEnabled");
	}

	@Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
		
		
        return context.getString(R.string.app_name);
    }

	
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		Toast.makeText(context, "Disabled",
				Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDisabled");
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		super.onPasswordChanged(context, intent);
		Toast.makeText(context, "onPasswordChanged",
				Toast.LENGTH_LONG).show();
		Log.d(TAG, "onPasswordChanged");
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		super.onPasswordFailed(context, intent);
		Toast.makeText(context, "onPasswordFailed",
				Toast.LENGTH_LONG).show();
		Log.d(TAG, "onPasswordFailed");
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		super.onPasswordSucceeded(context, intent);
		Toast.makeText(context, "onPasswordSucceeded",
				Toast.LENGTH_LONG).show();
		Log.d(TAG, "onPasswordSucceeded");
	}

}
