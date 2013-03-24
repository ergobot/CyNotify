package edu.simpson.obryan.projects;

import java.sql.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

public class CustomPhoneStateListener extends PhoneStateListener {
	

	Context context;
	SharedPreferences settings;
	public static final String PREFS_NAME = "CyNotifyPrefs";

	int previousState;

	
    public CustomPhoneStateListener(Context newContext) {
        super();
        context = newContext;
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		previousState = settings.getInt("previousPhoneState", -1);
    }
    
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
    	super.onCallStateChanged(state, incomingNumber);
    	
    	int newPhoneState = state;
    	
    	if (previousState == TelephonyManager.CALL_STATE_IDLE) 
		{
			// call state went from idle to idle
			if (newPhoneState == TelephonyManager.CALL_STATE_IDLE) 
			{
				// going idle to idle isn't really possible but 
				// the next state is important.  Store the
				// new phone state, do nothing.
				previousState = newPhoneState;
			} 
			// call state went from idle to call answered
			else if (newPhoneState == TelephonyManager.CALL_STATE_OFFHOOK) 
			{
				// change the recorded phone state to answered
				previousState = newPhoneState;
			} 
			// call state went from idle to ringing
			else if (newPhoneState == TelephonyManager.CALL_STATE_RINGING) 
			{
				// change the recorded phone state to ringing
				previousState = newPhoneState;
			}
		}
		// previously was off the hizzy, call was answered
		else if (previousState == TelephonyManager.CALL_STATE_OFFHOOK) 
		{
			// call state went from answered to idle
			if (newPhoneState == TelephonyManager.CALL_STATE_IDLE) 
			{
				
				
				// change the phone state to idle
				previousState = newPhoneState;
				
				
			} 
			// call state went from answered to answered
			else if (newPhoneState == TelephonyManager.CALL_STATE_OFFHOOK) 
			{
				// this doesn't matter but.. (it's not possible)
				// change the state from answered to answered
				previousState = newPhoneState;
			} 
			// call state went from answered to ringing
			else if (newPhoneState == TelephonyManager.CALL_STATE_RINGING) 
			{
				// this doesn't matter but.. (it's not possible)
				// change the state from answered to ringing
				previousState = newPhoneState;
			}
		} 
		// previously, the phone was ringing
		else if (previousState == TelephonyManager.CALL_STATE_RINGING) 
		{
			// call state went from ringing to idle
			if (newPhoneState == TelephonyManager.CALL_STATE_IDLE) 
			{
				// change the state from ringing to idle
				// This is the missed call we are looking for
				
				// Fire and forget the intent
				// After all that work... 
				// Reset the timer
				Intent alarmCanceler = new Intent(context, AlarmCanceler.class);
				context.startService(alarmCanceler);
				
				// change state from ringing to idle
				previousState = newPhoneState;
			} 
			// call state went from ringing to answered
			else if (newPhoneState == TelephonyManager.CALL_STATE_OFFHOOK) 
			{
				// change state from ringing to idle
				previousState = newPhoneState;
			} 
			// call state went from ringing to ringing
			else if (newPhoneState == TelephonyManager.CALL_STATE_RINGING) 
			{
				// change state from ringing to ringing
				previousState = newPhoneState;
			}
		}
		
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putInt("previousPhoneState", newPhoneState);
		editor.commit();
		
    }
    // End of method
    
    

	public void NotifyOfMissedCall()
	{
		
		// Prepare intent which is triggered if the
		// notification is selected

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://call_log/calls")); 
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);//.getActivities(this, 0, intent, 0);

		Date date = new Date(System.currentTimeMillis());
		String time = (String) DateFormat.format("hh:mm:ss", date.getTime());
		
		// Build notification
		Notification.Builder builder = new Notification.Builder(context);
		builder.setContentTitle("Missed Call");
		builder.setContentText(time);//"Subject");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentIntent(pIntent);
		Notification notification = builder.getNotification();
		

		NotificationManager notificationManager;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, notification); 
				
		
	}
	
    
    
    
}
