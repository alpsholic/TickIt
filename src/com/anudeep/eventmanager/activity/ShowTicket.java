package com.anudeep.eventmanager.activity;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anudeep.eventmanager.R;
import com.anudeep.eventmanager.dao.EventDao;
import com.anudeep.eventmanager.dao.TicketDao;
import com.anudeep.eventmanager.model.Event;
import com.anudeep.eventmanager.util.CryptoHelper;
import com.anudeep.eventmanager.util.QRCodeHelper;

public class ShowTicket extends Activity {

	Long mTicketId = null;
	private static final int SEND_EMAIL_ID = Menu.FIRST;
	//Long mEventId = null;
	//private static final int DECODE_ID = Menu.FIRST+1;
	private File barcodeImageFile = null;
	//private File barcodePDFFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_ticket);
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		Bundle extras = getIntent().getExtras();
		mTicketId = extras != null ? extras.getLong(TicketDao.KEY_TICKET_ID)
				: null;
//		mEventId = extras != null ? extras.getLong(TicketDao.KEY_TICKET_EVENT_ID)
//				: null;
		File folder = new File(Environment.getExternalStorageDirectory(), QRCodeHelper.SDCARD_FOLDER);
		barcodeImageFile = new File(folder,"Ticket"+mTicketId + ".png");
		//barcodePDFFile = new File(folder,"Ticket"+mTicketId+".pdf");
		image.setImageURI(Uri.parse("file://" + barcodeImageFile.getAbsolutePath()));
		image.setScaleType(ImageView.ScaleType.FIT_XY);  

		registerForContextMenu(image);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SEND_EMAIL_ID, 0, R.string.send_email);
		return true;
	};
	
	private void sendEmail(){
		 
		Intent i = new Intent(Intent.ACTION_SEND);
		//i.setType("text/plain");
		Uri uri = Uri.parse("file://" + barcodeImageFile.getAbsolutePath());
		i.setType("image/jpeg");
		//Uri uri = Uri.parse("file://" + barcodePDFFile.getAbsolutePath());
		//i.setType("application/pdf");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"anucool.iiit@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "Your e-ticket for event: ");
		i.putExtra(Intent.EXTRA_TEXT   , "Hi, Here is your e-ticket attached for event");
		i.putExtra(Intent.EXTRA_STREAM, uri);
		
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(ShowTicket.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case SEND_EMAIL_ID:
			sendEmail();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.add(0,DECODE_ID,0,R.string.decode);
//	}
//	
//	private void decode(){
//		String ticketBarcode = QRCodeHelper.decode(barcodeImageFile.getAbsolutePath());
//		
//		boolean updated = false;
//		if(ticketBarcode!=null){
//			TicketDao ticketDao = new TicketDao(this);
//			ticketDao.open();
//			updated = ticketDao.updateTicketOfEventAndBarcode(mEventId, ticketBarcode);
//			ticketDao.close();
//		}
//		Toast.makeText( this, updated? "SCAN OK!":"SCAN FAILED!", Toast.LENGTH_LONG).show();
//		
//		TextView s = (TextView) findViewById(R.id.logticket);
//		s.setText(ticketBarcode);
//	}
//
//	@Override
//	public boolean onContextItemSelected(android.view.MenuItem item) {
//		switch(item.getItemId()) {
//		case DECODE_ID:
//			decode();
//			return true;
//		}
//		return super.onContextItemSelected(item);
//	};
}
