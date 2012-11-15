package it.polimi.dei.bosp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class BBC extends Activity implements Runnable {

	private TextView console;
	private Handler handler;

	/** IntentFilter used to receive broadcast intents launched by service */
    IntentFilter receiverFilter = new IntentFilter ();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc);
        console = (TextView) findViewById(R.id.console);
        console.setMovementMethod(new ScrollingMovementMethod());
		handler = new Handler();
		//TODO: Remove tracing when not needed anymore
		receiverFilter.addAction("it.polimi.dei.bosp.BBQUE_INTENT");
		registerReceiver(receiver, receiverFilter);
    }

    @Override
	public void onResume() {
		super.onResume();
		handler.post(this);
	}

    @Override
	public void onBackPressed() {
		super.onBackPressed();
		unregisterReceiver(receiver);
	}

	/**
	 * Broadcast receiver: catches messages sent by the Tutorial3Service
	 */
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String entry = String.format("[%7.3f - %15s] %s \n",
					((float)(intent.getLongExtra("INTENT_TIMESTAMP",0)))/1000,
					intent.getStringExtra("APP_NAME"),
					intent.getStringExtra("BBQ_DEBUG"));
			console.append(entry);
			final int scrollAmount = console.getLayout().getLineTop(
					console.getLineCount())-console.getHeight()+10;
		    // if there is no need to scroll, scrollAmount will be <=0
		    if(scrollAmount>0)
		        console.scrollTo(0, scrollAmount);
		    else
		        console.scrollTo(0,0);
		}
	};

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
