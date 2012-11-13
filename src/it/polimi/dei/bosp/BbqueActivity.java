/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.polimi.dei.bosp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BbqueActivity extends Activity implements Runnable,
		View.OnClickListener {

	private Handler handler;
	private TextView output;
	private static final String TAG = "BbqueActivity";

	/** Flag indicating whether we have called bind on the service. */
    boolean mBound;

    /** Messenger for communicating with the service. */
    Messenger mService;

    /** IntentFilter used to receive broadcast intents launched by service */
    IntentFilter receiverFilter = new IntentFilter ();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main);
		output = (TextView) findViewById(R.id.output);
		handler = new Handler();
		//TODO: Remove tracing when not needed anymore
		Debug.startMethodTracing();
		receiverFilter.addAction("it.polimi.dei.bosp.BBQUE_INTENT");
		registerReceiver(receiver, receiverFilter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Binding to the service. This automatically starts the Service
        bindService(new Intent(this, BbqueService.class), mConnection,
            Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onResume() {
		super.onResume();
		handler.post(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Unbinding from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		super.onPause();
		Debug.stopMethodTracing();
	}

	/* *
	 * Buttons interacts with the Service through Messenger paradigm.
	 */

	public void btnRegistered(View v) {
		Log.d(TAG, "isRegistered button pressed...");
		if (!mBound) return;
		Message msg = Message.obtain(null, BbqueService.MSG_ISREGISTERED, 0, 0);
		try {
			msg.replyTo = mMessenger;
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void btnCreate(View v) {
		Log.d(TAG, "Create button pressed...");
		if (!mBound) return;
		Message msg = Message.obtain(null, BbqueService.MSG_CREATE, 0, 0);
		try {
			msg.replyTo = mMessenger;
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void btnStart(View v) {
		Log.d(TAG, "Start button pressed...");
		if (!mBound) return;
		Message msg = Message.obtain(null, BbqueService.MSG_START, 0, 0);
		try {
			msg.replyTo = mMessenger;
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Broadcast receiver: catches messages sent by the Tutorial3Service
	 */
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String bbqDebugIntent = intent.getStringExtra("BBQ_DEBUG");
			output.setText(bbqDebugIntent);
		}
	};

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
			case BbqueService.MSG_ISREGISTERED:
				Log.d(TAG, "isRegistered?: "+msg.arg1);
				output.setText("isRegistered?: "+msg.arg1);
				break;
			case BbqueService.MSG_CREATE:
				Log.d(TAG, "Create: "+msg.arg1);
				output.setText("Create: "+msg.arg1);
			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private final ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,
				IBinder service) {
			mService = new Messenger(service);
			mBound = true;
		}
		@Override
		public void onServiceDisconnected(ComponentName className) {
			mService = null;
			mBound = false;
		}
	};
}