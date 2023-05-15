package chargesim.gui;


import chargesim.Charge;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                JOptionPane.showMessageDialog(this,"Failed to save into a file","ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public void equipotentialColorChosen(Color color) {
        panelCenter.setEquipotentialColor(color);
    }

    public void backgroundColorChosen(Color color) {
        panelCenter.setBackground(color);
    }

    @Override
    public void addChargeClicked() {
        panelCenter.addCharge();
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
    //endregion centerpanel listener

    private String chargeToString(Charge charge) {
        return String.format("%f\t%f\t%f", charge.getX(), charge.getY(), charge.getValue());
    }
}
