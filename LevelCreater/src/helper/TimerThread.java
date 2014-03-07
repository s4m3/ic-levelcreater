package helper;

import java.util.Timer;

public class TimerThread implements Runnable {

	private Timer timer;
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		timer = new Timer();
		timer.scheduleAtFixedRate(new UpdateTimerTask(start), 0, 1000);

	}
	
	public void stopTimer() {
		this.timer.cancel();
	}

}
