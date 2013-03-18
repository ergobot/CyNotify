package edu.simpson.obryan.projects;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;

public class NotificationMaker {

	Context context;
	private NotificationManager notificationManager;
	
	public NotificationMaker(Context newContext){
		context = newContext;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public void NotifyOfCalls(ArrayList<PhoneCall> phoneCalls)
	{
		boolean notificationExists = false;	
		Notification myNotification;
		// Send notifications for phoneCalls
				for(int i = 0; i < phoneCalls.size();i++)
				{
					
					myNotification = new Notification(
							R.drawable.ic_launcher, "CyNotify - "
									+ phoneCalls.get(i).getContactName(),
							System.currentTimeMillis());

					
					Intent myIntent = new Intent(Intent.ACTION_VIEW);
					// Uri.parse("content://call_log/calls")); this
					// works for almost all
					// Uri.parse("tel:" + phNumber));// this goes to the
					// dialer;
		//
					myIntent.setType(CallLog.Calls.CONTENT_TYPE);
					PendingIntent pendingIntent = PendingIntent
							.getActivity(context, 0,
									myIntent,
									Intent.FLAG_ACTIVITY_NEW_TASK);
					
					if (!notificationExists) {
						myNotification.defaults |= Notification.DEFAULT_SOUND;
						notificationExists = true;
					} else {

					}
					myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
					myNotification.setLatestEventInfo(context,
							phoneCalls.get(i).getContactName(), phoneCalls.get(i).CallDate().toLocaleString(),
							pendingIntent);
					
					notificationManager
					.notify(phoneCalls.get(i).Id(), myNotification);
				}
		
	}
	
	public void NotifyOfSms(ArrayList<SmsMessage> smsMessages){
		
		boolean notificationExists = false;
				// Send notifications for smsMessage
				for(int i = 0; i < smsMessages.size(); i++)
				{
					
					Notification myNotification = new Notification(
							R.drawable.ic_launcher, "CyNotify - "
									+ smsMessages.get(i).ContactName(),
							System.currentTimeMillis());

					
					Intent myIntent = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("content://mms-sms/conversations/"
									+ smsMessages.get(i).MessageId()));
					PendingIntent pendingIntent = PendingIntent
							.getActivity(context, 0,
									myIntent,
									Intent.FLAG_ACTIVITY_NEW_TASK);
					
					if (!notificationExists) {
						myNotification.defaults |= Notification.DEFAULT_SOUND;
						notificationExists = true;
					}
	
					myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
					myNotification.setLatestEventInfo(context,
							smsMessages.get(i).ContactName(), smsMessages.get(i).Message(),
							pendingIntent);
					notificationManager.notify(
							Integer.parseInt(smsMessages.get(i).MessageId()), myNotification);
					
				}
		
	}
	
}
