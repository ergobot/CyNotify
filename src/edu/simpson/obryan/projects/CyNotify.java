package edu.simpson.obryan.projects;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

public class CyNotify extends Application {

	private static PendingIntent archivedIntent;

	private static CyNotify singleton;
	private static int frequency;
	private static Intent intent;

	public static CyNotify getInstance() {
		return singleton;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		archivedIntent = null;
		frequency = 0;
	}

	public int GetArchivedFrequency() {
		return frequency;
	}

	public void SetArchivedFrequency(int archivedFrequency) {
		frequency = archivedFrequency;
	}

	public PendingIntent GetArchivedIntent() {
		return archivedIntent;
	}

	public void RemoveArchivedIntent() {
		archivedIntent = null;
	}

	public void SetArchivedIntent(PendingIntent newArchive) {
		archivedIntent = newArchive;
	}
	public Intent GetIntent(){
		return intent;
	}
	public void SetIntent(Intent newIntent){
		intent = newIntent;
	}

}
