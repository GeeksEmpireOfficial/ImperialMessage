package com.geekstools.imperialmessage;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "imperial.db";
   public static final String CONTACTS_TABLE_NAME = "contacts";
   public static final String CONTACTS_COLUMN_ID = "id";
   public static final String CONTACTS_COLUMN_NAME = "name";
   public static final String CONTACTS_COLUMN_EMAIL = "email";
   public static final String CONTACTS_COLUMN_CITY = "address";
   public static final String CONTACTS_COLUMN_PHONE = "number";

  
   public DBHelper(Context context){
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table contacts " +
    		  								"(id integer primary key, name text, number text, email text, address text)"
      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS contacts");
      onCreate(db);
   }

   public boolean insertContact  (String name, String phone, String email, String place){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();

      contentValues.put("name", name);
      contentValues.put("number", phone);
      contentValues.put("email", email);	
      contentValues.put("address", place);

      db.insert("contacts", null, contentValues);
      return true;
   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from contacts where id=" + id + "", null );
      return res;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
      return numRows;
   }
   public boolean updateContact (Integer id, String name, String phone, String email, String place){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      
      contentValues.put("name", name);
      contentValues.put("number", phone);
      contentValues.put("email", email);;
      contentValues.put("address", place);
      
      db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      
      return true;
   }

   public Integer deleteContact (Integer id){
      SQLiteDatabase db = this.getWritableDatabase();
      
      return db.delete("contacts", "id = ? ",  new String[] { Integer.toString(id) });
   }
   
   public ArrayList<String> getAllCotacts(){
      ArrayList<String> array_list = new ArrayList<String>();
      
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from contacts", null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
    	  array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
    	  res.moveToNext();
      }
      
      return array_list;
   }
}