package com.example.simplecursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.CursorAdapter;

public class DeleteRow {
	int row_id;
	CursorAdapter cursor_adpater;
	Context caller;

	public DeleteRow(int id, CursorAdapter adapter, Context activity) {
		caller = activity;
		row_id = id;
		cursor_adpater = adapter;
		new DeleteTask().execute((Object[]) null);
	}

	private class DeleteTask extends AsyncTask<Object, Object, Cursor> {
		DataBaseHandler databaseConnector = new DataBaseHandler(caller);

		// perform the database access
		@Override
		protected Cursor doInBackground(Object... params) {

			// get a cursor containing call contacts
			databaseConnector.deleteContact(row_id);
			return databaseConnector.getAllContacts();
		} // end method doInBackground

		// use the Cursor returned from the doInBackground method
		@Override
		protected void onPostExecute(Cursor result) {
			cursor_adpater.changeCursor(result); // set the adapter's Cursor

			databaseConnector.close();
		} // end method onPostExecute

	} // end class GetContactsTask

}
