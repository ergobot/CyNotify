package edu.simpson.obryan.projects;

import java.util.ArrayList;
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

	boolean NotifyEnabled, NotifySms, NotifyCall;
	int frequency; // = 1 * 10000; // milliseconds - time in now is for testing
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

		ContentManager contentManager = new ContentManager(this.context);
		ArrayList<PhoneCall> phoneCalls = new ArrayList<PhoneCall>();
		ArrayList<SmsMessage> smsMessages = new ArrayList<SmsMessage>();
		
		NotificationMaker notificationMaker = new NotificationMaker(context);

		

		if (NotifySms) {
			smsMessages = contentManager.GetMissedSms(history);
		} // end of if NotifySms

		// start of content query in call logs

		if (NotifyCall) {
			phoneCalls = contentManager.GetMissedCalls(history);
		}
		// End of if NotifyCall
		
		notificationMaker.NotifyOfSms(smsMessages);
		notificationMaker.NotifyOfCalls(phoneCalls);
		
		
		
		

	}// End of Handle method

}
