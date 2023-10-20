package electron.ring;

import java.util.Calendar;

import electron.SchoolRing;
import electron.data.DirOptions;
import electron.data.database;
import electron.utils.logger;

public class ringThread extends Thread{
	private String lastplay = "";
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				TimeChecker();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void TimeChecker() throws InterruptedException {
		if(database.timesType1.contains(getHour()+":"+getMinute())) {
			if(lastplay.equals(getHour()+":"+getMinute())) {return;}
			lastplay=getHour()+":"+getMinute();
			logger.log("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" found.");
			player.selectMusic(DirOptions.getFilesDir(SchoolRing.musicDir1));
			return;
		}
		if(database.timesType2.contains(getHour()+":"+getMinute())) {
			if(lastplay.equals(getHour()+":"+getMinute())) {return;}
			lastplay=getHour()+":"+getMinute();
			logger.log("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" found.");
			player.selectMusic(DirOptions.getFilesDir(SchoolRing.musicDir2));
			return;
		}
		logger.debug("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" not exists.");
	}

	private static String getMinute() {
		Calendar c = Calendar.getInstance();
		String minute = String.valueOf(c.get(Calendar.MINUTE));
		if(String.valueOf(minute).length()==1) {minute="0"+minute;}
		return minute;
	}
	private static int getHour() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
}
