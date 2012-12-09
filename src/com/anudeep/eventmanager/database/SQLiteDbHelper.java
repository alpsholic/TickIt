package com.anudeep.eventmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "eventmanager.db";
	private static final int DATABASE_VERSION = 2;
	
	// Database creation sql statement
	private static final String CREATE_EVENT = "create table event ( " +
		" _id integer primary key autoincrement, " +
		" code text not null," +
		" name text not null," +
		" venue text not null," +
		" start_time text  );"; // TODO unique code, barcode
	
	private static final String CREATE_TICKET = "create table ticket ( " +
		" _id integer primary key autoincrement, " +
		" event_id integer not null," +
		" state text not null," +
		" barcode text not null," +
		" creation_time text,"+
		" FOREIGN KEY (event_id) REFERENCES event(_id) );"; // TODO default initial ticket state
	

	//private static String CREATE_DATABASE = "";

	public SQLiteDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_EVENT);
		database.execSQL(CREATE_TICKET);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteDbHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS event;");
		db.execSQL("DROP TABLE IF EXISTS ticket;");
		onCreate(db);
	}

}
