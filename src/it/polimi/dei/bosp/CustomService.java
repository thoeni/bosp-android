package it.polimi.dei.bosp;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class CustomService extends BbqueService {

	/**
	 * So far the following value overlaps with the ones defined into
	 * BbqueService
	 * TODO: figure out how to use enum in this case, to be extended.
	 */
	static final int MSG_CYCLES = 8;

	private static int cycle_n = 1;

	@Override
	public IBinder onBind(Intent intent) {
		return cMessenger.getBinder();
	}

	/**
	 * Instantiate the target - to be sent to clients - to communicate with this
	 * instance of CustomService.
	 */
	final Messenger cMessenger = new Messenger(new CustomMessageHandler());

	/**
	 * Handler of incoming messages from clients. This Handler overloads the
	 * BbqueMessageHandler.
	 */
	class CustomMessageHandler extends BbqueMessageHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CYCLES:
				Log.d(TAG, "Message cycles setting: " + msg.arg1);
				cycle_n = msg.arg1;
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Follow the customized implementations of callback methods "onSomething".
	 */

	@Override
	public int onRun() {
		int cycles = EXCCycles();
		Log.d(TAG, "onRun called, cycle: " + cycles);
		intent.putExtra("BBQ_DEBUG", "onRun called, cycle: " + cycles);
		sendBroadcast(intent);
		try {
			Thread.sleep(1000);
			if (cycles >= cycle_n)
				return 1;
		} catch (InterruptedException e) {

		}
		return 0;
	}

	@Override
	public int onRelease() {
		Log.d(TAG, "onRelease called");
		intent.putExtra("BBQ_DEBUG", "onRelease called");
		intent.putExtra("ON_RELEASE", 1);
		sendBroadcast(intent);
		return 0;
	}

}
