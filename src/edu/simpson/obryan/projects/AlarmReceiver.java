package edu.simpson.obryan.projects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		 
		//Toast.makeText(context, "Inside BroadcastReceiver ", Toast.LENGTH_LONG).show();
		Intent notifier = new Intent(context, NotifyService.class);
		context.startService(notifier);
		
	}

}
