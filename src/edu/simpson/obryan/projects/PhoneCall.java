package edu.simpson.obryan.projects;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class PhoneCall {

	private int id;
	private String number;
	private int type;
	private Date callDate;
	private int duration;
	private int callType;
	private String isRead;
	private String contactName;

	public PhoneCall() {

	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String _contactName) {
		contactName = _contactName;
	}

	public String QueryContactName(Context context, String phoneNumber) {
		if (phoneNumber.equalsIgnoreCase("-1")) {
			return "Unknown";
		} else {

			Uri uri;
			String[] projection;

			uri = Uri.parse("content://contacts/phones/filter");
			projection = new String[] { "name" };

			uri = Uri.withAppendedPath(uri, Uri.encode(this.Number()));
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

	public int Id() {
		return id;
	}

	public void Id(int _id) {
		id = _id;
	}

	public String Number() {
		return number;
	}

	public void Number(String _number) {
		number = _number;
	}

	public int CallType() {
		return type;
	}

	public void CallType(int callType) {
		type = callType;
	}

	public Date CallDate() {
		return callDate;
	}

	public void CallDate(Date _callDate) {
		callDate = _callDate;
	}

	public int Duration() {
		return duration;
	}

	public void Duration(int _duration) {
		duration = _duration;
	}

	public boolean IsRead() {
		if (isRead.equals("0"))
			return false;
		else
			return true;
	}

	public void IsRead(String _isRead) {
		isRead = _isRead;
	}

	public double GetHoursSinceCall() {
		// Get the now
		Date now = new Date();
		// Find difference of dates in milliseconds
		long difference = now.getTime() - this.CallDate().getTime();
		// Get the difference in hours
		double hours = difference / 1000 / 60 / 60;

		return hours;
	}

}
