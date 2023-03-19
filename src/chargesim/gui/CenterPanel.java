package chargesim.gui;

import chargesim.Charge;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    public interface Listener{
        void cursorMoved(int x, int y);
    }

    int chargeCounter = 0;

    Listener listener;

    Charge ladunek = new Charge();
    public CenterPanel(){
        super();
        setBackground(Color.PINK);
    }

    /*public void countPotential(int rx, int ry) {
        v = 1000*(1/Math.sqrt(rx*rx + ry*ry));
    }*/

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        listener.cursorMoved(x,y);

        /*if(chargeCounter == 0) {
            v = 0;
        }
        else {
            int rX = ladunek.x - e.getX();
            int rY = ladunek.y - e.getY();
            this.countPotential(rX, rY);
        }
        bottomPanel.potential.setText(String.format("%.2f",v));*/

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

}
