package it.polimi.dei.bosp;

import android.content.Intent;
import android.os.Handler;
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
	 * this instance of Service
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	/**
	 * Handler of incoming messages from clients.
	 */

	class IncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ISREGISTERED:
				isRegistered(msg.replyTo);
				break;
			case MSG_CREATE:
				create(msg.replyTo);
				break;
			case MSG_START:
				cycle_n = msg.arg1;
				start(msg.replyTo);
				break;
			case MSG_WAIT_COMPLETION:
				break;
			case MSG_TERMINATE:
				break;
			case MSG_ENABLE:
				break;
			case MSG_DISABLE:
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
