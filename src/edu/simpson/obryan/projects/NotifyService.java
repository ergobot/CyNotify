package edu.simpson.obryan.projects;

import java.util.Date;
import java.util.Timer;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.widget.Toast;

public class NotifyService extends IntentService {

	public static final String PREFS_NAME = "ServiceDemo2Prefs";

	Context context;
	SharedPreferences settings;
	private NotificationManager notificationManager;
	private Notification myNotification;

	boolean NotifyEnabled, NotifySms, NotifyCall;
	int frequency; //= 1 * 10000; // milliseconds - time in now is for testing
	int history; // hours

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		settings = context.getSharedPreferences(PREFS_NAME, 0);

		// Restore preferences
		NotifyCall = settings.getBoolean("NotifyCall", false);
		NotifySms = settings.getBoolean("NotifySms", false);
		NotifyEnabled = settings.getBoolean("NotifyEnabled", false);
		frequency = settings.getInt("frequency", 60000);
		history = settings.getInt("history", 24);
	}

	public NotifyService() {
		super("Notifier");

	}

	@Override
	protected void onHandleIntent(Intent intent) {

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Get the unread sms
		boolean notificationExists = false;
		if (NotifySms) {
			Uri uri = Uri.parse("content://sms/inbox");
			// Query the sms content provider
			Cursor c = getContentResolver().query(uri, null, null, null, null);
			// Iterate through cursor and get specific content in cursor
			// columns
			if (c.moveToFirst()) {

				for (int i = 0; i < c.getCount(); i++) {

					String readMsg = c.getString(
							c.getColumnIndexOrThrow("read")).toString();
					if (readMsg.equals("0")) {

						Long time = c.getLong(c.getColumnIndexOrThrow("date"));
						Date now = new Date();
						// Find difference of dates in milliseconds
						long difference = now.getTime() - time;
						// Get the difference in hours
						double hours = difference / 1000 / 60 / 60;

						if (hours <= history) {

							
							Context context = getApplicationContext();
							String address = c.getString(
									c.getColumnIndexOrThrow("address"))
									.toString();
							String message = c.getString(
									c.getColumnIndexOrThrow("body")).toString();
							String msg_id = c.getString(
									c.getColumnIndexOrThrow("thread_id"))
									.toString();
							myNotification = new Notification(
									R.drawable.ic_launcher, "CyNotify - " + address,
									System.currentTimeMillis());
							
							String notificationTitle = address;
							String notificationText = message;
							Intent myIntent = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("content://mms-sms/conversations/"
											+ msg_id));
							PendingIntent pendingIntent = PendingIntent
									.getActivity(NotifyService.this, 0,
											myIntent,
											Intent.FLAG_ACTIVITY_NEW_TASK);

							if (!notificationExists) {
								myNotification.defaults |= Notification.DEFAULT_SOUND;
								notificationExists = true;
							}

							myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
							myNotification.setLatestEventInfo(context,
									notificationTitle, notificationText,
									pendingIntent);
							notificationManager.notify(
									Integer.parseInt(msg_id), myNotification);
						}// End of if(hours <= history)
					}// End of if(msg is unread)
					c.moveToNext();
				} // End of cursor iterations

			}
		} // end of if NotifySms

		// start of content query in call logs

		if (NotifyCall) {
			Cursor managedCursor = getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null, null, null, null);
			int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
			int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
			int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
			int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

			// Used as the notification id for missed phone messages
			int phone_id = 0;

			while (managedCursor.moveToNext()) {
				String callType = managedCursor.getString(type);
				int dircode = Integer.parseInt(callType);
				switch (dircode) {

				case CallLog.Calls.MISSED_TYPE:

					// Get call date as String and Date
					String callDate = managedCursor.getString(date);
					Date callDayTime = new Date(Long.valueOf(callDate));
					// Get the now
					Date now = new Date();
					// Find difference of dates in milliseconds
					long difference = now.getTime() - callDayTime.getTime();
					// Get the difference in hours
					double hours = difference / 1000 / 60 / 60;

					// Only handle calls from the last 48 hours
					if (hours <= history) {

						
						Context context = getApplicationContext();

						String phNumber = managedCursor.getString(number);

						myNotification = new Notification(
								R.drawable.ic_launcher, "CyNotify - " + phNumber,
								System.currentTimeMillis());
						
						String notificationTitle = phNumber;
						String notificationText = callDayTime.toLocaleString();
						Intent myIntent = new Intent(Intent.ACTION_VIEW);
								// Uri.parse("content://call_log/calls")); this works for almost all
								// Uri.parse("tel:" + phNumber));// this goes to the dialer;
						myIntent.setType(CallLog.Calls.CONTENT_TYPE);
						PendingIntent pendingIntent = PendingIntent
								.getActivity(NotifyService.this, 0, myIntent,
										Intent.FLAG_ACTIVITY_NEW_TASK);

						if (!notificationExists) {
							myNotification.defaults |= Notification.DEFAULT_SOUND;
							notificationExists = true;
						} else {

						}
						myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
						myNotification.setLatestEventInfo(context,
								notificationTitle, notificationText,
								pendingIntent);
						notificationManager.notify(phone_id, myNotification);
						phone_id++;
						break;
					}

				}

			}
			managedCursor.close();

		}
		// End of if NotifyCall

	}// End of Handle method

}
