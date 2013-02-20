package edu.simpson.obryan.projects;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class StartupService extends IntentService {

	public static final String PREFS_NAME = "ServiceDemo2Prefs";

	Context context;
	SharedPreferences settings;

	boolean NotifyEnabled, NotifySms, NotifyCall;
	int frequency;// = 1 * 10000;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		settings = context.getSharedPreferences(PREFS_NAME, 0);
	}

	public StartupService() {
		super("StartupService");
		// TODO Auto-generated constructor stub
	
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(),
				//"StartupService " + NotifyEnabled, Toast.LENGTH_SHORT).show();

		
		NotifyEnabled = settings.getBoolean("NotifyEnabled", false);
		frequency = settings.getInt("frequency", 60000);

		if (NotifyEnabled) {
			 	AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			    Intent startupIntent = new Intent(this, AlarmReceiver.class);
			    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, startupIntent, 0);
			    Calendar time = Calendar.getInstance();
			    time.setTimeInMillis(System.currentTimeMillis());
			    time.add(Calendar.MILLISECOND, frequency);
			    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, time.MILLISECOND, frequency, pendingIntent);
		}
	}

}
