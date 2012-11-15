package it.polimi.dei.bosp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class BbqueService extends Service {

	static final String TAG = "BbqueService";

	//TODO: Define as enum
//	public static enum Msg {
//		MSG_ISREGISTERED,
//		MSG_CREATE,
//		MSG_START,
//		MSG_WAIT_COMPLETION,
//		MSG_TERMINATE,
//		MSG_ENABLE,
//		MSG_DISABLE;
//	}
	//******* Available messages to the Service *******
	static final int MSG_ISREGISTERED= 1;
	static final int MSG_CREATE= 2;
	static final int MSG_START= 3;
	static final int MSG_WAIT_COMPLETION= 4;
	static final int MSG_TERMINATE= 5;
	static final int MSG_ENABLE= 6;
	static final int MSG_DISABLE= 7;
	//*************************************************

	private String name, recipe;

	Intent intent = new Intent("it.polimi.dei.bosp.BBQUE_INTENT");

	private long creationTime = 0;

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreated");
		creationTime = System.currentTimeMillis();
		super.onCreate();
		int response;
		response = RTLIBInit("test");
		Log.d(TAG, "Response from RTLIBInit is: "+response);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStarted");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	protected void isRegistered(Messenger dest) {
		Log.d(TAG, "isRegistered?");
		boolean response = EXCisRegistered();
		Message msg = Message.obtain(null, MSG_ISREGISTERED,
										response ? 1 : 0,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	protected void create(Messenger dest, Object obj) {
		String messageString = obj.toString();
        String params[] = messageString.split("#");
        name = params[0];
        recipe = params[1];
        Log.d(TAG, "create, app: "+name+" with recipe "+recipe);
        int response = EXCCreate(name, recipe);
		Message msg = Message.obtain(null, MSG_CREATE,
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	protected void start(Messenger dest) {
		Log.d(TAG, "start");
		int response = EXCStart();
		Message msg = Message.obtain(null, MSG_START,
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/* *
     * Instantiate the target - to be sent to clients - to communicate with
     * this instance of BbqueService.
     */
	final Messenger mMessenger = new Messenger(new BbqueMessageHandler());

	/**
	 * Handler of incoming messages from clients.
	 */

	private class BbqueMessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ISREGISTERED:
				isRegistered(msg.replyTo);
				break;
			case MSG_CREATE:
				create(msg.replyTo, msg.obj);
				break;
			case MSG_START:
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

	/**
	 * Callback methods
	 */

	public int onSetup() {
		Log.d(TAG,"onSetup called");
		intent.putExtra("BBQ_DEBUG", "onSetup called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public int onConfigure(int awm_id) {
		Log.d(TAG,"onConfigure called");
		intent.putExtra("BBQ_DEBUG", "onConfigure called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public int onSuspend() {
		Log.d(TAG,"onSuspend called");
		intent.putExtra("BBQ_DEBUG", "onSuspend called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public int onResume() {
		Log.d(TAG,"onResume called");
		intent.putExtra("BBQ_DEBUG", "onResume called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public int onRun() {
		Log.d(TAG,"onRun called");
		intent.putExtra("BBQ_DEBUG", "onRun called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public int onMonitor() {
		Log.d(TAG,"onMonitor called");
		intent.putExtra("BBQ_DEBUG", "onMonitor called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public int onRelease() {
		Log.d(TAG,"onRelease called");
		intent.putExtra("BBQ_DEBUG", "onRelease called");
		intent.putExtra("INTENT_TIMESTAMP",
				System.currentTimeMillis()-creationTime);
		intent.putExtra("APP_NAME", name);
		sendBroadcast(intent);
		return 0;
	}

	public native int RTLIBInit(String mode);

	public native boolean EXCisRegistered();

	public native int EXCCreate(String name, String recipe);

	public native int EXCStart();

	public native int EXCCycles();

	/*
	 * Load the native library
	 */
	static {
		System.loadLibrary("bbque_rtlib");
	}
}
