package com.example.simplecursoradapter;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class GetContacts extends AsyncTask<Object, Object, Cursor> {
	Context ctx;
	CustomAdapter contactAdapter;
   public GetContacts(){
	   
   }
	public GetContacts(Context ctx, CustomAdapter adptr) {
		this.ctx = ctx;
		contactAdapter = adptr;
		
	}

	DataBaseHandler databaseConnector = new DataBaseHandler(ctx);

	// perform the database access
	@Override
	protected Cursor doInBackground(Object... params) {

		databaseConnector.open();
		// get a cursor containing call contacts

		return databaseConnector.getAllContacts();
	} // end method doInBackground

	// use the Cursor returned from the doInBackground method
	@Override
	protected void onPostExecute(Cursor result) {
		contactAdapter.changeCursor(result); // set the adapter's Cursor
		Log.i("Main Activiy", "Before Closing it in Main Activity");

		// databaseConnector.close();
	} // end method onPostExecute

}