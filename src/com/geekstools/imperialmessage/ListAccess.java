package com.geekstools.imperialmessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

public class ListAccess extends Activity{

	private ArrayList<String> array_list;
	ArrayAdapter<String> adapter;
	
	private ListView accessList;
	DBHelper iDB;
	
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		setContentView(R.layout.list_all_access);
		
		accessList = (ListView)findViewById(R.id.listView1);
		
		iDB = new DBHelper(this);
	    array_list = iDB.getAllCotacts();
	    
	    adapter = 
	    		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_2,  android.R.id.text1, array_list);
	    
	    accessList.setAdapter(adapter);
	    registerForContextMenu(accessList);
	    accessList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int id_To_Search = position + 1;
				Bundle dataBundle = new Bundle();
				dataBundle.putInt("id", id_To_Search);
				dataBundle.putInt("view", 666);
				Intent intent = new Intent(getApplicationContext(), com.geekstools.imperialmessage.DisplayContact.class);
				intent.putExtras(dataBundle);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		array_list = iDB.getAllCotacts();
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_2,  android.R.id.text1, array_list);
	    accessList.setAdapter(adapter);
	}
	
	@Override
	public void onPause(){
		
		super.onPause();
	}
	
	//Context Menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    
	    String itemName = adapter.getItem(info.position);
	    
	    menu.setHeaderTitle(itemName);
	    menu.setHeaderIcon(R.drawable.ic_launcher);
	    
	    String[] menuItems = getResources().getStringArray(R.array.ContextMenu);
	    for (int I = 0; I < menuItems.length; I++) {
	      menu.add(Menu.NONE, I, I, menuItems[I]);
	    }
	}
		
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  
	  int menuItemIndex = item.getItemId();
	  String[] menuItems = getResources().getStringArray(R.array.ContextMenu);
	  String menuItemName = menuItems[menuItemIndex];
	  String itemName = adapter.getItem(info.position);
	  
	  int id_To_Update = info.position + 1;
	  if(menuItemName.equals("Edit")){
			Bundle dataBundle = new Bundle();
			dataBundle.putInt("id", id_To_Update);
			dataBundle.putInt("view", 777);
			Intent intent = new Intent(getApplicationContext(), com.geekstools.imperialmessage.DisplayContact.class);
			intent.putExtras(dataBundle);
			startActivity(intent);
	  }
	  else if(menuItemName.equals("Delete")){
		  iDB.deleteContact(id_To_Update);
		  
		  Intent r = new Intent(ListAccess.this, ListAccess.class);
		  startActivity(r);
		  
		  ListAccess.this.finish();
	  }
		  
	  return true;
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
