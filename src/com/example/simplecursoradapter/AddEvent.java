package com.example.simplecursoradapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class AddEvent extends Activity {

	private EditText titleEditText;

	private TextView titleTextView;
	private TextView duedateTextView;
	private Button saveButton;
	private Button dueDateBtn;

	private TextView mDateDisplay;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event); // inflate the UI
		Log.e("ADD EVENT", "CLASS CAST FINE");
		titleEditText = (EditText) findViewById(R.id.etTitle);
		mDateDisplay = (TextView) findViewById(R.id.tvSelectedDueDate);
		titleTextView = (TextView) findViewById(R.id.tvTitle);
		duedateTextView = (TextView) findViewById(R.id.tvDueDate);
		dueDateBtn = (Button) findViewById(R.id.btnDueDate);
		Log.e("ADD EVENT", "CLASS CAST FINE 2");
		saveButton = (Button) findViewById(R.id.button1);
		saveButton.setOnClickListener(saveButtonOnClickListener);
		dueDateBtn.setOnClickListener(selectDueDateOnClickListener);
		Log.e("ADD EVENT", "CLASS CAST FINE 3");
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		Log.i("ADD EVENT", mYear + "");
		Log.i("ADD EVENT", mMonth + "");
		Log.i("ADD EVENT", mDay + "");
		updateDisplay();
	}

	OnClickListener saveButtonOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (titleEditText.getText().length() != 0) {
				AsyncTask<Object, Object, Object> saveContactTask = new AsyncTask<Object, Object, Object>() {
					@Override
					protected Object doInBackground(Object... params) {
						saveContact(); // save contact to the database
						return null;
					} // end method doInBackground

					@Override
					protected void onPostExecute(Object result) {
						finish(); // return to the previous Activity
					} // end method onPostExecute
				}; // end AsyncTask

				// save the contact to the database using a separate thread
				saveContactTask.execute((Object[]) null);
			} else {
				// create a new AlertDialog Builder
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddEvent.this);

				// set dialog title & message, and provide Button to dismiss
				builder.setTitle("Oops");
				builder.setMessage("You forgot to enter the event");
				builder.setPositiveButton("Ok", null);
				builder.show(); // display the Dialog
			} // end else

		}
	};
	OnClickListener selectDueDateOnClickListener = new OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			showDialog(DATE_DIALOG_ID);
		}
	};

	private void saveContact() {
		DataBaseHandler databaseConnector = new DataBaseHandler(AddEvent.this);
		databaseConnector.insertContact(titleEditText.getText().toString(),
				(mDateDisplay.getText().toString()),0);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {

		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private void updateDisplay() {
		/**
		 * String date = mDay + " ";
		 * 
		 * String day = DateFormat.format("EE", new Date(mYear, mMonth, mDay))
		 * .toString();
		 * 
		 * mDateDisplay.setText(new StringBuilder() // Month is 0 based so add 1
		 * .append(mDay).append(" ,").append(mMonth + 1).append(" ")
		 * .append(day));
		 */
		String newDateStr = "";
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");

		String dateStr = new StringBuilder()
				.
				// Month is 0 based so add 1
				append(mDay).append("/").append(mMonth + 1).append("/")
				.append(mYear).toString();
		Date dateObj;
		try {
			dateObj = curFormater.parse(dateStr);
			SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM, EE");
			newDateStr = postFormater.format(dateObj);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDateDisplay.setText(newDateStr);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

}
