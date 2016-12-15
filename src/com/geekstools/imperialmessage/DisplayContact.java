package com.geekstools.imperialmessage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends Activity {

   int from_Where_I_Am_Coming = 0;
   private DBHelper iDB;
   private TextView name ;
   private TextView number;
   private TextView email;
   private TextView address;

   private EditText ename ;
   private EditText enumber;
   private EditText eemail;
   private EditText eaddress;
   
   private Button g;
   
   int id_To_Update = 0;
   
   String[] data = new String[1000];
   String[] dataNew;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.display_access);
      
      name = (TextView) findViewById(R.id.tvname);
      number = (TextView) findViewById(R.id.tvnumber);
      email = (TextView) findViewById(R.id.tvemail);
      address = (TextView) findViewById(R.id.tvaddress);
      
      ename = (EditText) findViewById(R.id.name);
      enumber = (EditText) findViewById(R.id.number);
      eemail = (EditText) findViewById(R.id.email);
      eaddress = (EditText) findViewById(R.id.address);
      
      View d = (View)findViewById(R.id.display);
      d.setOnLongClickListener(new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			Bundle extras = getIntent().getExtras(); 
		      if(extras != null){
		    	  int Value = extras.getInt("id");
		          int view = extras.getInt("view");
		    	  if(Value > 0){
		    		  Cursor rs = iDB.getData(Value);
		              id_To_Update = Value;
		              rs.moveToFirst();
		              String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
		              String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
		              String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
		              String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
		              
		         	 name.setVisibility(View.GONE);
		         	 number.setVisibility(View.GONE);
		         	 email.setVisibility(View.GONE);
		         	 address.setVisibility(View.GONE);
		         	 
		         	 ename.setVisibility(View.VISIBLE);
		         	 enumber.setVisibility(View.VISIBLE);
		         	 eemail.setVisibility(View.VISIBLE);
		         	 eaddress.setVisibility(View.VISIBLE);
		         	 g.setVisibility(View.VISIBLE);
		         	 
		         	 if (!rs.isClosed()) { rs.close(); }
		         	 
		         	 ename.setText((CharSequence)nam);
		          	 enumber.setText((CharSequence)phon);
		          	 eemail.setText((CharSequence)emai);
		          	 eaddress.setText((CharSequence)plac);
		    	  }
		      }
			return true;
		}
	});
      
      g = (Button)findViewById(R.id.grant);
      g.setVisibility(View.GONE);
      g.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {		
			if(enumber.getText().toString().length() > 0){
				if(enumber.getText().toString().equals(number.getText().toString())){
					//
					System.out.println("Phone Number Not Changed: " +  number.getText().toString());
					RUN();
				}
				else{
					try{
						FileInputStream fin = openFileInput(".grantAccess");
						DataInputStream myDIS = new DataInputStream(fin);
						OutputStreamWriter fOut = new OutputStreamWriter(openFileOutput(".grantAccess.tmp", MODE_PRIVATE|MODE_APPEND));
			         
						String tmp = "";
					
						while( (tmp = myDIS.readLine()) != null){
				         
							//WRITE to NEW
							if(!tmp.trim().equals(number.getText().toString())){
				        	 
								fOut.write(tmp);
								fOut.write("\n");
				        	 			        	 
								System.out.println(tmp);
							}
						}
						fOut.write(enumber.getText().toString());
						System.out.println("New Phone-Num: " + enumber.getText().toString());
						
						fOut.close();
						myDIS.close();
						fin.close();
					
						//Delete File & Rename
						File tmpD = new File("//data//data//com.geekstools.imperialmessage//files//.grantAccess.tmp");
						File New = new File("//data//data//com.geekstools.imperialmessage//files//.grantAccess");
		        	 
						if(tmpD.isFile()){System.out.println("File");}
							deleteFile(".geTranslate");
							tmpD.renameTo(New);
					}
					catch(Exception e){
						System.out.println("ERROR Reading Password: " + e);
					}
					finally{
						RUN();
					}
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
			}
		}
	});
      
      
      iDB = new DBHelper(this);

      Bundle extras = getIntent().getExtras(); 
      if(extras != null){
         int Value = extras.getInt("id");
         int view = extras.getInt("view");
         if(Value > 0 && view == 666){
            Cursor rs = iDB.getData(Value);
            id_To_Update = Value;
            rs.moveToFirst();
            String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
            String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
            String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
            String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
            
            if (!rs.isClosed()) { rs.close(); }
            
            	name.setText((CharSequence)nam);
            	number.setText((CharSequence)phon);
            	email.setText((CharSequence)emai);
            	address.setText((CharSequence)plac);
           }
         else if(Value > 0 && view == 777){
        	 Cursor rs = iDB.getData(Value);
             id_To_Update = Value;
             rs.moveToFirst();
             String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
             String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
             String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
             String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
             
        	 name.setVisibility(View.GONE);
        	 number.setVisibility(View.GONE);
        	 email.setVisibility(View.GONE);
        	 address.setVisibility(View.GONE);
        	 
        	 ename.setVisibility(View.VISIBLE);
        	 enumber.setVisibility(View.VISIBLE);
        	 eemail.setVisibility(View.VISIBLE);
        	 eaddress.setVisibility(View.VISIBLE);
        	 g.setVisibility(View.VISIBLE);
        	 
        	 if (!rs.isClosed()) { rs.close(); }
        	 
        	 ename.setText((CharSequence)nam);
         	 enumber.setText((CharSequence)phon);
         	 eemail.setText((CharSequence)emai);
         	 eaddress.setText((CharSequence)plac);
         }
      }
      else{
    	  finish();
      }
}

   	public void RUN(){
   		Bundle extras = getIntent().getExtras();
      
   		if(extras !=null){
   			int Value = extras.getInt("id");
   			int View = extras.getInt("view");
         
   			if(Value > 0 && View == 777){
   				if(iDB.updateContact(id_To_Update, ename.getText().toString(), enumber.getText().toString(), eemail.getText().toString(), eaddress.getText().toString())){
   					Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
   					
   				 Cursor rs = iDB.getData(Value);
   	             id_To_Update = Value;
   	             rs.moveToFirst();
   	             String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
   	             String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
   	             String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
   	             String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
   					
   				 name.setVisibility(TextView.VISIBLE);
   	        	 number.setVisibility(TextView.VISIBLE);
   	        	 email.setVisibility(TextView.VISIBLE);
   	        	 address.setVisibility(TextView.VISIBLE);
   	        	 
   	        	 if (!rs.isClosed()) { rs.close(); }
   	            
             	name.setText((CharSequence)nam);
             	number.setText((CharSequence)phon);
             	email.setText((CharSequence)emai);
             	address.setText((CharSequence)plac);
   	        	 
   	        	 ename.setVisibility(EditText.GONE);
   	        	 enumber.setVisibility(EditText.GONE);
   	        	 eemail.setVisibility(EditText.GONE);
   	        	 eaddress.setVisibility(EditText.GONE);
   	        	 g.setVisibility(Button.GONE);
   				}		
   				else{
   					Toast.makeText(getApplicationContext(), "ERROR: During Update Process", Toast.LENGTH_SHORT).show();	
   				}
   			}
   			else if(Value > 0){
   				if(iDB.updateContact(id_To_Update, ename.getText().toString(), enumber.getText().toString(), eemail.getText().toString(), eaddress.getText().toString())){
   				Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
					
  				 Cursor rs = iDB.getData(Value);
  	             id_To_Update = Value;
  	             rs.moveToFirst();
  	             String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
  	             String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
  	             String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
  	             String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
  					
  				 name.setVisibility(TextView.VISIBLE);
  	        	 number.setVisibility(TextView.VISIBLE);
  	        	 email.setVisibility(TextView.VISIBLE);
  	        	 address.setVisibility(TextView.VISIBLE);
  	        	 
  	        	 if (!rs.isClosed()) { rs.close(); }
  	            
            	name.setText((CharSequence)nam);
            	number.setText((CharSequence)phon);
            	email.setText((CharSequence)emai);
            	address.setText((CharSequence)plac);
  	        	 
  	        	 ename.setVisibility(EditText.GONE);
  	        	 enumber.setVisibility(EditText.GONE);
  	        	 eemail.setVisibility(EditText.GONE);
  	        	 eaddress.setVisibility(EditText.GONE);
  	        	 g.setVisibility(Button.GONE);
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

   	private void Save(String toS, String file){
		File f = new File("//data//data//com.geekstools.imperialmessage//files//.grantAccess");
		toS = toS + "\n";
		
		try{
			FileOutputStream fout = openFileOutput(file, MODE_APPEND|MODE_PRIVATE);
			fout.write(toS.getBytes());
			
			fout.flush();
			fout.close();
			
			System.out.println("Access Granted to: " + toS);
		}
		catch(Exception e){
			System.out.println("ERROR: Saving Process\n" + e);
		}
	}
}
