package chargesim.gui;

import chargesim.Charge;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    public interface Listener {
        void cursorMoved(int x, int y);

        void potentialChange(double v);
    }

    Listener listener;

    public CenterPanel() {
        super();
        setBackground(Color.PINK);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public double calculatePotential(int x, int y) {
        return 1000 * (1 / Math.sqrt(x * x + y * y));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        listener.cursorMoved(x, y);
        listener.potentialChange(calculatePotential(x, y));
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
