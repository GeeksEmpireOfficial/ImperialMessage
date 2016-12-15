package com.geekstools.imperialmessage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class AccessCheck extends Activity{

	static String phoneNumber;
    static String SMStext;
	public static void getSmsDetails(String phNumber, String SMSBody) {
        phoneNumber = phNumber;
        SMStext = SMSBody;
    }
	
	private String whoAccess;
	
	TextView tvMsg;
	TextView tvNum;
	
	String[] data = new String[1000];
    String[] dataNew;
    
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		setContentView(R.layout.background);
		
		tvMsg = (TextView)findViewById(R.id.msg);
		tvNum = (TextView)findViewById(R.id.num);
		
		tvMsg.setText(SMStext);
		tvNum.setText(phoneNumber);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				//doBackEXEC
				checkAccess access = new checkAccess();
				access.execute();
				
				finish();
			}
		}, 25);
	}
	
	public class checkAccess extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute(){
			File f = new File("//data//data//com.geekstools.imperialmessage//files//.grantAccess");
			
			if(!f.exists()){
				System.out.println("No File");
				
				finish();
				return;
			}
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String line = "";
	        int i = 0;
	        
			try{
				FileInputStream fin = new FileInputStream("//data//data//com.geekstools.imperialmessage//files//.grantAccess");
				DataInputStream myDIS = new DataInputStream(fin);
				
		        while ( (line = myDIS.readLine()) != null) {
		            data[i] = line;
		            System.out.println(data[i]);
		            i++;
		        }
		        dataNew = new String[i];
		        System.arraycopy(data, 0, dataNew, 0, i);
		        data = dataNew;
			}
			catch(Exception e){
				System.out.println("doBackground Read: " +  e);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void res){
			int i = countLine();			
			
			String num = tvNum.getText().toString();
			String msg = tvMsg.getText().toString();
			
			System.out.println("NUM postEXE: " + num);
			System.out.println("MSG postEXE: " + msg);
			
				for(int l = 0; l < i; l++){
					System.out.println("postEXE whoAccess: " + dataNew[l]);
			
					if(!dataNew[l].isEmpty()){
						System.out.println("postEXE whoAccess: " + dataNew[l]);
						
						if(num.contains(dataNew[l])){
							System.out.println("postEXE whoAccess: " + dataNew[l] + " >> postEXE num: " + num);
					
							Intent t = new Intent(AccessCheck.this, CommandsHandler.class);
							t.putExtra("cmd", msg);
							t.putExtra("num", num);
							startActivity(t);
							
							finish();
							break;
						}
					}
				}
		}
	}
	
	public int countLine(){
		int nLines = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader("//data//data//com.geekstools.imperialmessage//files//.grantAccess"));

			while (reader.readLine() != null){
				nLines++;
			}
			System.out.println("Count of Line: " + nLines);
			reader.close();
		}
		catch(Exception e){
			System.out.println("ERROR Line Counting\n" + e);
		}
		
		return nLines;
	}
}
