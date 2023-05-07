package chargesim;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
