package it.polimi.dei.bosp;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BbqueLauncher extends BroadcastReceiver {

	private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equalsIgnoreCase(BOOT_ACTION))
			Log.d("BbqueLauncher", "BOOT_COMPLETED received!");
			try {
				Process process = Runtime
						.getRuntime()
						.exec("/data/bosp/sbin/barbeque -c /data/bosp/bbque/etc/bbque.conf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
