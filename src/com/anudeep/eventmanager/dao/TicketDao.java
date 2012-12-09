package com.anudeep.eventmanager.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anudeep.eventmanager.database.SQLiteDbHelper;
import com.anudeep.eventmanager.model.Ticket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TicketDao {
	
	public static final String KEY_TICKET_ID = "_id";
	public static final String KEY_TICKET_EVENT_ID = "event_id";
	public static final String KEY_TICKET_STATE = "state";
	public static final String KEY_TICKET_BARCODE = "barcode";
	
	private SQLiteDatabase database;
	private SQLiteDbHelper dbHelper;
	private String[] allColumns = { KEY_TICKET_ID,KEY_TICKET_EVENT_ID,KEY_TICKET_STATE,KEY_TICKET_BARCODE};

	public TicketDao(Context context) {
		dbHelper = new SQLiteDbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Ticket createTicket(long eventId,String barcode) {
		ContentValues values = new ContentValues();
		values.put(KEY_TICKET_EVENT_ID, eventId);
		values.put(KEY_TICKET_STATE, "GENERATED");
		values.put(KEY_TICKET_BARCODE, barcode);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		values.put("creation_time",dateFormat.format(date));

		long insertId = database.insert("ticket", null,values);
		return getTicket(insertId);
	}

	public boolean deleteTicket(long id) {
		System.out.println("Deleting ticket with id: " + id);
		return database.delete("ticket", KEY_TICKET_ID +" = "+ id, null) > 0;
	}
	
	public Ticket getTicket(long id) {
		return cursorToTicket(fetchTicket(id));
	}
	public Cursor fetchTicket(long id) {
		System.out.println("Selecting ticket id: " + id);
		Cursor cursor = database.query("ticket", allColumns,KEY_TICKET_ID +" = " + id, null,null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateTicketOfEventAndBarcode(Long eventId,String ticketBarcode){
		ContentValues values = new ContentValues();
		values.put(KEY_TICKET_STATE,"SCANNED");
		
		String whereClause = KEY_TICKET_EVENT_ID +" = ? AND "+ KEY_TICKET_BARCODE+" = ? AND " + KEY_TICKET_STATE + " = ?";
		String whereArgs[] = {eventId.toString(),ticketBarcode.toString(), "GENERATED"};
		int rowsUpdated = database.update("ticket", values, whereClause, whereArgs);
		
		return rowsUpdated == 1 ;
	}
	
	public void updateTicket(Ticket ticket) {
		long id = ticket.getId();
		ContentValues values = new ContentValues();
		values.put(KEY_TICKET_STATE,ticket.getState());
		values.put(KEY_TICKET_BARCODE,ticket.getBarcode());
		values.put(KEY_TICKET_EVENT_ID,ticket.getEventId());
		database.update("ticket", values, KEY_TICKET_ID +" = "+id, null);
		System.out.println("ticket id: "+id+" updated");
	}

	public List<Ticket> getTickets(long eventId) {
		List<Ticket> tickets = new ArrayList<Ticket>();
		Cursor cursor = database.query("ticket", allColumns, KEY_TICKET_EVENT_ID +" = "+eventId, null, null, null, null);
		while (cursor.moveToNext()) {
			Ticket ticket = cursorToTicket(cursor);
			tickets.add(ticket);
		}
		cursor.close();
		return tickets;
	}
	public Cursor fetchTickets(long eventId) {
		System.out.println("Selecting all tickets");
        return database.query("ticket", allColumns, KEY_TICKET_EVENT_ID +" = "+eventId, null, null, null, null);
	}

	private Ticket cursorToTicket(Cursor cursor) {
		Ticket ticket = new Ticket();
		ticket.setId(cursor.getLong(0));
		ticket.setEventId(cursor.getLong(1));
		ticket.setState(cursor.getString(2));
		ticket.setBarcode(cursor.getString(3));
		return ticket;
	}
}
