import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    //panele
    JPanel panelCenter = new JPanel();
    JPanel panelDown = new JPanel();

    //label w ktory wrzucimy wspolrzedne itp
    JLabel text = new JLabel("bajo");

    //menubar
    JMenuBar menuBar = new JMenuBar();

    //menu glowne
    JMenu menu = new JMenu("Menu");

    //przyciski w menu glownym
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem newItem = new JMenuItem("New");

    //menu dodaj
    JMenu menuAdd = new JMenu("Add");

    //przyciski w menu dodaj
    JMenuItem chargeItem = new JMenuItem("Charge");

    //menu language
    JMenu menuLanguage = new JMenu("Language");

    //przyciski w menu language
    JMenuItem polishItem = new JMenuItem("Polish");
    JMenuItem englishItem = new JMenuItem("English");

    //menu color
    JMenu menuColor = new JMenu("Colors");

    //przyciski menu color
    JMenuItem backgroundColorItem = new JMenuItem("Background");

    AddCharge addChargePanel = new AddCharge();

    public GUI() {
        this.setSize(640, 480);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.add(addChargePanel);



        add(BorderLayout.CENTER, panelCenter);
        panelCenter.setBackground(Color.GREEN);


        add(BorderLayout.SOUTH, panelDown);


        panelDown.add(text);


        menuBar.add(menu);


        menu.add(saveItem);
        menu.add(openItem);
        menu.add(newItem);


        menuBar.add(menuAdd);
        menuAdd.add(chargeItem);

        /*chargeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                public void paint(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawOval(150, 150, 100, 100);

                }
            }
        });*/


        menuLanguage.add(polishItem);
        menuLanguage.add(englishItem);

        menuBar.add(menuLanguage);


        menuBar.add(menuColor);


        backgroundColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
