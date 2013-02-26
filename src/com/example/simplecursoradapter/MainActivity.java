package com.example.simplecursoradapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String ROW_ID = "row_id"; // Intent extra key
	private CustomAdapter contactAdapter; // adapter for ListView
	private ListView tasks;
	GestureDetector gestureDetector;
	TouchListener OnTouchListener;
	private DataBaseHandler databaseConnector;
	 Animation animation;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tasks = (ListView) findViewById(R.id.lvTasks);
		gestureDetector = new GestureDetector(this, new GestureListener());
		OnTouchListener = new TouchListener();
		Log.i("Main Activity", "1");

		Log.i("Main Activity", "1_2");
		System.out.println("contactlistview" + tasks);
		tasks.setOnTouchListener(OnTouchListener);
		/**tasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("MainActivity", "on item click method");
				Toast.makeText(MainActivity.this, "ITEM CLICKED",
						Toast.LENGTH_SHORT).show();

			}
		});*/

		String[] from = new String[] { "due_date", "name" };
		Log.i("Main Activity", "2");
		int[] to = new int[] { R.id.textView2, R.id.textView1 };
		databaseConnector = new DataBaseHandler(MainActivity.this);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				databaseConnector.open();
				contactAdapter = new CustomAdapter(MainActivity.this,
						databaseConnector.getAllContacts());
				tasks.setAdapter(contactAdapter);
				databaseConnector.close();
			}

		});
		Log.i("Main Activity", "3");

		animation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});

	}

	protected void onResume() {
		super.onResume(); // call super's onResume method

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				databaseConnector.open();
				contactAdapter.changeCursor(databaseConnector.getAllContacts()); // set
																					// the
																					// adapter's
																					// Cursor
			}
		});

		// create new GetContactsTask and execute it
		// new GetContacts(this, contactAdapter).execute((Object[]) null);
		Log.i("MAIN ACTIVITY", "ON RESUME");
	} // end method onResume

	protected void onStop() {
		Cursor cursor = contactAdapter.getCursor(); // get current Cursor

		if (cursor != null)
			cursor.deactivate(); // deactivate it

		contactAdapter.changeCursor(null); // adapted now has no Cursor
		super.onStop();
	} // end method onStop

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		Intent addEvent = new Intent(MainActivity.this, AddEvent.class);
		startActivity(addEvent);
		return super.onOptionsItemSelected(item); // call super's method
	} // end method onOptionsItemSelected

	class TouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent e) {
			if (gestureDetector.onTouchEvent(e)) {
				return true;
			} else {
				return false;
			}
		}

	}

	class GestureListener extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_MAX_OFF_PATH = 100;
		private static final int SWIPE_THRESHOLD_VELOCITY = 25;

		private MotionEvent mLastOnDownEvent = null;

		@Override
		public boolean onDown(MotionEvent e) {
			mLastOnDownEvent = e;
			return super.onDown(e);
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "On Single TAP up ", Toast.LENGTH_SHORT).show();
			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1 == null) {
				e1 = mLastOnDownEvent;
			}
			if (e1 == null || e2 == null) {
				return false;
			}

			float dX = e2.getX() - e1.getX();
			float dY = e1.getY() - e2.getY();

			if (Math.abs(dY) < SWIPE_MAX_OFF_PATH
					&& Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY
					&& Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
				if (dX > 0) {
					int position = tasks.pointToPosition((int) e1.getX(),
							(int) e1.getY());
						
					int _id = (int) tasks.getItemIdAtPosition(position);
					databaseConnector.deleteContact(_id);
					
					new DeleteRow(_id, contactAdapter, getApplicationContext());
				contactAdapter.notifyDataSetChanged();
					

					Toast.makeText(getApplicationContext(),
							"Right Swipe" + _id, Toast.LENGTH_SHORT).show();
				} else {

					Toast.makeText(getApplicationContext(), "Left Swipe",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}

			return false;
		}

		private void deleteContact(int row_id) {
			DataBaseHandler databaseConnector = new DataBaseHandler(
					MainActivity.this);

		}
	}

}
