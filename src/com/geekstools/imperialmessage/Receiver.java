package com.geekstools.imperialmessage;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver{
	
	final SmsManager sms = SmsManager.getDefault();
	//GetCommandNormal inSMS;

	String senderNum;
	String message;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final Bundle bundle = intent.getExtras();
		
		try {
			
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				
				for (int i = 0; i < pdusObj.length; i++) {
					
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();
					
			        senderNum = phoneNumber;
			        message = currentMessage.getDisplayMessageBody();

			        AccessCheck.getSmsDetails(senderNum, message);
			        System.out.println("*SmsReceiver*\n" + "senderNum: " + senderNum + " message: " + message);

					Toast.makeText(context, "Num: "+ senderNum + "\n" + "Msg: " + message, Toast.LENGTH_LONG).show();	
				}
              }
		}
		catch (Exception e) {
			System.out.println("SmsReceiver: " + "Exception smsReceiver:\n" +e);	
		}
		finally{
			Intent i = new Intent();
			i.setClassName("com.geekstools.imperialmessage", "com.geekstools.imperialmessage.AccessCheck");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
}