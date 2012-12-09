package com.anudeep.eventmanager.activity;

import com.anudeep.eventmanager.dao.EventDao;
import com.anudeep.eventmanager.dao.TicketDao;
import com.anudeep.eventmanager.model.Event;
import com.anudeep.eventmanager.util.CryptoHelper;
import com.anudeep.eventmanager.util.QRCodeHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EventOptions extends ListActivity {

	private static final String[] options = new String[]{"Edit Event","Generate Tickets",
		"List Tickets","Scan Barcode"}; // TODO later send tickets (send by mail or save to phone)
	private Long mEventId;
	private static final int ACTION_SCAN = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		mEventId = extras != null ? extras.getLong(EventDao.KEY_EVENT_ID)
				: null;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, options);
		setListAdapter(adapter);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		if (resultCode == RESULT_OK && requestCode == ACTION_SCAN ){	
			String ticketBarcode = intent.getStringExtra("SCAN_RESULT");
			boolean updated = false;
			if(ticketBarcode!=null){
				TicketDao ticketDao = new TicketDao(this);
				ticketDao.open();
				updated = ticketDao.updateTicketOfEventAndBarcode(mEventId, ticketBarcode);
				ticketDao.close();
			}
			Toast.makeText( this, updated? "SCAN OK!":"SCAN FAILED!", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = null;
		switch(position){
		case 0: 
			i = new Intent(this, EditEvent.class);
			i.putExtra(EventDao.KEY_EVENT_ID, mEventId);
			startActivity(i);
			break;
		case 1:
			i = new Intent(this, GenerateTickets.class);
			i.putExtra(EventDao.KEY_EVENT_ID, mEventId);
			startActivity(i);
			break;
		case 2: 
			i = new Intent(this, ListTicketsActivity.class);
			i.putExtra(EventDao.KEY_EVENT_ID, mEventId);
			startActivity(i);
			break;
		case 3: 
			i = new Intent("com.google.zxing.client.android.SCAN");
			i.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(i,ACTION_SCAN);
			break;
		}
	}
}
