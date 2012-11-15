package it.polimi.dei.bosp;

import android.util.Log;

public class CustomService extends BbqueService {

	private static int cycle_n = 1;

	public static void setCyclesNumber(int n) {
		cycle_n = n;
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
