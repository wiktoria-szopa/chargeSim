import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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

        JLabel text = new JLabel("hejo");
        panelDown.add(text);


        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem saveItem = new JMenuItem("Save");
        menu.add(saveItem);

        JMenuItem openItem = new JMenuItem("Open");
        menu.add(openItem);

        JMenuItem newItem = new JMenuItem("New");
        menu.add(newItem);

        JMenu menuAdd = new JMenu("Add");
        menuBar.add(menuAdd);

        JMenuItem chargeItem = new JMenuItem("Charge");
        menuAdd.add(chargeItem);

        JMenu menuLanguage = new JMenu("Language");

        JMenuItem polishItem = new JMenuItem("Polish");
        menuLanguage.add(polishItem);

        JMenuItem englishItem = new JMenuItem("English");
        menuLanguage.add(englishItem);

        menuBar.add(menuLanguage);

        JMenu menuColor = new JMenu("Colors");
        menuBar.add(menuColor);

        JMenuItem backgroundColorItem = new JMenuItem("Background");
        backgroundColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser backgroundColorChooser = new JColorChooser();

                Color backgorundColor = JColorChooser.showDialog(null, "Pick a background color", Color.BLACK);
                panelCenter.setBackground(backgorundColor);
            }
        });
        menuColor.add(backgroundColorItem);

        this.setJMenuBar(menuBar);

    }

    public static void main(String[] args) {
        GUI start = new GUI();
        start.setVisible(true);

    }

}
