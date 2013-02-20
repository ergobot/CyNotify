package edu.simpson.obryan.projects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StartupReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Toast.makeText(context, "Inside StartupReceiver ", Toast.LENGTH_LONG).show();
		Intent startup = new Intent(context, StartupService.class);
		context.startService(startup);

	}

}
