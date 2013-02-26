package com.example.simplecursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends CursorAdapter {

	Context mContext;
	private DataBaseHandler databaseConnector;
	Cursor cursor;

	public CustomAdapter(Context activity, Cursor allContacts) {
		// TODO Auto-generated constructor stub
		super(activity, allContacts);
		mContext = activity;
		cursor = allContacts;
	}

	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		// when the view will be created for first time,

		// we need to tell the adapters, how each item will look

		LayoutInflater inflater = LayoutInflater.from(parent.getContext());

		View retView = inflater.inflate(R.layout.row_item, parent, false);

		return retView;

	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {
		// here we are setting our data
		// that means, take the data from the cursor and put it in views

		TextView textViewTaskTitle = (TextView) view
				.findViewById(R.id.textView1);

		textViewTaskTitle.setText(cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(1))));
		Typeface typeFace_tasktitle = Typeface.createFromAsset(
				context.getAssets(), "fonts/Roboto-LightItalic.ttf");
		textViewTaskTitle.setTypeface(typeFace_tasktitle);

		TextView textViewTaskDate = (TextView) view
				.findViewById(R.id.textView2);
		textViewTaskDate.setText(cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(2))));

		TextView textViewDateSectionHeader = (TextView) view
				.findViewById(R.id.textView3);
		textViewDateSectionHeader.setTypeface(typeFace_tasktitle);
		String current_Date = cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(2)));
		String previous_Date = null;

		if (cursor.getPosition() > 0 && cursor.moveToPrevious()) {
			previous_Date = cursor.getString(cursor.getColumnIndex(cursor
					.getColumnName(2)));
			cursor.moveToNext();
		}
		if (previous_Date == null || !previous_Date.equals(current_Date)) {
			textViewDateSectionHeader.setVisibility(View.VISIBLE);
			
			textViewDateSectionHeader.setText(current_Date);
		} else {
			textViewDateSectionHeader.setVisibility(View.GONE);

		}

		final ImageButton imgBtn_priority = (ImageButton) view
				.findViewById(R.id.imgBtnPriority);
		Log.i("CUSTOM ADAPTER : ",
				""
						+ "- VALUE OF PRIORITY: "
						+ cursor.getInt(cursor.getColumnIndex(cursor
								.getColumnName(3))));

		final int priority = cursor.getInt(cursor.getColumnIndex(cursor
				.getColumnName(3)));

		if (cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))) == 0) {
			Log.i("CUSTOM ADAPTER : ",
					"CHECKING IF PRIORITY IS 0 AND YES IT IS 0");
			imgBtn_priority.setImageResource(R.drawable.rect_not_imp);
		} else if (cursor
				.getInt(cursor.getColumnIndex(cursor.getColumnName(3))) == 1) {
			Log.i("CUSTOM ADAPTER : ",
					"CHECKING IF PRIORITY IS 1 AND YES IT IS 1");
			imgBtn_priority.setImageResource(R.drawable.rect_imp);
		}
		final long row_id = cursor.getLong(cursor.getColumnIndex(cursor
				.getColumnName(0)));
		imgBtn_priority.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				databaseConnector = new DataBaseHandler(mContext);

				new Handler().post(new Runnable() {

					@Override
					public void run() {
						databaseConnector.open();
						Cursor crs = databaseConnector.getOneContact(row_id);
						crs.moveToFirst();

						Log.i("CUSTOM ADAPTER:  ",
								"IN RUN METHOD, THE CURRENT PR IS :"
										+ crs.getInt(crs.getColumnIndex(crs
												.getColumnName(3))));
						/**Toast.makeText(
								mContext,
								"CURRENT PR IS "
										+ crs.getInt(crs.getColumnIndex(crs
												.getColumnName(3))),
								Toast.LENGTH_SHORT).show();*/
						if (crs.getInt(crs.getColumnIndex(crs.getColumnName(3))) == 0) {

							databaseConnector.updatePriority(row_id, 1);

							// Toast.makeText(mContext, "Changing PR: 0 To 1",
							// Toast.LENGTH_SHORT).show();
							Log.i("CUSTOM ADAPTER :   ",
									"IN RUN METHOD: SETTING PR TO 1 ");
							imgBtn_priority
									.setImageResource(R.drawable.rect_imp);
						} else if (crs.getInt(crs.getColumnIndex(crs
								.getColumnName(3))) == 1) {
							databaseConnector.updatePriority(row_id, 0);
							Log.i("CUSTOM ADAPTER :  ",
									"IN RUN METHOD: SETTING PR TO 0 ");
							// Toast.makeText(mContext, "Changing PR: 1 To 0",
							// Toast.LENGTH_SHORT).show();
							imgBtn_priority
									.setImageResource(R.drawable.rect_not_imp);
						}

						databaseConnector.close();
					}

				});

			}
		});
	}
}
