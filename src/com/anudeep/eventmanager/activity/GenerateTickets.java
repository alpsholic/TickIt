package com.anudeep.eventmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.anudeep.eventmanager.R;
import com.anudeep.eventmanager.dao.EventDao;
import com.anudeep.eventmanager.dao.TicketDao;
import com.anudeep.eventmanager.model.Event;
import com.anudeep.eventmanager.model.Ticket;
import com.anudeep.eventmanager.util.BarcodeHelper;
import com.anudeep.eventmanager.util.CryptoHelper;
import com.anudeep.eventmanager.util.ITextPDFHelper;
import com.anudeep.eventmanager.util.QRCodeHelper;

public class GenerateTickets extends Activity {

	private Long mEventId;
	private EditText mTicketsQtyText;
	private TicketDao ticketDao;
	private EventDao eventDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ticketDao = new TicketDao(this);
		ticketDao.open();
		eventDao = new EventDao(this);
		eventDao.open();
		
		setContentView(R.layout.generate_tickets);

		mTicketsQtyText = (EditText) findViewById(R.id.ticketsQuantity);

		Bundle extras = getIntent().getExtras();
		mEventId = extras != null ? extras.getLong(EventDao.KEY_EVENT_ID)
				: null;


	}

	// Will be called via the onClick attribute
	public void generateTickets(View view) {
		if(view.getId() == R.id.ticketsGenerate){
			if(mTicketsQtyText.getText()!=null){
				Event event = eventDao.getEvent(mEventId);
				String pdfContent = "Event:"+event.getName()+", Venue:"+event.getVenue();
				String eventCode = event.getCode();
				BarcodeHelper barcodeHelper = new BarcodeHelper();
				CryptoHelper cryptoHelper = new CryptoHelper();
				
				Integer qty = Integer.parseInt(mTicketsQtyText.getText().toString());
				for(int i = 1; i <= qty.intValue() ; i++){
					String ticketBarcode =null;
					try {
						ticketBarcode = cryptoHelper.encryptString(barcodeHelper.getUUId(), mEventId + eventCode);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(this, "No ticket generated", Toast.LENGTH_LONG);
						return;
					}
					if(ticketBarcode!=null){
						Ticket ticket = ticketDao.createTicket(mEventId,ticketBarcode);
						String qrCodeFileName = "Ticket"+ticket.getId();
						Bitmap bm = QRCodeHelper.encode(ticket.getBarcode());
						String absPath = QRCodeHelper.save(bm, qrCodeFileName);
						ITextPDFHelper.generatePDF(qrCodeFileName, pdfContent, absPath);
					}
				}
				Intent i = new Intent(this, ListTicketsActivity.class);
				i.putExtra(EventDao.KEY_EVENT_ID, mEventId);
				startActivity(i);
			}else{
				Toast.makeText(this, "No quantity entered", Toast.LENGTH_LONG);
			}

		}
	}



}
