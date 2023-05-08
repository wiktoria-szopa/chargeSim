package chargesim.gui;

import javax.swing.*;

public class BottomPanel extends JPanel {

    JLabel xcord = new JLabel();
    JLabel ycord = new JLabel();
    JLabel potential = new JLabel();

    JLabel xField = new JLabel("X: ");
    JLabel yField = new JLabel(" Y: ");
    JLabel potentialField = new JLabel("V: ");


    public BottomPanel() {
        super();
        add(xField);
        add(xcord);
        add(yField);
        add(ycord);
        add(potentialField);
        add(potential);
    }

    public void renderXcord(double x) {
        xcord.setText(String.format("%.2f", x) + " m");
    }

    public void renderYcord(double y) {
        ycord.setText(String.format("%.2f", y) + " m");
    }

    public void renderPotential(double v) {
        potential.setText(String.format("%.2f", v) + " V");
    }

}
