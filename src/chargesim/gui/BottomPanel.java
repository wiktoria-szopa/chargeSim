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

    public void renderXcord(int x){
        xcord.setText(Integer.toString(x));
    }

    public void renderYcord(int y){
        ycord.setText(Integer.toString(y));
    }

    public void renderPotential(double v){
        potential.setText(String.format("%.2f",v));
    }

}
