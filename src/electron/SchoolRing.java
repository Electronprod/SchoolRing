package electron;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.JSONObject;

import electron.data.DirOptions;
import electron.data.database;
import electron.ring.ringThread;
import electron.utils.logger;

public class SchoolRing extends JPanel {
	//Directories for music
	public static File musicDir1 = new File("fromlesson");
	public static File musicDir2 = new File("tolesson");
	public static File musicDir3 = new File("beforelesson");
	//GUI values
    private JScrollPane ScrollField;
    private JButton createbtn;
    private JButton deletebtn;
    private JTextField dateField;
    private JLabel title1;
    private JLabel title2;
    private JComboBox typeSelector;
    private JLabel status;
    private JLabel about;
    private JButton github;
    private java.util.List<String> listInfo;
    private String[] jcomp8Items = {"From lesson", "To lesson","Before lesson"};
    
    /**
     * GUI builder and worker
     */
    public SchoolRing() {
        //construct preComponents
    	listInfo=database.getAll();
        final DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < listInfo.size(); i++) {
            listModel.addElement(listInfo.get(i));
        }
        //construct components
        final JList list = new JList(listModel);
        ScrollField = new JScrollPane (list);
        createbtn = new JButton ("Create");
        deletebtn = new JButton ("Delete");
        dateField = new JTextField (5);
        dateField.setText("9:00");
        title1 = new JLabel ("Time");
        title2 = new JLabel ("Ring type:");
        typeSelector = new JComboBox (jcomp8Items);
        status = new JLabel ("Ring info");
        about = new JLabel ("<html>Made by <a href=https://github.com/Electronprod>Electron_prod</a>");
        github = new JButton ("GitHub");

        //adjust size and set layout
        setPreferredSize (new Dimension (357, 334));
        setLayout (null);

        //add components
        add (ScrollField);
        add (createbtn);
        add (deletebtn);
        add (dateField);
        add (title1);
        add (title2);
        add (typeSelector);
        add (status);
        add (about);
        add (github);

        //set component bounds (only needed by Absolute Positioning)
        ScrollField.setBounds (5, 5, 235, 300);
        list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(list.getSelectedIndex()==-1) {status.setText("Select item to show type");return;}
				JSONObject obj = database.getInfoItem(listInfo.get(list.getSelectedIndex()));
				status.setText("Selected type: "+jcomp8Items[Integer.parseInt(String.valueOf(obj.get("type")))-1]);
				
			}       	
        });
        createbtn.setBounds (245, 125, 100, 25);
        createbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(database.getAll().contains(dateField.getText())) {
        			JOptionPane.showMessageDialog(new JFrame(), "ERROR: this time already exists!", "SchoolRing", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		java.util.List<String> sorted = database.getAll();
        		Collections.sort(sorted);
        		listInfo = sorted;
        		database.add(dateField.getText(), (typeSelector.getSelectedIndex()+1));
        		status.setText("Added time to database. Type added: "+(typeSelector.getSelectedIndex()+1));
        		java.util.List<String> sorted1 = database.getAll();
        		Collections.sort(sorted1);
        		listInfo = sorted1;
        		list.setListData(sorted1.toArray(new String[0]));
        		repaint();
        	}});
        deletebtn.setBounds (245, 160, 100, 25);
        deletebtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(list.isSelectionEmpty()) {
        			JOptionPane.showMessageDialog(new JFrame(), "Please, select time you want to delete", "SchoolRing", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		java.util.List<String> sorted = database.getAll();
        		Collections.sort(sorted);
        		listInfo = sorted;
        		database.remove(listInfo.get(list.getSelectedIndex()));
        		java.util.List<String> sorted1 = database.getAll();
        		Collections.sort(sorted1);
        		list.setListData(sorted1.toArray(new String[0]));
        		listInfo = sorted1;
        		status.setText("Removed time from database.");
        		repaint();
        	}});
        dateField.setBounds (245, 40, 100, 25);
        title1.setBounds (245, 10, 100, 25);
        title2.setBounds (245, 70, 100, 25);
        typeSelector.setBounds (245, 95, 100, 25);
        status.setBounds (0, 305, 230, 25);
        about.setBounds (250, 225, 90, 35);
        github.setBounds (255, 265, 80, 15);
        github.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {		    
        				try {
							Desktop.getDesktop().browse(new URI("https://github.com/Electronprod/SchoolRing"));
						} catch (IOException | URISyntaxException e1) {
							JOptionPane.showMessageDialog(new JFrame(), "ERROR: "+e1.getMessage(), "SchoolRing", JOptionPane.ERROR_MESSAGE);
		        			e1.printStackTrace();
						}	
            	}
        }});
    }


    public static void main (String[] args) {
    	logger.enDebug=false; // for developers
    	//Load database
    	database.load();
    	//Load directories
    	DirOptions.loadDir(musicDir1);
    	DirOptions.loadDir(musicDir2);
    	DirOptions.loadDir(musicDir3);
    	//Create GUI
        JFrame frame = new JFrame ("SchoolRing");
        frame.setDefaultCloseOperation (JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add (new SchoolRing());
        frame.setResizable(false);
        Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
        frame.setIconImage(image);;
        frame.pack();
        frame.setVisible (true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
              logger.log("[TRAY]: Window closed.");
              enableTrayIcon(frame);
              e.getWindow().dispose();
            }
        });
        //Start time checker thread
        Thread c = new ringThread();
        c.start();
    }
    
    /**
     * Tray mode function
     * @param fr - frame to hide
     */
	 private static void enableTrayIcon(JFrame fr) {
		    if(!SystemTray.isSupported() ) {
		      System.out.println("[TRAY]: function not supported. Program is going to exit, bye.");
		      System.exit(0);
		    }
		    SystemTray tray = SystemTray.getSystemTray();
		    PopupMenu trayMenu = new PopupMenu();
		    MenuItem open = new MenuItem("Open app");
		    trayMenu.add(open);
		    MenuItem exit = new MenuItem("Exit");
		    exit.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		      }
		    });
		    trayMenu.add(exit);
		    Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
		    TrayIcon trayIcon = new TrayIcon(image, "SchoolRing", trayMenu);
		    trayIcon.setImageAutoSize(true);
		    open.addActionListener(new ActionListener() {
			      @Override
			      public void actionPerformed(ActionEvent e) {
			        fr.setVisible(true);
			        logger.log("[TRAY]: Showed window.");
			        tray.remove(trayIcon);
			      }
			    });
		    try {
		      tray.add(trayIcon);
		    } catch (AWTException e) {
		      e.printStackTrace();
		    }

		    trayIcon.displayMessage("SchoolRing", "App minimized to tray.",TrayIcon.MessageType.INFO);
		  }
}