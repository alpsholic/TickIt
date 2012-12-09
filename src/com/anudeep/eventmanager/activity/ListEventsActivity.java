package com.anudeep.eventmanager.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.anudeep.eventmanager.R;
import com.anudeep.eventmanager.dao.EventDao;
import com.anudeep.eventmanager.model.Event;

public class ListEventsActivity extends ListActivity {

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST+1;
	 private EventDao eventDao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_list);
		eventDao = new EventDao(this);
		eventDao.open();
		fillData();
		registerForContextMenu(getListView());

	}

	private void fillData(){
		Cursor eventsCursor = eventDao.fetchAllEvents();
		startManagingCursor(eventsCursor);
		String[] from = new String[]{EventDao.KEY_EVENT_NAME,EventDao.KEY_EVENT_VENUE};
		int[] to = new int[]{R.id.eventText,R.id.venueText};
		SimpleCursorAdapter adapter = 
			new SimpleCursorAdapter(this, R.layout.events_row, eventsCursor, from, to);
		
//		ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this,
//				android.R.layout.simple_list_item_1, eventDao.getAllEvents());
		setListAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert_event);
		return true;
	};

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case INSERT_ID:
			createEvent();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0,DELETE_ID,0,R.string.menu_delete_event);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		switch(item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			eventDao.deleteEvent(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);

	};

	private void createEvent(){
		Intent i = new Intent(this, EditEvent.class);
		startActivity(i);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, EventOptions.class);
		i.putExtra(EventDao.KEY_EVENT_ID, id);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

}
