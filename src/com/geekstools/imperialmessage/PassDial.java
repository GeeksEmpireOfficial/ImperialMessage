package com.geekstools.imperialmessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PassDial extends Dialog {
	
	  public Activity Act;
	
	  EditText et;
	  Button ok;
	  Button cancel;
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.passcheck);

	    et = (EditText)findViewById(R.id.editText1);
	    ok = (Button)findViewById(R.id.ok);
	    cancel = (Button)findViewById(R.id.cancel);
	    
	    ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String p = et.getText().toString();
				File pass = new File("//data//data//com.geekstools.imperialmessage//files//.pass");
				
				if(pass.exists()){
					String Pass = Read(pass, Act);
					if(p.equals(Pass)){
						Intent i = new Intent(Act, GUI.class);
						Act.startActivity(i);
						Toast.makeText(Act, "ACCESS PERMITTED", Toast.LENGTH_SHORT).show();
					
						PassDial.this.cancel();
					}
					else{
						Toast.makeText(Act, "ACCESS DENIED", Toast.LENGTH_SHORT).show();
						
						PassDial.this.cancel();
						Act.finish();
					}
				}
			}
		});
	    cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PassDial.this.cancel();
				Act.finish();
			}
		});

	  }

	  public PassDial(Activity A) {
	    super(A);

	    this.Act = A;
	  }
	  
	  /****************************/
	  
	  public class SetPassDial extends Dialog {
			
		  public Activity Act;
		
		  EditText first;
		  EditText confirm;
		  Button ok;
		  Button cancel;
		  
		  @Override
		  protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    setContentView(R.layout.set_pass);

		    first = (EditText)findViewById(R.id.editText1);
		    confirm = (EditText)findViewById(R.id.editText2);
		    ok = (Button)findViewById(R.id.ok);
		    cancel = (Button)findViewById(R.id.cancel);
		    
		    ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String filePass = ".pass";
					
					String firstP = first.getText().toString();
					String confirmP = confirm.getText().toString();
					
					if(firstP.length() < 5 && confirmP.length() < 5){
						Toast.makeText(Act, "At Least 5 Characters", Toast.LENGTH_LONG).show();
						
						return;
					}
					
					if(firstP.equals(confirmP)){
						Save(confirmP, filePass, Act);
						
						SetPassDial.this.cancel();
					}
					else{
						Toast.makeText(Act, "NOT MATCHED", Toast.LENGTH_LONG).show();
					}
				}
			});
		    cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SetPassDial.this.cancel();
				}
			});

		  }

		  public SetPassDial(Activity A) {
		    super(A);

		    this.Act = A;
		  }
	}
	  
	/*********************/
	  public class RemoteSetPassDial extends Dialog {
			
		  public Activity Act;
		
		  EditText first;
		  EditText confirm;
		  Button ok;
		  Button cancel;
		  
		  @Override
		  protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    setContentView(R.layout.remote_pass);

		    first = (EditText)findViewById(R.id.editText1);
		    confirm = (EditText)findViewById(R.id.editText2);
		    ok = (Button)findViewById(R.id.ok);
		    cancel = (Button)findViewById(R.id.cancel);
		    
		    ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String filePassRemote = ".remotepass";
					String firstP = first.getText().toString();
					String confirmP = confirm.getText().toString();
					
					if(firstP.equals(confirmP)){
						if(confirmP.length() == 3 && firstP.length() == 3){
							Save(confirmP, filePassRemote, Act);
							
							RemoteSetPassDial.this.cancel();
						}
						else{
							Toast("3 Number Required", Act);
						}
					}
					else{
						Toast("NOT MATCHED", Act);
					}
				}
			});
		    cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					RemoteSetPassDial.this.cancel();
				}
			});

		  }

		  public RemoteSetPassDial(Activity A) {
		    super(A);

		    this.Act = A;
		  }
	}
	  
	  private void Save(String toS, String file, Activity act){
			try{
				FileOutputStream fout = act.openFileOutput(file, act.MODE_PRIVATE);
				fout.write(toS.getBytes());
				
				fout.flush();
				fout.close();
				
				System("Access Granted to: " + toS);
				Toast.makeText(act, "PASSWORD SET SUCCESSFULLY", Toast.LENGTH_SHORT).show();
			}
			catch(Exception e){
				System("ERROR: Saving Process\n" + e);
			}
		}
	  private String Read(File f, Activity act){
		  String content = "";
		  
		  try{
			  FileInputStream fin = new FileInputStream(f);
			  BufferedReader br = new BufferedReader(new InputStreamReader(fin, "UTF-8"), 1024);
			  
			  int c;
			  String temp = "";
			  while( (c = br.read()) != -1){
				  temp = temp + Character.toString((char)c);
			  }
			  content = temp;
		  }
		  catch(Exception e){
			  Toast("ERROR OCCURED\n" + e, act);
		  }
		  
		  return content;
	  }
	  public void System(String s){
		  System.out.println(s);
	  }
	  public void Toast(String s, Activity Act){
		  Toast.makeText(Act, s, Toast.LENGTH_SHORT).show();
	  }
}