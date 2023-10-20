package electron.ring;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.File;
import java.util.List;

import electron.utils.logger;

public class player {
	/**
	 * Method,that selects music to play
	 * @param toplay - music list to play
	 * @throws InterruptedException
	 */
	public static void selectMusic(List<File> toplay) throws InterruptedException {
		int exeptions=0;
		while(true) {
		if(play(toplay.get(random(toplay.size())))) {
			break;
		}
		if(exeptions>=5) {
			JOptionPane.showMessageDialog(new JFrame(), "ERROR: tried to play 5 music files.\n All have unsupported format or bitrate.", "[AUDIO_SYSTEM]", JOptionPane.ERROR_MESSAGE);
			break;
		}
		exeptions++;
		}
	}
	/**
	 * Play music function
	 * @param filename - file to play
	 * @return state - true:played or false:not
	 */
	private static boolean play(File filename){
		logger.log("[AUDIO_PLAYER]: Playing: "+filename.getName());
	    try{
	        Clip clip = AudioSystem.getClip();
	        clip.open(AudioSystem.getAudioInputStream(filename));
	        clip.start();
	        Thread.sleep(clip.getMicrosecondLength()/1000);
	        logger.log("[AUDIO_PLAYER]: Clip played.");
	        clip.stop();
	        clip.close(); 
	        return true;
	    }
	    catch (Exception exc)
	    {
	       JOptionPane.showMessageDialog(new JFrame(), "[AUDIO_PLAYER]: ", "Error playing "+filename+": "+exc.getMessage()+"\nMore:\n"+exc.toString(),0);
	       return false;
	    }
	}
	/**
	 * Generate random number method
	 * @param max - max number to generate from 0
	 * @return random number from 0 to max
	 */
	private static int random(int max) {
		java.util.Random random = new java.util.Random();
		int i = random.nextInt(max);
		return i;
	}
}
