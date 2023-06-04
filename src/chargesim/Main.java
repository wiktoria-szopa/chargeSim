package chargesim;

import chargesim.gui.GUI;

import javax.swing.*;

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
