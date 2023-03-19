package chargesim.gui;

import chargesim.Charge;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    int chargeCounter = 0;

    Charge ladunek = new Charge();
    public double v = 0;
    public int x = 0;
    public int y = 0;
    public CenterPanel(){
        super();
        setBackground(Color.PINK);
    }

    public void countPotential(int rx, int ry) {
        v = 1000*(1/Math.sqrt(rx*rx + ry*ry));
    }

    public void mouseMoved(MouseEvent e, BottomPanel bottomPanel) {
        x = e.getX();
        y = e.getY();
        bottomPanel.xcord.setText(Integer.toString(x));
        bottomPanel.ycord.setText(Integer.toString(y));

        if(chargeCounter == 0) {
            v = 0;
        }
        else {
            int rX = ladunek.x - e.getX();
            int rY = ladunek.y - e.getY();
            this.countPotential(rX, rY);
        }
        bottomPanel.potential.setText(String.format("%.2f",v));

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
