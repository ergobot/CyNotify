package edu.simpson.obryan.projects;

import java.util.Calendar;
import android.app.AlarmManager;
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

public class AlarmCanceler extends IntentService {
	// Context context;
	AlarmManager alarmMgr;
	
	
	public static final String PREFS_NAME = "CyNotifyPrefs";

	Context context;
	SharedPreferences settings;

	int frequency; // = 1 * 10000; // milliseconds - time in now is for testing

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	public AlarmCanceler() {
		super("AlarmCanceler");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Intent mIntent = new Intent(context, AlarmReceiver.class);
		PendingIntent alarmIntent = (PendingIntent.getBroadcast(getBaseContext(), 0, mIntent, PendingIntent.FLAG_NO_CREATE));
		
		
		// Cancel the alarm
		if (alarmIntent != null) {

			// Cancel the alarm
			alarmMgr.cancel(alarmIntent);
			//Toast.makeText(context, "Cancelling the alarm ", Toast.LENGTH_LONG)
				//	.show();
			// Re-arm the alarm
			settings = context.getSharedPreferences(PREFS_NAME, 0);

			// Restore frequency variable from shared preferences
			frequency = settings.getInt("frequency", 60000);
			
			Calendar time = Calendar.getInstance();
			time.setTimeInMillis(System.currentTimeMillis());
			time.add(Calendar.MILLISECOND, frequency);
			alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					time.getTimeInMillis(), frequency, alarmIntent);

		}

	}// End of Handle method

}
