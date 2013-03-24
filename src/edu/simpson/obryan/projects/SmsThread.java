package edu.simpson.obryan.projects;

import java.util.Date;
import java.util.*;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsThread {
	
	private long dateReceived;
	private String _address;
	private String _message;
	private String _msg_id;
	private boolean isRead;
	private String _contactName;
	
	private ArrayList<SmsMessage> _smsThread;
	private int _count;
	
	public SmsThread()
	{
		_smsThread = new ArrayList<SmsMessage>();
		_count = 0;
	}
	
	public int Count(){
		return _count;
	}
	public void Count(int count)
	{
		_count += count;
	}
	
	public void Add(SmsMessage smsMessage)
	{
		// Collection is empty
		if(this.Count() == 0)
		{
			// Add the message;
			_smsThread.add(smsMessage);
			// increment the total count
			Count(1);
			
			this.DateReceived(smsMessage.DateReceived());
			this.Address(smsMessage.Address());
			this.Message(smsMessage.Message());
			this.MessageId(smsMessage.MessageId());
			this.IsRead(smsMessage.IsRead());
			this.ContactName(smsMessage.ContactName());
			
		}
		else
		{
			// Add the message
			_smsThread.add(smsMessage);
			// increment the total count
			Count(1);
			
			this.Message(String.valueOf(this.Count()) + " Unread Messages");
			
		}
		
	}
	
	public void IsRead(boolean readStatus){
		readStatus = isRead;
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
