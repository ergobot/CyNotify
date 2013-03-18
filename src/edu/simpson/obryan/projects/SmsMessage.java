package edu.simpson.obryan.projects;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsMessage {

	private long dateReceived;
	private String _address;
	private String _message;
	private String _msg_id;
	private boolean isRead;
	private String _contactName;
	
	public void IsRead(String readStatus){
		if(readStatus.equals("0")){
			isRead = false;
		}
		else{
			isRead = true;
		}
	}
	
	public boolean IsRead(){
		return isRead;
	}
	public void DateReceived(long _date) {
		dateReceived = _date;
	}

	public long DateReceived() {
		return dateReceived;
	}

	public double GetHoursSinceMessage() {
		// Get the now
		Date now = new Date();
		// Find difference of dates in milliseconds
		long difference = now.getTime() - this.DateReceived();// this.CallDate().getTime();
		// Get the difference in hours
		double hours = difference / 1000 / 60 / 60;

		return hours;
	}

	public void Address(String address) {
		_address = address;
	}

	public String Address() {
		return _address;
	}

	public void Message(String message) {
		_message = message;
	}

	public String Message() {
		return _message;
	}

	public void MessageId(String messageId) {
		_msg_id = messageId;
	}

	public String MessageId() {
		return _msg_id;
	}

	public String ContactName() {
		return _contactName;
	}

	public void ContactName(String contactName) {
		_contactName = contactName;
	}
	
	public String QueryContactName(Context context, String phoneNumber) {
		if (phoneNumber.equalsIgnoreCase("-1")) {
			return "Unknown";
		} else {

			Uri uri;
			String[] projection;

			uri = Uri.parse("content://contacts/phones/filter");
			projection = new String[] { "name" };

			uri = Uri.withAppendedPath(uri, Uri.encode(this.Address()));
			Cursor nameCursor = context.getContentResolver().query(uri,
					projection, null, null, null);

			String contactName = "";

			if (nameCursor.moveToFirst()) {
				contactName = nameCursor.getString(0);
			}

			nameCursor.close();
			nameCursor = null;

			if (contactName != null && !contactName.isEmpty()) {
				return contactName;
			} else {
				return phoneNumber;
			}
			
		}
	}
	
}
