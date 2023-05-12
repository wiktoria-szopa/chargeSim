package chargesim.gui;

import chargesim.Charge;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.awt.*;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int CHARGE_RADIUS = 20;
    private static final double k = 8.9875; //*Math.pow(10, 9);
    private static final double eQ = 1.6 * Math.pow(10, (-19));
    private double x;
    private double y;
    int nextChargeId = 1;
    Charge movingCharge = null;
    
    
    int rez = 3;
    int jump = 1;
    int dJump = 2;
    private double[][] potentialTab = new double[805][805];
    private int[][] binTab = new int[268][268];

    private BufferedImage positiveImage;
    private BufferedImage negativeImage;

    private Color equipotentialColor = Color.black;
    private Color forceLineColor = Color.black;

    public interface Listener {
        void cursorMoved(double x, double y);

        void potentialChange(double v);
    }

    Listener listener;

    private java.util.List<Charge> charges = new ArrayList<Charge>();

    public CenterPanel() {
        super();
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
        URL resource1 = getClass().getResource("/chargesim/dodatni.png");
        URL resource2 = getClass().getResource("/chargesim/ujemny.png");
        try {
            positiveImage = ImageIO.read(resource1);
            negativeImage = ImageIO.read(resource2);
        } catch (IOException e) {
            System.err.println("Blad odczytu obrazka");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        /*
        g2d.setColor(equipotentialColor);
        if (charges.size() != 0) {
            int A = charges.size();
            double B = calculateAbsCharge();
            double C = 0.05;
            for (double step = 0.0; step <= 30 * B; step += 0.5 + step * 0.25) {
                for (int i = 0; i < this.getWidth(); i++) {
                    for (int j = 0; j < this.getHeight(); j++) {
                        C = Math.sqrt(Math.abs(potentialTab[i + 5][j + 5] - potentialTab[i][j]));
                        if (potentialTab[i][j] <= step && potentialTab[i][j] >= step - 0.12 * C * A - step * 0.005 * A * Math.pow(potentialTab[i][j], 1.2) / (1 + 8 * B)) {
                            g2d.drawOval(i - 1, j - 1, 1, 1);
                        }
                    }
                }
            }
        }
        g2d.setColor(Color.black); */
        
        g2d.setColor(equipotentialColor);
        double B = calculateAbsCharge();
        int state = 0;
        g2d.setStroke( new BasicStroke(3));
        for (double step = 0.0; step <= 30 * B; step += 0.5 + step * 0.25) {
        	calcBinTab(step);
            for (int i = 0; i < 267; i++) {
                for (int j = 0; j < 267; j++) {
                	 state = getState(binTab[i][j], binTab[i+1][j], binTab[i+1][j+1], binTab[i][j+1]);
                	 switch (state) {
                	 case 1:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+jump, j*rez+dJump);
                		 break;
                	 case 2:
                		 g2d.drawLine(i*rez+jump, j*rez+dJump, i*rez+dJump, j*rez+jump);
                		 break;
                	 case 3:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+dJump, j*rez+jump);
                		 break; 
                	 case 4:
                		 g2d.drawLine(i*rez+jump, j*rez, i*rez+dJump, j*rez+jump);
                		 break; 
                	 case 5:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+jump, j*rez);
                		 g2d.drawLine(i*rez+jump, j*rez+dJump, i*rez+dJump, j*rez+jump);
                		 break;
                	 case 6:
                		 g2d.drawLine(i*rez+jump, j*rez, i*rez+jump, j*rez+dJump);
                		 break;
                	 case 7:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+jump, j*rez);
                		 break;
                	 case 8:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+jump, j*rez);
                		 break; 
                	 case 9:
                		 g2d.drawLine(i*rez+jump, j*rez, i*rez+jump, j*rez+dJump);
                		 break; 
                	 case 10:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+jump, j*rez+dJump);
                		 g2d.drawLine(i*rez+jump, j*rez, i*rez+dJump, j*rez+jump);
                		 break;
                	 case 11:
                		 g2d.drawLine(i*rez+jump, j*rez, i*rez+dJump, j*rez+jump);
                		 break;
                	 case 12:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+dJump, j*rez+jump);
                		 break;
                	 case 13:
                		 g2d.drawLine(i*rez+jump, j*rez+dJump, i*rez+dJump, j*rez+jump);
                		 break; 
                	 case 14:
                		 g2d.drawLine(i*rez, j*rez+jump, i*rez+jump, j*rez+dJump);
                		 break; 
                	 case 15:
                		 break;
                	 }
                	 
                }           
            }                  
        }
        g2d.setColor(Color.black);
        


        for (Charge charge : charges) {
            if (charge.getValue() < 0) {
                g2d.drawImage(
                        negativeImage,
                        (int) charge.getX() - CHARGE_RADIUS,
                        (int) charge.getY() - CHARGE_RADIUS,
                        2 * CHARGE_RADIUS,
                        2 * CHARGE_RADIUS,
                        this);
            } else {
                g2d.drawImage(
                        positiveImage,
                        (int) charge.getX() - CHARGE_RADIUS,
                        (int) charge.getY() - CHARGE_RADIUS,
                        2 * CHARGE_RADIUS,
                        2 * CHARGE_RADIUS,
                        this);
            }
        }
    }

    //region mouse listeners
    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        x = x / 100;
        y = y / 100;
        listener.cursorMoved(x, y);
        listener.potentialChange(calculatePotential(x, y));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int tmpX = e.getX();
        int tmpY = e.getY();
        if (SwingUtilities.isRightMouseButton(e)) {
            for (Charge charge : charges) {
                if (calculateDistance(tmpX, tmpY, charge.getX(), charge.getY()) < CHARGE_RADIUS) {
                    showChargeMenu(tmpX, tmpY, charge);
                }
            }
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e))
            return;
        int tmpX = e.getX();
        int tmpY = e.getY();
        boolean chargeFound = false;
        for (Charge charge : charges) {
            if (calculateDistance(tmpX, tmpY, charge.getX(), charge.getY()) < CHARGE_RADIUS) {
                movingCharge = charge;
                chargeFound = true;
                break;
            }
        }
        if (!chargeFound) {
            movingCharge = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        movingCharge = null;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (movingCharge == null) {
            return;
        }
        int tmpX = e.getX();
        int tmpY = e.getY();
        movingCharge.setX(tmpX);
        movingCharge.setY(tmpY);
        calculatePotTab();      
        repaint();

    }
    //endregion mouse listeners    

    //region MarchingSquares
    public void calcBinTab(double value) {
    	 for (int i = 0; i < 268; i++) {
             for (int j = 0; j < 268; j++) {
             	if(potentialTab[j*rez][i*rez] >= value) {
             		binTab[j][i] = 1;
             	}
             	else {
             		binTab[j][i] = 0;
             	}
             	//System.out.print(binTab[j][i]);            	
             }
             //System.out.println("\n");
         }
    }
    
    public int getState(int a, int b, int c, int d) {
    	return a*8 + b*4 + c*2 + d*1;
    }
    
    //endregion MarchingSquares
    
    //region PotentialCalculation
    public double calculateAbsCharge() {
        double tmpCharge = 0;
        for (Charge charge : charges) {
            tmpCharge += Math.abs(charge.getValue());
        }
        return tmpCharge;
    }

    private double calculatePotential(double x, double y) {
        double tmpPotential = 0;
        for (Charge charge : charges) {
            tmpPotential += charge.getValue() / calculateDistance(x, y, charge.getX() / 100, charge.getY() / 100);
        }
        return k * tmpPotential;
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    //tablica wartosc bezwglendych potencjalu w danych punktach
    public void calculatePotTab() {
        if (charges.size() != 0) {
            for (int i = 0; i < this.getWidth() + 5; i++) {
                for (int j = 0; j < this.getHeight() + 5; j++) {
                    double ii = i;
                    double jj = j;
                    ii = ii / 100;
                    jj = jj / 100;
                    potentialTab[i][j] = Math.abs(calculatePotential(ii, jj));
                }
            }
        }
    }
    //endregion PotentialCalculation

    public void addCharge() {
        Charge charge = new Charge(this.getWidth() / 2, this.getHeight() / 2, 5);
        charges.add(charge);
        calculatePotTab();
        //calcBinTab();
        repaint();
    }

    private void showChargeMenu(double x, double y, Charge charge) {
        double initialChargeValue = charge.getValue();
        JPopupMenu chargeMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem deleteItem = new JMenuItem("Delete");

        chargeMenu.add(copyItem);
        chargeMenu.add(deleteItem);
        chargeMenu.add(editItem);

        chargeMenu.show(this, (int) x, (int) y);

        JFrame editMenuFrame = new JFrame();
        editMenuFrame.setSize(450, 150);

        JPanel editMenuPanel = new JPanel();
        editMenuPanel.setLayout(new GridLayout(2, 2));

        editMenuFrame.add(editMenuPanel);

        final int SLIDER_MIN = -10;
        final int SLIDER_MAX = 10;
        JSlider chargeValueInput = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, (int) charge.getValue());
        chargeValueInput.setMajorTickSpacing(2);
        chargeValueInput.setPaintLabels(true);
        chargeValueInput.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                charge.setValue(chargeValueInput.getValue());
                calculatePotTab();
                repaint();

            }
        });

        JLabel chargeValueLabel = new JLabel("Set charge value:");

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMenuFrame.dispose();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                charge.setValue(initialChargeValue);
                calculatePotTab();
                repaint();
                editMenuFrame.dispose();
            }
        });


        editMenuPanel.add(chargeValueLabel);
        editMenuPanel.add(chargeValueInput);
        editMenuPanel.add(okButton);
        editMenuPanel.add(cancelButton);

        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMenuFrame.setLocationRelativeTo(CenterPanel.this);
                editMenuFrame.setVisible(true);

            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                charges.remove(charge);
                calculatePotTab();
                repaint();

            }
        });

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newChargeX = CenterPanel.this.getWidth()/2;
                int newChargeY = CenterPanel.this.getHeight()/2;
                charges.add(new Charge(newChargeX, newChargeY, charge.getValue()));
                calculatePotTab();
                repaint();
            }
        });

    }

    //region get/set
    public Color getEquipotentialColor() {
        return equipotentialColor;
    }

    public void setEquipotentialColor(Color equipotentialColor) {
        this.equipotentialColor = equipotentialColor;
        this.repaint();
    }

    public Color getForceLineColor() {
        return forceLineColor;
    }

    public void setForceLineColor(Color forceLineColor) {
        this.forceLineColor = forceLineColor;
    }

    public void clearChargesArray() {
        charges.clear();
        this.repaint();
    }
    //enndregion get/set

}
