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

	/* *
	 * Regarding the following implementation they say that it can be expensive
	 * but we actually
	 */
	public enum Msg {
		ISREGISTERED,
		CREATE,
		START,
		WAIT_COMPLETION,
		TERMINATE,
		ENABLE,
		DISABLE,
		COUNT;
	};

	private String name, recipe;

	Intent intent = new Intent("it.polimi.dei.bosp.BBQUE_INTENT");

	private long creationTime = 0;

	/** Within the onCreate we initialize the RTLib */
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

	/** Binding to the mMessenger, which is a Messenger type object, created
	 *  starting from a "new BbqueMessageHandler()", defined some lines below */
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	/**
	 * Follow the implementation of Activity2RTLib calls. This implementation
	 * won't be overridden
	 */

	protected void isRegistered(Messenger dest) {
		Log.d(TAG, "isRegistered?");
		boolean response = EXCisRegistered();
		Message msg = Message.obtain(null, Msg.ISREGISTERED.ordinal(),
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
		Message msg = Message.obtain(null, Msg.CREATE.ordinal(),
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
		Message msg = Message.obtain(null, Msg.START.ordinal(),
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void waitCompletion(Messenger dest) {
		Log.d(TAG, "wait completion");
		int response = EXCWaitCompletion();
		Message msg = Message.obtain(null, Msg.WAIT_COMPLETION.ordinal(),
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void terminate(Messenger dest) {
		Log.d(TAG, "terminate");
		int response = EXCTerminate();
		Log.d(TAG, "terminate completed, sending message...");
		Message msg = Message.obtain(null, Msg.TERMINATE.ordinal(),
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void enable(Messenger dest) {
		Log.d(TAG, "enable");
		int response = EXCWaitCompletion();
		Message msg = Message.obtain(null, Msg.ENABLE.ordinal(),
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void disable(Messenger dest) {
		Log.d(TAG, "disable");
		int response = EXCWaitCompletion();
		Message msg = Message.obtain(null, Msg.DISABLE.ordinal(),
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiate the target - to be sent to clients - to communicate with
	 * this instance of BbqueService.
	 */
	final Messenger mMessenger = new Messenger(new BbqueMessageHandler());

	/**
	 * Handler of incoming messages from clients.
	 */

	protected class BbqueMessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (Msg.values()[msg.what]) {
			case ISREGISTERED:
				isRegistered(msg.replyTo);
				break;
			case CREATE:
				create(msg.replyTo, msg.obj);
				break;
			case START:
				start(msg.replyTo);
				break;
			case WAIT_COMPLETION:
				break;
			case TERMINATE:
				terminate(msg.replyTo);
				break;
			case ENABLE:
				enable(msg.replyTo);
				break;
			case DISABLE:
				disable(msg.replyTo);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Callback methods, basic implementation.
	 * These methods are meant to be overridden by the CustomService developer.
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

	public native int EXCWaitCompletion();

	public native int EXCTerminate();

	public native int EXCEnable();

	public native int EXCDisable();

	/*
	 * Load the native library
	 */
	static {
		System.loadLibrary("bbque_rtlib");
	}
}
