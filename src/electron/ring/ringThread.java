package electron.ring;

import java.util.Calendar;

import electron.SchoolRing;
import electron.data.DirOptions;
import electron.data.database;
import electron.utils.logger;

public class ringThread extends Thread{
	private String lastplay = "";
	/**
	 * Checker thread method
	 */
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
	/**
	 * Method, that checks time from database and current
	 * @throws InterruptedException
	 */
	private void TimeChecker() throws InterruptedException {
		if(database.timesType1.contains(getHour()+":"+getMinute())) {
			if(lastplay.equals(getHour()+":"+getMinute())) {return;}
			lastplay=getHour()+":"+getMinute();
			logger.log("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" found. Type - 1");
			player.selectMusic(DirOptions.getFilesDir(SchoolRing.musicDir1));
			return;
		}
		if(database.timesType2.contains(getHour()+":"+getMinute())) {
			if(lastplay.equals(getHour()+":"+getMinute())) {return;}
			lastplay=getHour()+":"+getMinute();
			logger.log("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" found. Type - 2");
			player.selectMusic(DirOptions.getFilesDir(SchoolRing.musicDir2));
			return;
		}
		if(database.timesType3.contains(getHour()+":"+getMinute())) {
			if(lastplay.equals(getHour()+":"+getMinute())) {return;}
			lastplay=getHour()+":"+getMinute();
			logger.log("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" found. Type - 3");
			player.selectMusic(DirOptions.getFilesDir(SchoolRing.musicDir3));
			return;
		}
		
		logger.debug("[RING_SYSTEM]: time "+getHour()+":"+getMinute()+" not exists.");
	}
	/**
	 * @return current minute
	 */
	private static String getMinute() {
		Calendar c = Calendar.getInstance();
		String minute = String.valueOf(c.get(Calendar.MINUTE));
		if(String.valueOf(minute).length()==1) {minute="0"+minute;}
		return minute;
	}
	/**
	 * @return current hour
	 */
	private static int getHour() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
}
