package com.example.simplecursoradapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler {
	// database name
	private static final String DATABASE_NAME = "UserContacts";
	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	// public constructor for DatabaseConnector
	public DataBaseHandler(Context context) {
		// create a new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME,
				null, 7);
	} // end DatabaseConnector constructor
		// open the database connection

	public void open() throws SQLException {
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	} // end method open

	public void close() {
		if (database != null)
			database.close(); // close the database connection
	} // end method close

	public void insertContact(String name, String duedate, int priority) {
		open();
		ContentValues newContact = new ContentValues();
		newContact.put("name", name);

		newContact.put("due_date", duedate);
		newContact.put("priority", priority);

		database.insert("contacts", null, newContact);
		close(); // close the database
	} // end method insertContact

	public void updateContact(long id, String name, String duedate,
			int priority) {
		ContentValues editContact = new ContentValues();
		editContact.put("due_date", duedate);
		open(); // open the database
		database.update("contacts", editContact, "_id=" + id, null);

	} // end method updateContact

	public void updatePriority(long id, int priority) {
		ContentValues editContact = new ContentValues();
		editContact.put("priority", priority);
		Log.i("DB HANDLER","UPDATING THE PR TO : "+priority);
		database.update("contacts", editContact, "_id=" + id, null);

	} // end method updateContact

	public Cursor getAllContacts() {
		// open();
		return database.query("contacts", new String[] { "_id", "name",
				"due_date", "priority" }, null, null, null, null,
				"due_date ASC");
	} // end method getAllContacts

	public Cursor getOneContact(long id) {
		return database.query("contacts", null, "_id=" + id, null, null, null,
				null);
	} // end method getOnContact

	public void deleteContact(long id) {
		open(); // open the database
		database.delete("contacts", "_id=" + id, null);

	} // end method deleteContact

	public void deleteAll() {

		database.delete("contacts", null, null);

	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper {
		// public constructor
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		} // end DatabaseOpenHelper constructor

		// creates the contacts table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			// query to create a new table named contacts
			String createQuery = "CREATE TABLE contacts"
					+ "(_id integer primary key autoincrement,"
					+ "name TEXT, due_date TEXT,priority INTEGER );";

			db.execSQL(createQuery); // execute the query
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE contacts");
			onCreate(db);
		} // end method onUpgrade
	} // end class DatabaseOpenHelper
}