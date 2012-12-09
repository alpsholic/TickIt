package com.anudeep.eventmanager.activity;

import com.anudeep.eventmanager.R;
import com.anudeep.eventmanager.dao.EventDao;
import com.anudeep.eventmanager.dao.TicketDao;
import com.anudeep.eventmanager.model.Event;
import com.anudeep.eventmanager.model.Ticket;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListTicketsActivity extends ListActivity {
	
	private static final int DELETE_ID = Menu.FIRST;
	private TicketDao ticketDao;
	private Long mEventId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tickets_list);
		ticketDao = new TicketDao(this);
		ticketDao.open();
		
		Bundle extras = getIntent().getExtras();
		mEventId = extras != null ? extras.getLong(EventDao.KEY_EVENT_ID)
				: null;
		
		fillData();
		registerForContextMenu(getListView());

	}
	
	private void fillData(){
		 Cursor cursor = ticketDao.fetchTickets(mEventId);
		 startManagingCursor(cursor);
		String[] from = new String[]{TicketDao.KEY_TICKET_ID, TicketDao.KEY_TICKET_STATE };
		int[] to = new int[]{R.id.ticketIdText,R.id.stateText};
	    SimpleCursorAdapter adapter = 
	           new SimpleCursorAdapter(this, R.layout.tickets_row, cursor, from, to);
	 
//		ArrayAdapter<Ticket> adapter = new ArrayAdapter<Ticket>(this,
//				android.R.layout.simple_list_item_1, ticketDao.getTickets(mEventId));
		setListAdapter(adapter);

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0,DELETE_ID,0,R.string.menu_delete_ticket);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		switch(item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			ticketDao.deleteTicket(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);

	};
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ShowTicket.class);
		i.putExtra(TicketDao.KEY_TICKET_ID, id);
		i.putExtra(TicketDao.KEY_TICKET_EVENT_ID, mEventId);
		startActivity(i);
	}
}
