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
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int CHARGE_RADIUS = 20;
    private static final double k = 8.9875;
    private double x;
    private double y;
    
    int nextChargeId = 1;
    Charge movingCharge = null;    
    
    private int rez = 2;
    private int dJump = 2;
    private int xJump = 1;
    private int yJump = 1;
    private int binTabWidth = 400;
    private double[][] potentialTab = new double[binTabWidth*rez][binTabWidth*rez];
    private int[][] binTab = new int[binTabWidth][binTabWidth];
    private double maxPot = 0;

    private BufferedImage positiveImage;
    private BufferedImage negativeImage;

    private Color equipotentialColor = Color.black;
    private Color forceLineColor = Color.black;

    public interface Listener {
        void cursorMoved(double x, double y);

        void potentialChange(double v);
        
        void EChange(double ex, double ey);
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
        
        g2d.setColor(equipotentialColor);
        int state = 0;
        g2d.setStroke(new BasicStroke(2));
        if(charges.size() != 0) {
        	for (double step = 0.0; step <= maxPot; step += 0.5 + step) {
            	calcBinTab(step);
                for (int i = 0; i < binTabWidth-1; i++) {
                    for (int j = 0; j < binTabWidth-1; j++) {
                    	 xJump = setJump(potentialTab[i*rez][j*rez], potentialTab[i*rez+1][j*rez], potentialTab[i*rez+2][j*rez]);
                    	 yJump = setJump(potentialTab[i*rez][j*rez], potentialTab[i*rez][j*rez+1], potentialTab[i*rez][j*rez+2]);
                    	 state = getState(binTab[i][j], binTab[i+1][j], binTab[i+1][j+1], binTab[i][j+1]);
                    	 switch (state) {
                    	 case 1:               		 
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+xJump, j*rez+dJump);
                    		 break;
                    	 case 2:
                    		 g2d.drawLine(i*rez+xJump, j*rez+dJump, i*rez+dJump, j*rez+yJump);
                    		 break;
                    	 case 3:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+dJump, j*rez+yJump);
                    		 break; 
                    	 case 4:
                    		 g2d.drawLine(i*rez+xJump, j*rez, i*rez+dJump, j*rez+yJump);
                    		 break; 
                    	 case 5:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+xJump, j*rez);
                    		 g2d.drawLine(i*rez+xJump, j*rez+dJump, i*rez+dJump, j*rez+yJump);
                    		 break;
                    	 case 6:
                    		 g2d.drawLine(i*rez+xJump, j*rez, i*rez+xJump, j*rez+dJump);
                    		 break;
                    	 case 7:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+xJump, j*rez);
                    		 break;
                    	 case 8:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+xJump, j*rez);
                    		 break; 
                    	 case 9:
                    		 g2d.drawLine(i*rez+xJump, j*rez, i*rez+xJump, j*rez+dJump);
                    		 break; 
                    	 case 10:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+xJump, j*rez+dJump);
                    		 g2d.drawLine(i*rez+xJump, j*rez, i*rez+dJump, j*rez+yJump);
                    		 break;
                    	 case 11:
                    		 g2d.drawLine(i*rez+xJump, j*rez, i*rez+dJump, j*rez+yJump);
                    		 break;
                    	 case 12:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+dJump, j*rez+yJump);
                    		 break;
                    	 case 13:
                    		 g2d.drawLine(i*rez+xJump, j*rez+dJump, i*rez+dJump, j*rez+yJump);
                    		 break; 
                    	 case 14:
                    		 g2d.drawLine(i*rez, j*rez+yJump, i*rez+xJump, j*rez+dJump);
                    		 break; 
                    	 case 15:
                    		 break;
                    	 }                	 
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
        listener.EChange(calcExField(x, y), calcEyField(x, y));
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
        mouseMoved(e);
        calculatePotTab();      
        repaint();

    }
    //endregion mouse listeners    

    //region MarchingSquares
    public void calcBinTab(double value) {
    	 for (int i = 0; i < binTabWidth; i++) {
             for (int j = 0; j < binTabWidth; j++) {
             	if(potentialTab[j*rez][i*rez] >= value) {
             		binTab[j][i] = 1;
             	}
             	else {
             		binTab[j][i] = 0;
             	}           	
             }
         }
    }
    
    public int getState(int a, int b, int c, int d) {
    	return a*8 + b*4 + c*2 + d*1;
    }
    
    public int setJump(double a, double b, double c) {
    	int tmp_jump = 0;
    	if(a>b && a>c) {
    		tmp_jump = 0;
    	}
    	if(a<b && b>c) {
    		tmp_jump = 1;
    	}
    	if(a<b && b<c) {
    		tmp_jump = 2;
    	}
    	return tmp_jump;
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
    
    private double calcThirdDistance(double x1, double y1, double x2, double y2) {
        return Math.pow(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)),1.5);
    }

    //tablica wartosc bezwglendych potencjalu w danych punktach
    public void calculatePotTab() {
        if (charges.size() != 0) {
            for (int i = 0; i < binTabWidth*rez; i++) {
                for (int j = 0; j < binTabWidth*rez; j++) {
                    double ii = i;
                    double jj = j;
                    ii = ii / 100;
                    jj = jj / 100;
                    
                    if(Math.abs(calculatePotential(ii, jj)) <= 50000) {
                    	potentialTab[i][j] = Math.abs(calculatePotential(ii, jj));
                    }
                    else {
                    	potentialTab[i][j] = 0.25*(Math.abs(calculatePotential(ii-1, jj)) +
                    		Math.abs(calculatePotential(ii, jj+1)) +
                    		Math.abs(calculatePotential(ii+1, jj)) +
                    		Math.abs(calculatePotential(ii, jj-1)));
                    }                                        
                    if(potentialTab[i][j] >= maxPot) {
                    	maxPot = potentialTab[i][j];
                    }
                }
            }
        }
    }        
    //endregion PotentialCalculation
    
    //region ElectricFieldCalculation
    private double calcExField(double x, double y) {
    	double tmpEx =0;
    	for (Charge charge : charges) {
            tmpEx += (x- charge.getX()/100) * charge.getValue() / calcThirdDistance(x, y, charge.getX() / 100, charge.getY() / 100);
        }
    	return k * tmpEx;
    }
    
    private double calcEyField(double x, double y) {
    	double tmpEy =0;
    	for (Charge charge : charges) {
            tmpEy += (y- charge.getY()/100) * charge.getValue() / calcThirdDistance(x, y, charge.getX() / 100, charge.getY() / 100);
        }
    	return k * tmpEy;
    }
    
    public void calculateETab() {
    	
    }
    
    //endregion ElectricFieldCalculation
    
    public void addCharge() {
        Charge charge = new Charge(this.getWidth()/2, this.getHeight()/2, 5);
        charges.add(charge);
        calculatePotTab();
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
        calculatePotTab();
        this.repaint();
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges){
        this.charges = charges;
        repaint();
    }
    //enndregion get/set

}
