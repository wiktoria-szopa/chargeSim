import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI extends JFrame {

    public GUI() {
        this.setSize(640, 480);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelCenter = new JPanel();
        add(BorderLayout.CENTER, panelCenter);
        panelCenter.setBackground(Color.GREEN);

        JPanel panelDown = new JPanel();
        add(BorderLayout.SOUTH, panelDown);

        JLabel text = new JLabel("siema");
        panelDown.add(text);



        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem save = new JMenuItem("Save");
        menu.add(save);


        this.setJMenuBar(menuBar);

    }

    public static void main(String[] args) {
        GUI start = new GUI();
        start.setVisible(true);

    }

}
