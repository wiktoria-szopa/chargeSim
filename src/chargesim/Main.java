package chargesim;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chargesim.gui.GUI;

public class Main {
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				GUI start = new GUI();
		        start.setVisible(true);		
		        
		        
			}
		});        
    }
}
