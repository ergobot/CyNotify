package edu.simpson.obryan.projects;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

public class ContentManager {

	Context context;

	// ctor with context
	public ContentManager(Context newContext) {
		context = newContext;
	}

	public ArrayList<PhoneCall> GetMissedCalls(double history) {
		ArrayList<PhoneCall> phoneCalls = new ArrayList<PhoneCall>();

		Cursor managedCursor = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

		int isreadcol = managedCursor.getColumnIndex(CallLog.Calls.IS_READ);
		// Used as the notification id for missed phone messages
		int phone_id = 0;

		while (managedCursor.moveToNext()) {
			PhoneCall phoneCall = new PhoneCall();
			String callType = managedCursor.getString(type);
			// int dircode = Integer.parseInt(callType);
			phoneCall.CallType(Integer.parseInt(callType));

			// switch (dircode) {
			switch (phoneCall.CallType()) {

			case CallLog.Calls.MISSED_TYPE:

				phoneCall.IsRead(managedCursor.getString(isreadcol));

				// if user has interacted with missed call notification then
				// isread == 1
				// if user has not interacted then isread == 0
				// if (isread.equals("0")) {
				if (!phoneCall.IsRead()) {

					// Get call date as String and Date
					String callDate = managedCursor.getString(date);
					// Date callDayTime = new Date(Long.valueOf(callDate));
					phoneCall.CallDate(new Date(Long.valueOf(callDate)));

					/*
					 * // Get the now Date now = new Date(); // Find difference
					 * of dates in milliseconds long difference = now.getTime()
					 * - callDayTime.getTime(); // Get the difference in hours
					 * double hours = difference / 1000 / 60 / 60;
					 * 
					 * // Only handle calls from the last 48 hours if (hours <=
					 * history) {
					 */
					if (phoneCall.GetHoursSinceCall() <= history) {

						phoneCall.Number(managedCursor.getString(number));

						phoneCall.setContactName(phoneCall.QueryContactName(
								this.context, phoneCall.Number()));

						phoneCall.Id(phone_id);

						phone_id++;
						phoneCalls.add(phoneCall);

						break;
					}
				}
			}

		}
		managedCursor.close();

		return phoneCalls;
	}

	public ArrayList<SmsMessage> GetMissedSms(double history) {
		ArrayList<SmsMessage> smsMessages = new ArrayList<SmsMessage>();

		Uri uri = Uri.parse("content://sms/inbox");
		// Query the sms content provider
		Cursor managerCursor = context.getContentResolver().query(uri, null,
				null, null, null);
		// Iterate through cursor and get specific content in cursor
		// columns
		if (managerCursor.moveToFirst()) {

			for (int i = 0; i < managerCursor.getCount(); i++) {

				SmsMessage smsMessage = new SmsMessage();

				smsMessage
						.IsRead(managerCursor.getString(
								managerCursor.getColumnIndexOrThrow("read"))
								.toString());

				if (!smsMessage.IsRead()) {

					// Long time = c.getLong(c.getColumnIndexOrThrow("date"));
					smsMessage.DateReceived(managerCursor.getLong(managerCursor
							.getColumnIndexOrThrow("date")));

					if (smsMessage.GetHoursSinceMessage() <= history) {

						smsMessage.Address(managerCursor.getString(
								managerCursor.getColumnIndexOrThrow("address"))
								.toString());

						smsMessage.Message(managerCursor.getString(
								managerCursor.getColumnIndexOrThrow("body"))
								.toString());

						smsMessage.MessageId(managerCursor.getString(
								managerCursor
										.getColumnIndexOrThrow("thread_id"))
								.toString());

						smsMessage.ContactName(smsMessage.QueryContactName(
								this.context, smsMessage.Address()));

						smsMessages.add(smsMessage);

					}// End of if(hours <= history)
				}// End of if(msg is unread)
				managerCursor.moveToNext();
			} // End of cursor iterations

		}

		return smsMessages;
	}

}
