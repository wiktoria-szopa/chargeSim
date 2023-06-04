package chargesim;

import chargesim.gui.GUI;

import java.util.Enumeration;
import java.util.Locale;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				GUI start = new GUI();
		        start.setVisible(true);
		        UIDefaults defaults = UIManager.getDefaults(); 
		        System.out.println(defaults.size()+ " properties"); 
		        for (Enumeration e = defaults.keys(); 
		            e.hasMoreElements();) { 
		          Object key = e.nextElement(); 
		          System.out.println(key + " = " + defaults.get(key)); 
		        }
			}
		});        
    }
}
