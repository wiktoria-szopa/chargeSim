package chargesim.gui;

import chargesim.Charge;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int CHARGE_RADIUS = 20;

    public interface Listener {
        void cursorMoved(int x, int y);

        void potentialChange(double v);
    }

    Listener listener;
    private java.util.List<Charge> charges = new ArrayList<Charge>();

    public CenterPanel() {
        super();
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    //region mouse listeners
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
        int x = e.getX();
        int y = e.getY();
        if (SwingUtilities.isRightMouseButton(e)) {
            for (Charge charge : charges) {
                if (calculateDistance(x, y, charge.x, charge.y) < CHARGE_RADIUS) {
                    showChargeMenu(x,y);
                }
            }

        }

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
    //endregion mouse listeners

    public void addCharge() {
        Charge charge = new Charge(this.getWidth() / 2, this.getHeight() / 2, 1);
        charges.add(charge);
        this.getGraphics()
                .drawImage(
                        charge.positive.getImage(),
                        charge.x - CHARGE_RADIUS,
                        charge.y - CHARGE_RADIUS,
                        2 * CHARGE_RADIUS,
                        2 * CHARGE_RADIUS,
                        null
                );
    }

    private double calculatePotential(int x, int y) {
        return 1000 * (1 / Math.sqrt(x * x + y * y));
    }

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private void showChargeMenu(int x, int y){
        JPopupMenu chargeMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem deleteItem = new JMenuItem("Delete");

        chargeMenu.add(copyItem);
        chargeMenu.add(deleteItem);
        chargeMenu.add(editItem);

        chargeMenu.show(this, x, y);

        JFrame editMenuFrame = new JFrame();
        editMenuFrame.setSize(250, 100);
        editMenuFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);


        JPanel editMenuPanel = new JPanel();
        editMenuPanel.setLayout(new GridLayout(2,2));

        editMenuFrame.add(editMenuPanel);

        JTextField chargeValueInput = new JTextField();
        chargeValueInput.setText("1");
        JLabel chargeValueLabel = new JLabel("Set charge value:");

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        editMenuPanel.add(chargeValueLabel);
        editMenuPanel.add(chargeValueInput);
        editMenuPanel.add(okButton);
        editMenuPanel.add(cancelButton);

        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMenuFrame.setVisible(true);

            }
        });

    }

}
