package electron.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import electron.utils.logger;

public class database {
	private static File clFile = new File("settings.txt");
	public static List<String> timesType1 = new ArrayList();
	public static List<String> timesType2 = new ArrayList();
	public static List<String> timesType3 = new ArrayList();
	/**
	 * Load method. Must be called before using other functions from this class.
	 */
	public static void load() {
	    FileOptions.loadFile(clFile);
		if(FileOptions.getFileLines(clFile.getPath().toString()).isEmpty()) {return;}
		JSONArray arr = (JSONArray) FileOptions.ParseJs(FileOptions.getFileLine(clFile));
		for(int i = 0;i<arr.size();i++) {
			JSONObject obj = (JSONObject) arr.get(i);
			addTimeType(obj);
		}
		Collections.sort(timesType1);
		Collections.sort(timesType2);
		Collections.sort(timesType3);
	}
	/**
	 * Add item to database method
	 * @param time - time to add
	 * @param type - type of time
	 */
	public static void add(String time,int type) {
		JSONObject obj = new JSONObject();
		obj.put("type", type);
		obj.put("time", time);
		if(FileOptions.getFileLines(clFile.getPath().toString()).isEmpty()) {
			JSONArray arr = new JSONArray();
			arr.add(obj);
			addTimeType(obj);
			write(arr.toJSONString());
		}else {
			JSONArray arr = (JSONArray) FileOptions.ParseJs(FileOptions.getFileLine(clFile));
			arr.add(obj);
			addTimeType(obj);
			write(arr.toJSONString());
		}
	}
	/**
	 * Remove item from database method
	 * @param in - time to remove
	 */
	public static void remove(String in) {
		if(FileOptions.getFileLines(clFile.getPath().toString()).isEmpty()) {
			JOptionPane.showMessageDialog(new JFrame(), "ERROR: database is empty", "SchoolRing", JOptionPane.ERROR_MESSAGE);
		return;}
		JSONArray arr = (JSONArray) FileOptions.ParseJs(FileOptions.getFileLine(clFile));
		for(int i = 0;i<arr.size();i++) {
			JSONObject item = (JSONObject) arr.get(i);
			if(item.get("time").equals(in)) {
				if(Integer.parseInt(String.valueOf(item.get("type"))) == 1) {
					timesType1.remove(String.valueOf(item.get("time")));
				}else if(Integer.parseInt(String.valueOf(item.get("type"))) == 2){
					timesType2.remove(String.valueOf(item.get("time")));
				}else if(Integer.parseInt(String.valueOf(item.get("type"))) == 3){
					timesType3.remove(String.valueOf(item.get("time")));
				}else {
					JOptionPane.showMessageDialog(new JFrame(), "ERROR: type of item removing is incorrect", "SchoolRing", JOptionPane.ERROR_MESSAGE);
					return;
				}
				arr.remove(item);
				logger.log("Removed item "+item.get("time")+". Type of item: "+Integer.parseInt(String.valueOf(item.get("type"))));
				break;
			}
		}
		write(arr.toJSONString());
	}
	public static JSONObject getInfoItem(String in) {
		JSONArray arr = (JSONArray) FileOptions.ParseJs(FileOptions.getFileLine(clFile));
		for(int i = 0;i<arr.size();i++) {
			JSONObject item = (JSONObject) arr.get(i);
			if(item.get("time").equals(in)) {
				return item;
			}
		}
		return null;
	}
	/**
	 * Add JSONObject to it`s list
	 * 1-list1
	 * 2-list2
	 * 3-list3
	 */
	private static void addTimeType(JSONObject obj) {
		if(Integer.parseInt(String.valueOf(obj.get("type"))) == 1) {
			timesType1.add(String.valueOf(obj.get("time")));
		}else if(Integer.parseInt(String.valueOf(obj.get("type"))) == 2){
			timesType2.add(String.valueOf(obj.get("time")));
		}else{
			timesType3.add(String.valueOf(obj.get("time")));
		}
	}
	/**
	 * Get list of times for type
	 * @param type - type of list you want to get
	 * @return times for this type
	 */
	public static List<String> get(int type) {
		if(type == 1) {
			return timesType1;
		}else if (type==2){
			return timesType2;
		}else {
			return timesType3;
		}
	}
	/**
	 * Get list with all times
	 * @return list with all times
	 */
	public static List<String> getAll(){
		List<String> arr = new ArrayList();
		arr.addAll(timesType1);
		arr.addAll(timesType2);
		arr.addAll(timesType3);
		return arr;
	}
	/**
	 * Write to file method
	 * @param toWrite - string to write
	 */
	public static void write(String toWrite) {
		FileOptions.writeFile(toWrite, clFile);
	}
}
