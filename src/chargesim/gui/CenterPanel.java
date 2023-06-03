package chargesim.gui;

import chargesim.Charge;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CenterPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int CHARGE_RADIUS = 30;
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
    private double[][] potentialTab = new double[binTabWidth * rez][binTabWidth * rez];
    private int[][] binTab = new int[binTabWidth][binTabWidth];
    private double maxPot = 0;
    private int iRez;
    private int jRez;

    private int rezE = 21;
    private int widthETab = (int) Math.ceil(800 / 21);
    private double[][] ExTab = new double[widthETab][widthETab];
    private double[][] EyTab = new double[widthETab][widthETab];
    private double maxEx = 0;
    private double maxEy = 0;
    private int iRezE;
    private int jRezE;

    private boolean equipotenttialFlag = true;
    private boolean fieldForceFlag = true;

    private BufferedImage positiveImage;
    private BufferedImage negativeImage;

    private Color equipotentialColor = Color.black;
    private Color forceLineColor = Color.black;

    public interface Listener {
        void cursorMoved(double x, double y);

        void potentialChange(double v);

        void EChange(double ex, double ey);

        void setPotential();

        void setE();
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

        if (equipotenttialFlag) {
            g2d.setColor(equipotentialColor);
            int state = 0;
            g2d.setStroke(new BasicStroke(2));
            if (charges.size() != 0) {
                for (double step = 0.0; step <= maxPot; step += 0.5 + step) {
                    calcBinTab(step);
                    for (int i = 0; i < binTabWidth - 1; i++) {
                        for (int j = 0; j < binTabWidth - 1; j++) {
                            iRez = i * rez;
                            jRez = j * rez;
                            xJump = setJump(potentialTab[iRez][jRez], potentialTab[iRez + 1][jRez], potentialTab[iRez + 2][jRez]);
                            yJump = setJump(potentialTab[iRez][jRez], potentialTab[iRez][jRez + 1], potentialTab[iRez][jRez + 2]);
                            state = getState(binTab[i][j], binTab[i + 1][j], binTab[i + 1][j + 1], binTab[i][j + 1]);
                            switch (state) {
                                case 1:
                                case 14:
                                    g2d.drawLine(iRez, jRez + yJump, iRez + xJump, jRez + dJump);
                                    break;
                                case 2:
                                case 13:
                                    g2d.drawLine(iRez + xJump, jRez + dJump, iRez + dJump, jRez + yJump);
                                    break;
                                case 3:
                                case 12:
                                    g2d.drawLine(iRez, jRez + yJump, iRez + dJump, jRez + yJump);
                                    break;
                                case 4:
                                case 11:
                                    g2d.drawLine(iRez + xJump, jRez, iRez + dJump, jRez + yJump);
                                    break;
                                case 5:
                                    g2d.drawLine(iRez, jRez + yJump, iRez + xJump, jRez);
                                    g2d.drawLine(iRez + xJump, jRez + dJump, iRez + dJump, jRez + yJump);
                                    break;
                                case 6:
                                case 9:
                                    g2d.drawLine(iRez + xJump, jRez, iRez + xJump, jRez + dJump);
                                    break;
                                case 7:
                                case 8:
                                    g2d.drawLine(iRez, jRez + yJump, iRez + xJump, jRez);
                                    break;
                                case 10:
                                    g2d.drawLine(iRez, jRez + yJump, iRez + xJump, jRez + dJump);
                                    g2d.drawLine(iRez + xJump, jRez, iRez + dJump, jRez + yJump);
                                    break;
                                case 15:
                                    break;
                            }
                        }
                    }
                }
            }
            g2d.setColor(Color.black);
        }

        if (fieldForceFlag) {
            int stateE;
            g2d.setStroke(new BasicStroke(3));
            if (charges.size() != 0) {
                for (int i = 0; i < widthETab; i++) {
                    for (int j = 0; j < widthETab; j++) {
                        iRezE = i * rezE;
                        jRezE = j * rezE;
                        stateE = getEState(ExTab[i][j], EyTab[i][j], maxEx, maxEy);
                        g2d.setColor(calcColor(ExTab[i][j], EyTab[i][j]));
                        switch (stateE) {
                            case 0:
                                break;
                            case 1:
                                g2d.drawLine(iRezE + 2, jRezE + 10, iRezE + 18, jRezE + 10);
                                break;
                            case 2:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 11, iRezE + 18, jRezE + 9);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 9, iRezE + 18, jRezE + 11);
                                }
                                break;
                            case 3:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 12, iRezE + 18, jRezE + 8);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 8, iRezE + 18, jRezE + 12);
                                }
                                break;
                            case 4:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 13, iRezE + 18, jRezE + 7);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 7, iRezE + 18, jRezE + 13);
                                }
                                break;
                            case 5:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 14, iRezE + 18, jRezE + 6);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 6, iRezE + 18, jRezE + 14);
                                }
                                break;
                            case 6:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 15, iRezE + 18, jRezE + 5);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 5, iRezE + 18, jRezE + 15);
                                }
                                break;
                            case 7:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 16, iRezE + 18, jRezE + 4);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 4, iRezE + 18, jRezE + 16);
                                }
                                break;
                            case 8:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 17, iRezE + 18, jRezE + 3);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 3, iRezE + 18, jRezE + 17);
                                }
                                break;
                            case 9:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 2, jRezE + 18, iRezE + 18, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 2, jRezE + 2, iRezE + 18, jRezE + 18);
                                }
                                break;
                            case 10:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 3, jRezE + 18, iRezE + 17, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 3, jRezE + 2, iRezE + 17, jRezE + 18);
                                }
                                break;
                            case 11:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 4, jRezE + 18, iRezE + 16, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 4, jRezE + 2, iRezE + 16, jRezE + 18);
                                }
                                break;
                            case 12:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 5, jRezE + 18, iRezE + 15, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 5, jRezE + 2, iRezE + 15, jRezE + 18);
                                }
                                break;
                            case 13:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 6, jRezE + 18, iRezE + 14, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 6, jRezE + 2, iRezE + 14, jRezE + 18);
                                }
                                break;
                            case 14:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 7, jRezE + 18, iRezE + 13, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 7, jRezE + 2, iRezE + 13, jRezE + 18);
                                }
                                break;
                            case 15:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 8, jRezE + 18, iRezE + 12, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 8, jRezE + 2, iRezE + 12, jRezE + 18);
                                }
                                break;
                            case 16:
                                if (EyTab[i][j] / ExTab[i][j] < 0) {
                                    g2d.drawLine(iRezE + 9, jRezE + 18, iRezE + 11, jRezE + 2);
                                } else {
                                    g2d.drawLine(iRezE + 9, jRezE + 2, iRezE + 11, jRezE + 18);
                                }
                                break;
                            case 17:
                                g2d.drawLine(iRezE + 10, jRezE + 18, iRezE + 10, jRezE + 2);
                                break;

                        }
                    }
                }
            }
            g2d.setColor(Color.black);
        }


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
        x = e.getX();
        y = e.getY();
        if (SwingUtilities.isRightMouseButton(e)) {
            boolean chargeClicked = false;
            for (Charge charge : charges) {
                if (calculateDistance(x, y, charge.getX(), charge.getY()) < CHARGE_RADIUS) {
                    showChargeMenu(x, y, charge);
                    chargeClicked = true;
                }
            }
            if (!chargeClicked) {
                showNotChargeMenu(x, y);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e))
            return;
        x = e.getX();
        y = e.getY();
        boolean chargeFound = false;
        for (Charge charge : charges) {
            if (calculateDistance(x, y, charge.getX(), charge.getY()) < CHARGE_RADIUS) {
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
        x = e.getX();
        y = e.getY();
        if (x > 20 && x < 763 && y > 20 && y < 691) {
            movingCharge.setX(x);
            movingCharge.setY(y);
            x = x / 100;
            y = y / 100;
            listener.cursorMoved(x, y);
            listener.setPotential();
            listener.setE();
            calculatePotTab();
            calculateExTab();
            calculateEyTab();
            repaint();
        }

    }
    //endregion mouse listeners    

    //region MarchingSquares
    public void calcBinTab(double value) {
        for (int i = 0; i < binTabWidth; i++) {
            for (int j = 0; j < binTabWidth; j++) {
                if (potentialTab[j * rez][i * rez] >= value) {
                    binTab[j][i] = 1;
                } else {
                    binTab[j][i] = 0;
                }
            }
        }
    }

    public int getState(int a, int b, int c, int d) {
        return a * 8 + b * 4 + c * 2 + d * 1;
    }

    public int setJump(double a, double b, double c) {
        int tmp_jump = 0;
        if (a > b && a > c) {
            tmp_jump = 0;
        }
        if (a < b && b > c) {
            tmp_jump = 1;
        }
        if (a < b && b < c) {
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
        double tmp_r2 = ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return (Math.sqrt(tmp_r2) * tmp_r2);
    }

    //tablica wartosc bezwglendych potencjalu w danych punktach
    public void calculatePotTab() {
        if (charges.size() != 0) {
            double ii;
            double jj;
            for (int i = 0; i < binTabWidth * rez; i++) {
                for (int j = 0; j < binTabWidth * rez; j++) {
                    ii = i;
                    jj = j;
                    ii = ii / 100;
                    jj = jj / 100;

                    if (Math.abs(calculatePotential(ii, jj)) <= 50000) {
                        potentialTab[i][j] = Math.abs(calculatePotential(ii, jj));
                    } else {
                        potentialTab[i][j] = 0.25 * (Math.abs(calculatePotential(ii - 1, jj)) +
                                Math.abs(calculatePotential(ii, jj + 1)) +
                                Math.abs(calculatePotential(ii + 1, jj)) +
                                Math.abs(calculatePotential(ii, jj - 1)));
                    }
                    if (potentialTab[i][j] >= maxPot) {
                        maxPot = potentialTab[i][j];
                    }
                }
            }
        }
    }

    public void zeroPotTab() {
        for (int i = 0; i < binTabWidth * rez; i++) {
            for (int j = 0; j < binTabWidth * rez; j++) {
                potentialTab[i][j] = 0;
            }
        }
    }
    //endregion PotentialCalculation

    //region ElectricFieldCalculation
    private double calcExField(double x, double y) {
        double tmpEx = 0;
        for (Charge charge : charges) {
            tmpEx += (x - charge.getX() / 100) * charge.getValue() / calcThirdDistance(x, y, charge.getX() / 100, charge.getY() / 100);
        }
        return k * tmpEx;
    }

    private double calcEyField(double x, double y) {
        double tmpEy = 0;
        for (Charge charge : charges) {
            tmpEy += (y - charge.getY() / 100) * charge.getValue() / calcThirdDistance(x, y, charge.getX() / 100, charge.getY() / 100);
        }
        return k * tmpEy;
    }

    public void zeroETab() {
        for (int i = 0; i < widthETab; i++) {
            for (int j = 0; j < widthETab; j++) {
                ExTab[i][j] = 0;
                EyTab[i][j] = 0;
            }
        }
    }

    public void calculateExTab() {
        double ii;
        double jj;
        if (charges.size() != 0) {
            for (int i = 0; i < widthETab; i++) {
                for (int j = 0; j < widthETab; j++) {
                    ii = i;
                    jj = j;
                    ii = rezE * (ii / 100);
                    jj = rezE * (jj / 100);

                    if (Math.abs(calcExField(ii, jj)) <= 10000) {
                        ExTab[i][j] = calcExField(ii, jj);
                    } else {
                        ExTab[i][j] = 0.25 * (calcExField(ii - 1, jj) +
                                calcExField(ii, jj + 1) +
                                calcExField(ii + 1, jj) +
                                calcExField(ii, jj - 1));
                    }
                    if (Math.abs(ExTab[i][j]) >= Math.abs(maxEx) && Math.abs(ExTab[i][j]) < 1000) {
                        maxEx = Math.abs(ExTab[i][j]);
                    } else {
                        maxEx = 1000;
                    }
                }
            }
        }

    }

    public void calculateEyTab() {
        double ii;
        double jj;
        if (charges.size() != 0) {
            for (int i = 0; i < widthETab; i++) {
                for (int j = 0; j < widthETab; j++) {
                    ii = i;
                    jj = j;
                    ii = rezE * (ii / 100);
                    jj = rezE * (jj / 100);

                    if (Math.abs(calcEyField(ii, jj)) <= 10000) {
                        EyTab[i][j] = calcEyField(ii, jj);
                    } else {
                        EyTab[i][j] = 0.25 * (calcEyField(ii - 1, jj) +
                                calcEyField(ii, jj + 1) +
                                calcEyField(ii + 1, jj) +
                                calcEyField(ii, jj - 1));
                    }
                    if (Math.abs(EyTab[i][j]) >= Math.abs(maxEy) && Math.abs(EyTab[i][j]) < 1000) {
                        maxEy = Math.abs(EyTab[i][j]);
                    } else {
                        maxEy = 1000;
                    }
                }
            }
        }
    }

    int getEState(double a, double b, double c, double d) {
        double il = 0;
        double maxIl = 1;
        if (a == 0 && b != 0) {
            return 19;
        }
        if (b == 0 && a != 0) {
            return 1;
        }
        if (a != 0 && b != 0) {
            il = Math.abs(b / a);
            if (il < 0.1 * maxIl) {
                return 1;
            }
            if (il < 0.2 * maxIl) {
                return 2;
            }
            if (il < 0.3 * maxIl) {
                return 3;
            }
            if (il < 0.4 * maxIl) {
                return 4;
            }
            if (il < 0.5 * maxIl) {
                return 5;
            }
            if (il < 0.6 * maxIl) {
                return 6;
            }
            if (il < 0.7 * maxIl) {
                return 7;
            }
            if (il < 0.85 * maxIl) {
                return 8;
            }
            if (il < maxIl) {
                return 9;
            }
            if (il < 1.4 * maxIl) {
                return 11;
            }
            if (il < 1.8 * maxIl) {
                return 12;
            }
            if (il < 2.4 * maxIl) {
                return 13;
            }
            if (il < 4.0 * maxIl) {
                return 14;
            }
            if (il < 6.0 * maxIl) {
                return 15;
            }
            if (il < 9.0 * maxIl) {
                return 16;
            }
            if (il > 9.0 * maxIl) {
                return 17;
            }
        }
        return 0;
    }

    Color calcColor(double a, double b) {
        double tmp_d = Math.sqrt(a * a + b * b);
        double tmp_max = Math.sqrt(maxEx * maxEx + maxEy * maxEy);
        Color tmp_color = forceLineColor;
        if (tmp_d < 0.002 * tmp_max) {
            tmp_color = new Color(forceLineColor.getRed(), forceLineColor.getGreen(), forceLineColor.getBlue(), 50);
            return tmp_color;
        }
        if (tmp_d < 0.007 * tmp_max) {
            tmp_color = new Color(forceLineColor.getRed(), forceLineColor.getGreen(), forceLineColor.getBlue(), 100);
            return tmp_color;
        }
        if (tmp_d < 0.015 * tmp_max) {
            tmp_color = new Color(forceLineColor.getRed(), forceLineColor.getGreen(), forceLineColor.getBlue(), 150);
            return tmp_color;
        }
        if (tmp_d < 0.03 * tmp_max) {
            tmp_color = new Color(forceLineColor.getRed(), forceLineColor.getGreen(), forceLineColor.getBlue(), 200);
            return tmp_color;
        }
        if (tmp_d > 0.035 * tmp_max) {
            tmp_color = new Color(forceLineColor.getRed(), forceLineColor.getGreen(), forceLineColor.getBlue(), 250);
            return tmp_color;
        }
        return tmp_color;
    }


    //endregion ElectricFieldCalculation

    public void addCharge() {
        if (charges.size() >= 10) {
            JOptionPane.showMessageDialog(this,
                    "Too many charges on the board",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            Charge charge = new Charge(this.getWidth() / 2, this.getHeight() / 2, 5);
            charges.add(charge);
            calculatePotTab();
            calculateExTab();
            calculateEyTab();
            repaint();
        }
    }

    public void addChargeHere(double x, double y) {
        if (charges.size()>=10) {
            JOptionPane.showMessageDialog(this,
                    "Too many charges on the board",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);

        } else {
            Charge charge = new Charge(x, y, 5);
            charges.add(charge);
            calculatePotTab();
            calculateExTab();
            calculateEyTab();
            repaint();
        }
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
        this.repaint();
    }


    public void clearChargesArray() {
        charges.clear();
        zeroPotTab();
        zeroETab();
        this.repaint();
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> chargeses) {
    	if(charges.size() + chargeses.size() <= 10) {
    		for(Charge ch : chargeses) {
        		charges.add(ch);
        	}
    	}
    	else {
    		JOptionPane.showMessageDialog(this,
                    "Too many charges on the board",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
    	}
    	
        //this.charges = chargeses;
        repaint();
    }

    public boolean isEquipotenttialFlag() {
        return equipotenttialFlag;
    }

    public void setEquipotenttialFlag(boolean equipotenttialFlag) {
        this.equipotenttialFlag = equipotenttialFlag;
        this.repaint();
    }

    public boolean isFieldForceFlag() {
        return fieldForceFlag;
    }

    public void setFieldForceFlag(boolean fieldForceFlag) {
        this.fieldForceFlag = fieldForceFlag;
        this.repaint();
    }


    //enndregion get/set

    //region ChargeMenu
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
                calculateExTab();
                calculateEyTab();
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
                calculateExTab();
                calculateEyTab();
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
                calculateExTab();
                calculateEyTab();
                repaint();

            }
        });

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charges.size() >= 10) {
                    JOptionPane.showMessageDialog(CenterPanel.this,
                            "Too many charges on the board",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);

                } else {
                    int newChargeX = CenterPanel.this.getWidth() / 2;
                    int newChargeY = CenterPanel.this.getHeight() / 2;
                    charges.add(new Charge(newChargeX, newChargeY, charge.getValue()));
                    calculatePotTab();
                    calculateExTab();
                    calculateEyTab();
                    repaint();
                }
            }
        });

    }

    private void showNotChargeMenu(double x, double y) {
        JPopupMenu notChargeMenu = new JPopupMenu();
        JMenuItem addChargeHereItem = new JMenuItem("Add charge here");
        notChargeMenu.add(addChargeHereItem);
        notChargeMenu.show(this, (int) x, (int) y);

        addChargeHereItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addChargeHere(x, y);
            }
        });
    }
    
    //end region ChargeMenu
    
    
}
