package electron.data;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import electron.utils.logger;

public class DirOptions {
	//Поскольку этот класс переезжает из проекта в проект это нужно
	private static void log(String msg) {logger.log(msg);}
	private static void logerr(String msg) {logger.error(msg);}
	
	public static void loadDir(File file) {
		if(!file.exists()) {
			if(!file.mkdir()) {
				logerr("[DIR_OPTIONS]: error creating directory.");
				System.exit(1);
			}
			log("[DIR_OPTIONS]: directory created and loaded.");
		}else {
			log("[DIR_OPTIONS]: directory loaded.");
		}
		
	}
	public static List<File> getFilesDir(File musicDir1) {
		List<File> fls =  new ArrayList();
		File folder = musicDir1;
		File[] files = folder.listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		        fls.add(file);
		    }
		}
		return fls;
	}
}
