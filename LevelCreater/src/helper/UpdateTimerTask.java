package helper;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;

import main.LevelCreater;

public class UpdateTimerTask extends TimerTask {

	private long startTime;
	private JLabel timeLabel;
	public UpdateTimerTask(long startTime) {
		this.startTime = startTime;
		timeLabel = LevelCreater.getInstance().timerLabel;
	}
	@Override
	public void run() {
		long diff = System.currentTimeMillis() - startTime;
		String duration = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diff),
	            TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
	            TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));

		timeLabel.setText(duration);
	}

}
