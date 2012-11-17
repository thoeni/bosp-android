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

	//******* Available messages to the Service *******
    static final int MSG_ISREGISTERED= 1;
    static final int MSG_CREATE= 2;
    static final int MSG_START= 3;
    static final int MSG_WAIT_COMPLETION= 4;
    static final int MSG_TERMINATE= 5;
    static final int MSG_ENABLE= 6;
    static final int MSG_DISABLE= 7;
    static final int MSG_GET_CH_UID= 8;
    static final int MSG_GET_UID= 9;
    static final int MSG_SET_CPS= 10;
    static final int MSG_SET_CTIME_US= 11;
    static final int MSG_CYCLES= 12;
    static final int MSG_DONE= 13;
    static final int MSG_CURRENT_AWM= 14;
    //*************************************************

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

	protected void waitCompletion(Messenger dest) {
		Log.d(TAG, "wait completion");
		int response = EXCWaitCompletion();
		Message msg = Message.obtain(null, MSG_WAIT_COMPLETION,
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
		Message msg = Message.obtain(null, MSG_TERMINATE,
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
		Message msg = Message.obtain(null, MSG_ENABLE,
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
		Message msg = Message.obtain(null, MSG_DISABLE,
										response,
										0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void getChUid(Messenger dest) {
		Log.d(TAG, "getChUid");
		String response = EXCGetChUid();
		Message msg = Message.obtain(null, MSG_GET_CH_UID,
										response);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void getUid(Messenger dest) {
		Log.d(TAG, "getUid");
		long response = EXCGetUid();
		Message msg = Message.obtain(null, MSG_GET_UID,
										response);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void setCPS(Messenger dest, float cps) {
		Log.d(TAG, "setCPS");
		int response = EXCSetCPS(cps);
		Message msg = Message.obtain(null, MSG_SET_CPS,
										response, 0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void setCTimeUS(Messenger dest, int us) {
		Log.d(TAG, "setCTimeUS");
		int response = EXCSetCTimeUs(us);
		Message msg = Message.obtain(null, MSG_SET_CTIME_US,
										response, 0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void cycles(Messenger dest) {
		Log.d(TAG, "cycles");
		int response = EXCCycles();
		Message msg = Message.obtain(null, MSG_CYCLES,
										response, 0);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void done(Messenger dest) {
		Log.d(TAG, "done");
		boolean response = EXCDone();
		Message msg = Message.obtain(null, MSG_DONE,
										response);
		try {
			dest.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void currentAWM(Messenger dest) {
		Log.d(TAG, "currentAWM");
		byte response = EXCCurrentAWM();
		Message msg = Message.obtain(null, MSG_CURRENT_AWM,
										response, 0);
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
				waitCompletion(msg.replyTo);
				break;
			case MSG_TERMINATE:
				terminate(msg.replyTo);
				break;
			case MSG_ENABLE:
				enable(msg.replyTo);
				break;
			case MSG_DISABLE:
				disable(msg.replyTo);
				break;
			case MSG_GET_CH_UID:
				getChUid(msg.replyTo);
				break;
			case MSG_GET_UID:
				getUid(msg.replyTo);
				break;
			case MSG_SET_CPS:
				setCPS(msg.replyTo, Float.valueOf(msg.obj.toString()));
				break;
			case MSG_SET_CTIME_US:
				setCTimeUS(msg.replyTo, msg.arg1);
				break;
			case MSG_CYCLES:
				cycles(msg.replyTo);
				break;
			case MSG_DONE:
				done(msg.replyTo);
				break;
			case MSG_CURRENT_AWM:
				currentAWM(msg.replyTo);
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

	public native String EXCGetChUid();

	public native long EXCGetUid();

	public native int EXCSetCPS(float cps);

	public native int EXCSetCTimeUs(int us);

	public native boolean EXCDone();

	public native byte EXCCurrentAWM();

	/*
	 * Load the native library
	 */
	static {
		System.loadLibrary("bbque_rtlib");
	}
}
