package it.polimi.dei.bosp;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class CustomService extends BbqueService {

	private static int cycle_n = 1;

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	/* *
	 * Instantiate the target - to be sent to clients - to communicate with
	 * this instance of CustomService.
	 * The Messenger has to be created, from a new CustomHandler() in case
	 * the developer wants to customize the messages, or a BbqueMessageHandler
	 * if he's ok with the default calls.
	 */
	final Messenger mMessenger = new Messenger(new CustomHandler());

	/**
	 * Handler of incoming messages from clients.
	 * This Handler overloads the BbqueMessageHandler.
	 */
	class CustomHandler extends BbqueMessageHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START:
				cycle_n = msg.arg1;
				start(msg.replyTo);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	@Override
	public int onRun() {
		int cycles = EXCCycles();
		Log.d(TAG,"onRun called, cycle: "+cycles);
		intent.putExtra("BBQ_DEBUG", "onRun called, cycle: "+cycles);
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
		Log.d(TAG,"onRelease called");
		intent.putExtra("BBQ_DEBUG", "onRelease called");
		intent.putExtra("ON_RELEASE", 1);
		sendBroadcast(intent);
		return 0;
	}

}
