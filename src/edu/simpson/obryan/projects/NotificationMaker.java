package edu.simpson.obryan.projects;

import java.sql.Date;
import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.text.format.DateFormat;

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
					.notify(phoneCalls.hashCode(), myNotification);
//					.notify(phoneCalls.get(i).Id(), myNotification);
				}
		
	}
	
	public void NotifyOfSms(ArrayList<SmsThread> smsThreads){
		
		boolean notificationExists = false;
		
				NotificationManager notificationManager;
				notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				
				// Send notifications for smsMessage
				for(int i = 0; i < smsThreads.size(); i++)
				{
					
					// Prepare intent which is triggered if the
					// notification is selected
					
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("content://mms-sms/conversations/"+ smsThreads.get(i).MessageId())); 
					PendingIntent pIntent = PendingIntent.getActivity(context, 0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);

					Date date = new Date(System.currentTimeMillis());
					String time = (String) DateFormat.format("hh:mm:ss", date.getTime());
					
					// Build notification
					Notification.Builder builder = new Notification.Builder(context);
					builder.setTicker("CyNotify - " + smsThreads.get(i).ContactName());
					builder.setContentTitle(smsThreads.get(i).ContactName());
					builder.setContentText(smsThreads.get(i).Message());
					builder.setSmallIcon(R.drawable.ic_launcher);
					builder.setContentIntent(pIntent);
					Notification notification = builder.getNotification();

					if (!notificationExists) {
						notification.defaults |= Notification.DEFAULT_SOUND;
						notificationExists = true;
					}
					
					// Hide the notification after its selected
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notificationManager.notify(Integer.parseInt(smsThreads.get(i).MessageId()), notification); 
					//notificationManager.notify(i, notification); 
				}
		
	}

	
	
	
	//
	
}
