package edu.simpson.obryan.projects;

import java.util.Calendar;
import java.util.Hashtable;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {

	Switch phoneSwitch, smsSwitch, switchEnabled;
	boolean NotifyCall, NotifySms, NotifyEnabled;

	Context context;
	SharedPreferences settings;

	Spinner frequencySpinner;
	Spinner historySpinner;
	int frequency; // milliseconds
	int history; // hours
	Hashtable<Integer, Integer> freq_table, reverse_freq_table;
	Hashtable<Integer, Integer> reverse_history_table;
	Hashtable<Integer, Integer> history_table;

	public static final String PREFS_NAME = "ServiceDemo2Prefs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this.getApplicationContext();

		setupUi(savedInstanceState);

		setupListeners();

	}

	protected void setupListeners() {

		switchEnabled.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				if (buttonView.isChecked()) {
					NotifyEnabled = true;
				} else {
					NotifyEnabled = false;
				}
				editor.putBoolean("NotifyEnabled", NotifyEnabled);
				editor.commit();

				if (NotifyEnabled) {
					setAlarm();
				} else {
					cancelAlarm();

				}

			}

		});

		phoneSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();

				if (buttonView.isChecked()) {
					NotifyCall = true;
					Toast.makeText(context, "Missed call reminders enabled",
							Toast.LENGTH_SHORT).show();
				} else {
					NotifyCall = false;
					Toast.makeText(context, "Missed call reminders disabled",
							Toast.LENGTH_SHORT).show();
				}

				editor.putBoolean("NotifyCall", NotifyCall);
				editor.commit();

				if (NotifyEnabled) {
					setAlarm();

				}

			}

		});

		smsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();

				if (buttonView.isChecked()) {
					NotifySms = true;
					Toast.makeText(context, "Text message reminders enabled",
							Toast.LENGTH_SHORT).show();
				} else {
					NotifySms = false;
					Toast.makeText(context, "Text message reminders disabled",
							Toast.LENGTH_SHORT).show();
				}

				editor.putBoolean("NotifySms", NotifySms);
				editor.commit();

				if (NotifyEnabled) {
					setAlarm();
				}

			}

		});

		frequencySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						frequency = freq_table.get(pos);

						Toast.makeText(
								context,
								"Frequency changed to: "
										+ (frequency / 1000 / 60) + " Minutes",
								Toast.LENGTH_SHORT).show();

						settings = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("frequency", frequency);

						editor.commit();

						if (NotifyEnabled) {
							setAlarm();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// There is nothing to do

					}

				});

		historySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				history = history_table.get(pos);

				settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("history", history);

				editor.commit();

				Toast.makeText(
						context,
						"Past messages set to "
								+ parent.getItemAtPosition(pos).toString(),
						Toast.LENGTH_SHORT).show();

				if (NotifyEnabled) {
					setAlarm();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// There is nothing to do

			}

		});

	}

	protected void setAlarm() {
		Toast.makeText(context,
				"Notification set for " + (frequency / 1000 / 60) + " Minutes",
				Toast.LENGTH_SHORT).show();
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.MILLISECOND, frequency);
		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				time.getTimeInMillis(), frequency, pendingIntent);
	}

	protected void cancelAlarm() {
		Toast.makeText(context, "Disabled CyNotify", Toast.LENGTH_SHORT).show();
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		alarmMgr.cancel(pendingIntent);
	}

	protected void setupUi(Bundle savedInstanceState) {

		settings = getSharedPreferences(PREFS_NAME, 0);

		NotifyCall = settings.getBoolean("NotifyCall", false);
		NotifySms = settings.getBoolean("NotifySms", false);
		NotifyEnabled = settings.getBoolean("NotifyEnabled", false);
		frequency = settings.getInt("frequency", (1 * 60 * 1000));
		history = settings.getInt("history", 24);

		freq_table = SetupFreqTable();
		reverse_freq_table = SetupReverseFreqTable();
		history_table = SetupHistoryTable();
		reverse_history_table = SetupReverseHistoryTable();

		phoneSwitch = (Switch) findViewById(R.id.callSwitch);
		smsSwitch = (Switch) findViewById(R.id.smsSwitch);
		switchEnabled = (Switch) findViewById(R.id.serviceSwitch);
		frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
		historySpinner = (Spinner) findViewById(R.id.historySpinner);

		// Setup the values on the UI
		phoneSwitch.setChecked(NotifyCall);
		smsSwitch.setChecked(NotifySms);
		switchEnabled.setChecked(NotifyEnabled);

		// Create an ArrayAdapter using the string arrays(frequency_array) and
		// the custom layout (spinneritem)
		ArrayAdapter<CharSequence> freq_adapter = ArrayAdapter
				.createFromResource(this, R.array.frequency_array,
						R.layout.spinneritem);
		// Specify the layout to use when the list of choices appears
		// (customspinner)
		freq_adapter.setDropDownViewResource(R.layout.customspinner);
		// Apply the adapter to the spinner
		frequencySpinner.setAdapter(freq_adapter);
		int freqSpinnerPos = reverse_freq_table.get(frequency);
		frequencySpinner.setSelection(freqSpinnerPos);

		// Create an ArrayAdapter using the string array(history_array) and the
		// custom layout (spinneritem)
		ArrayAdapter<CharSequence> history_adapter = ArrayAdapter
				.createFromResource(this, R.array.history_array,
						R.layout.spinneritem);
		// Specify the layout to use when the list of choices appears
		// (customspinner)
		history_adapter.setDropDownViewResource(R.layout.customspinner);
		// Apply the adapter to the spinner
		historySpinner.setAdapter(history_adapter);
		// make the spinner selected value match the sharedprefs x2
		// lookup
		int historySpinnerPos = reverse_history_table.get(history);
		// set
		historySpinner.setSelection(historySpinnerPos);

	}

	public Hashtable<Integer, Integer> SetupFreqTable() {
		Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>(8);
		table.put(0, (1 * 60 * 1000));
		table.put(1, (5 * 60 * 1000));
		table.put(2, (10 * 60 * 1000));
		table.put(3, (15 * 60 * 1000));
		table.put(4, (30 * 60 * 1000));
		table.put(5, (45 * 60 * 1000));
		table.put(6, (1 * 60 * 60 * 1000));
		table.put(7, (2 * 60 * 60 * 1000));

		return table;
	}

	public Hashtable<Integer, Integer> SetupReverseFreqTable() {
		Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>(8);

		table.put((1 * 60 * 1000), 0);
		table.put((5 * 60 * 1000), 1);
		table.put((10 * 60 * 1000), 2);
		table.put((15 * 60 * 1000), 3);
		table.put((30 * 60 * 1000), 4);
		table.put((45 * 60 * 1000), 5);
		table.put((1 * 60 * 60 * 1000), 6);
		table.put((2 * 60 * 60 * 1000), 7);
		return table;
	}

	public Hashtable<Integer, Integer> SetupReverseHistoryTable() {
		Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>(4);

		table.put(1, 0);
		table.put(12, 1);
		table.put(24, 2);
		table.put(48, 3);
		return table;
	}

	public Hashtable<Integer, Integer> SetupHistoryTable() {
		Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>(4);

		table.put(0, 1);
		table.put(1, 12);
		table.put(2, 24);
		table.put(3, 48);
		return table;
	}

	@Override
	protected void onPause() {
		super.onPause();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("NotifyEnabled", NotifyEnabled);
		editor.putBoolean("NotifyCall", NotifyCall);
		editor.putBoolean("NotifySms", NotifySms);
		editor.putInt("frequency", frequency);
		editor.putInt("history", history);

		// Commit the edits!
		editor.commit();
	}

}
