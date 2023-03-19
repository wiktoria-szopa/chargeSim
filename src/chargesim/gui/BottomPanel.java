package chargesim.gui;

import javax.swing.*;

public class BottomPanel extends JPanel {

    JLabel xcord;
    JLabel ycord;
    JLabel potential;

    JLabel xField = new JLabel("X: ");
    JLabel yField = new JLabel(" Y: ");
    JLabel vField = new JLabel("V: ");


    public BottomPanel(CenterPanel centerPanel) {
        super();

        xcord = new JLabel(Integer.toString(centerPanel.x));
        ycord = new JLabel(Integer.toString(centerPanel.y));
        potential = new JLabel(Double.toString(centerPanel.v));


        add(xField);
        add(xcord);
        add(yField);
        add(ycord);
        add(vField);
        add(potential);


    }

}
