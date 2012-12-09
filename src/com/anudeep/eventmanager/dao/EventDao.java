package com.anudeep.eventmanager.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anudeep.eventmanager.database.SQLiteDbHelper;
import com.anudeep.eventmanager.model.Event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EventDao {

	public static final String KEY_EVENT_ID = "_id";
	public static final String KEY_EVENT_CODE = "code";
	public static final String KEY_EVENT_NAME = "name";
	public static final String KEY_EVENT_VENUE = "venue";

	private SQLiteDatabase database;
	private SQLiteDbHelper dbHelper;
	private String[] allColumns = { "_id","code","name","venue" };

	public EventDao(Context context) {
		dbHelper = new SQLiteDbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	//	private String toEncryptedCode(String code){
	//		try {
	//			byte[] encryptedCode = CryptoHelper.getInstance().encrypt(code);
	//			return CryptoHelper.getInstance().bytesToHex(encryptedCode);
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}

	public Event createEvent(String code,String name,String venue) {
		ContentValues values = new ContentValues();
		values.put("code", code);
		values.put("name", name);
		values.put("venue", venue);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		values.put("start_time",dateFormat.format(date));
		long insertId = database.insert("event", null,values);
		return getEvent(insertId);
	}

	public boolean deleteEvent(long id) {
		System.out.println("Deleting event with id: " + id);
		return database.delete("event", "_id = " + id, null) > 0;
	}

	public Event getEvent(long id) {
		System.out.println("Selecting event id: " + id);
		// To show how to query
		Cursor cursor = database.query("event", allColumns, "_id = " + id, null,null, null, null);
		Event event = null;
		if(cursor.moveToFirst()){
			event =  cursorToEvent(cursor);
		}
		return event;
	}
	public Cursor fetchEvent(long id) {
		System.out.println("Selecting event id: " + id);
		// To show how to query
		Cursor cursor = database.query("event", allColumns, "_id = " + id, null,null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void updateEvent(Event event) {
		long id = event.getId();
		ContentValues values = new ContentValues();
		if(event.getCode()!=null){
			values.put("code",event.getCode());
		}
		values.put("name",event.getName());
		values.put("venue",event.getVenue());
		System.out.println("Event id: "+id+"updated");
		database.update("event", values, "_id = "+id, null);
	}

	public List<Event> getAllEvents() {
		List<Event> events = new ArrayList<Event>();
		Cursor cursor = database.query("event", allColumns, null, null, null, null, null);
		if (cursor.moveToFirst()){
			do{
				Event event = cursorToEvent(cursor);
				events.add(event);
			}while (cursor.moveToNext());
		}
		return events;
	}
	public Cursor fetchAllEvents() {
		System.out.println("Selecting all events");
		return database.query("event", allColumns, null, null, null, null, null);
	}

	private Event cursorToEvent(Cursor cursor) {
		Event event = new Event();
		event.setId(cursor.getLong(0));
		event.setCode(cursor.getString(1));
		event.setName(cursor.getString(2));
		event.setVenue(cursor.getString(3));
		return event;
	}
}
