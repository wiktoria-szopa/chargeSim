package chargesim.gui;


import chargesim.Charge;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame implements Menu.Listener, CenterPanel.Listener {
    //panele
    CenterPanel panelCenter = new CenterPanel();
    BottomPanel panelDown = new BottomPanel();
    Menu menuBar = new Menu();

    public GUI() {
        this.setSize(800, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        setLayout(new BorderLayout());

        add(BorderLayout.CENTER, panelCenter);
        add(BorderLayout.SOUTH, panelDown);

        setJMenuBar(menuBar);
        menuBar.listener = this;
        panelCenter.listener = this;
    }

    //region menu listener
    public void newItemChosen() {
        panelCenter.clearChargesArray();
        panelCenter.setBackground(Color.white);
        panelCenter.setEquipotentialColor(Color.black);
        panelCenter.setForceLineColor(Color.black);
    }

    @Override
    public void onSaveClicked() {
        java.util.List<Charge> charges = panelCenter.getCharges();

        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setApproveButtonText("Save");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "charge sim files", "cs");
        saveFileChooser.setFileFilter(filter);
        int returnVal = saveFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileOut = new File(saveFileChooser.getSelectedFile() + ".cs");
            try {
                BufferedWriter fileOutWriter = new BufferedWriter(new FileWriter(fileOut));
                for (Charge charge : charges) {
                    fileOutWriter.write(chargeToString(charge));
                    fileOutWriter.newLine();
                }
                fileOutWriter.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to save into a file",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    @Override
    public void onOpenClicked() {
        java.util.List<Charge> charges = new ArrayList<>();
        JFileChooser openFileChooser = new JFileChooser();
        openFileChooser.setApproveButtonText("Open");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "charge sim files", "cs");
        openFileChooser.setFileFilter(filter);
        int returnVal = openFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileIn = openFileChooser.getSelectedFile();
            try {

                BufferedReader reader = new BufferedReader(new FileReader(fileIn));
                String line = reader.readLine();
                while (line != null) {
                    charges.add(stringToCharge(line));
                    line = reader.readLine();
                }
                reader.close();
                panelCenter.setCharges(charges);
                panelCenter.calculatePotTab();
                panelCenter.calculateExTab();
                panelCenter.calculateEyTab();
                panelCenter.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to open: " + openFileChooser.getSelectedFile().getAbsolutePath(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void equipotentialColorChosen(Color color) {
        panelCenter.setEquipotentialColor(color);
    }
    
    public void forceLineColorChosen(Color color) {
    	panelCenter.setForceLineColor(color);
    }

    public void backgroundColorChosen(Color color) {
        panelCenter.setBackground(color);
    }

    @Override
    public void addChargeClicked() {
        panelCenter.addCharge();
    }
    
    public void equiShowChosen(boolean b) {
    	panelCenter.setEquipotenttialFlag(b);
    }
    
    public void fieldForceShowChosen(boolean b) {
    	panelCenter.setFieldForceFlag(b);
    }
    //endregion menu listner

    //region centerpanel listener
    @Override
    public void cursorMoved(double x, double y) {
        panelDown.renderXcord(x);
        panelDown.renderYcord(y);
    }

    @Override
    public void potentialChange(double v) {
        panelDown.renderPotential(v);
    }
    
    public void setPotential() {
    	panelDown.setPotential();
    }
    
    public void EChange(double ex, double ey) {
    	panelDown.renderE(ex, ey);
    }
    
    public void setE() {
    	panelDown.setE();
    }
    
    //endregion centerpanel listener

    private String chargeToString(Charge charge) {
        return String.format(Locale.US,"%f\t%f\t%f", charge.getX(), charge.getY(), charge.getValue());
    }

    private Charge stringToCharge(String line){
        String [] tmp = line.split("\t");
        double x = Double.parseDouble(tmp[0]);
        double y = Double.parseDouble(tmp[1]);
        double q = Double.parseDouble(tmp[2]);

        return new Charge(x,y,q);
    }
}
