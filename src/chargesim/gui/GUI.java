package chargesim.gui;


import chargesim.Charge;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

public class GUI extends JFrame implements Menu.Listener, CenterPanel.Listener {

    CenterPanel panelCenter = new CenterPanel();
    BottomPanel panelDown = new BottomPanel();
    Menu menuBar = new Menu();
    
    //language variables
    private String sSave = "Save";
    private String sOpen = "Open";
    private String sError = "Error";
    private String sFailedToSave = "Failed to save into a file";
    private String sFailedToOpen = "Failed to open: ";
    private String sFailedToOpenTooMany = "Failed to open because of too many charges: ";
    private String sFailedToOpenInvalid = "Failed to open because of invalid charge coordinates: ";


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
    @Override
    public void onSaveClicked() {
        java.util.List<Charge> charges = panelCenter.getCharges();

        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setDialogTitle(sSave);
        saveFileChooser.setApproveButtonText(sSave);
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
                        sFailedToSave,
                        sError,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    @Override
    public void onPngSaveClicked() {
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setDialogTitle(sSave);
        saveFileChooser.setApproveButtonText(sSave);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "png", "png");
        saveFileChooser.setFileFilter(filter);
        int returnVal = saveFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileOut = new File(saveFileChooser.getSelectedFile() + ".png");
            try {
                BufferedImage image = new BufferedImage(panelCenter.getWidth(), panelCenter.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D canvas = image.createGraphics();
                panelCenter.paintAll(canvas);
                ImageIO.write(image, "png", fileOut);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                		sFailedToSave,
                        sError,
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    @Override
    public void onOpenClicked() {
        int i = 0;
        java.util.List<Charge> charges = new ArrayList<>();
        JFileChooser openFileChooser = new JFileChooser();
        openFileChooser.setDialogTitle(sOpen);
        openFileChooser.setApproveButtonText(sOpen);
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
                    i++;
                    if (i < 11) {
                        Charge charge = stringToCharge(line);
                        if(charge.getX() < 0 || charge.getX() > 783 || charge.getY() < 0 || charge.getY() > 711 ){
                            throw new InvalidChargeException();
                        }
                        charges.add(charge);
                        line = reader.readLine();
                    } else {
                        throw new TooManyChargesException();
                    }
                }
                reader.close();
                panelCenter.setCharges(charges);
                panelCenter.calculatePotTab();
                panelCenter.calculateExTab();
                panelCenter.calculateEyTab();
                panelCenter.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        sFailedToOpen + openFileChooser.getSelectedFile().getAbsolutePath(),
                        sError,
                        JOptionPane.ERROR_MESSAGE);
            } catch (TooManyChargesException e) {
                JOptionPane.showMessageDialog(this,
                        sFailedToOpenTooMany + openFileChooser.getSelectedFile().getAbsolutePath(),
                        sError,
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidChargeException e){
                JOptionPane.showMessageDialog(this,
                		sFailedToOpenInvalid + openFileChooser.getSelectedFile().getAbsolutePath(),
                        sError,
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }
    
    public void newItemChosen() {
        panelCenter.clearChargesArray();
        panelCenter.setBackground(Color.white);
        panelCenter.setEquipotentialColor(Color.black);
        panelCenter.setForceLineColor(Color.black);
    }



    @Override
    public void addChargeClicked() {
        panelCenter.addCharge();
    }

    @Override
    public void addDipoleClicked() throws URISyntaxException {
        java.util.List<Charge> charges = new ArrayList<>();
        URL dipole = getClass().getResource("/chargesim/dipole.cs");

        assert dipole != null;
        File fileIn = new File(dipole.toURI());
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
                    sFailedToOpen,
                    sError,
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void addQuadrupoleClicked() throws URISyntaxException {
        java.util.List<Charge> charges = new ArrayList<>();
        URL dipole = getClass().getResource("/chargesim/quadrupole.cs");

        assert dipole != null;
        File fileIn = new File(dipole.toURI());
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
            		sFailedToOpen,
                    sError,
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void equiShowChosen(boolean b) {
        panelCenter.setEquipotenttialFlag(b);
    }

    public void fieldForceShowChosen(boolean b) {
        panelCenter.setFieldForceFlag(b);
    }
    
    public void equipotentialColorChosen(Color color) {
        if(color != null){
            panelCenter.setEquipotentialColor(color);
        };
    }

    public void forceLineColorChosen(Color color) {
        if(color != null){
            panelCenter.setForceLineColor(color);
        };
    }

    public void backgroundColorChosen(Color color) {
        if(color != null){
            panelCenter.setBackground(color);
        }
    }
    
    public void polishItemClicked() {
    	panelCenter.setPolishtext();
        sSave = "Zapisz";
        sOpen = "Otwórz";
        sError = "Błąd";
        sFailedToSave = "Nie udało sie zapisać do pliku";
        sFailedToOpen = "Nie udało się otworzyć: ";
        sFailedToOpenTooMany = "Za duża ilość ładunków! Nie udało się otworzyć pliku: ";
        sFailedToOpenInvalid = "Niewłaściwe parametry ładunków! Nie udało się otworzyć pliku: ";
        UIManager.put("FileChooser.cancelButtonText", "Anuluj");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Format pliku: ");
        UIManager.put("FileChooser.fileNameLabelText", "Nazwa pliku: ");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Wszystkie pliki");
        UIManager.put("FileChooser.fileNameHeaderText", "Nazwa");
        UIManager.put("FileChooser.fileDateHeaderText", "Data modyfikacji");
        UIManager.put("FileChooser.lookInLabelText", "Szukaj w:");   
        //UIManager.put("ColorChooser.swatchesRecent.textAndMnemonic", "Siema");
    }
    
    public void englishItemClicked() {
    	panelCenter.setEnglishText();
        sSave = "Save";
        sOpen = "Open";
        sError = "Error";
        sFailedToSave = "Failed to save into a file";
        sFailedToOpen = "Failed to open: ";
        sFailedToOpenTooMany = "Failed to open because of too many charges: ";
        sFailedToOpenInvalid = "Failed to open because of invalid charge coordinates: ";
        UIManager.put("FileChooser.cancelButtonText", "Cancel");
        UIManager.put("FileChooser.filesOfTypeLabelText", "File Format: ");
        UIManager.put("FileChooser.fileNameLabelText", "File name: ");
        UIManager.put("FileChooser.acceptAllFileFilterText", "All files");
        UIManager.put("FileChooser.fileNameHeaderText", "Name");
        UIManager.put("FileChooser.fileDateHeaderText", "Date Modified");
        UIManager.put("FileChooser.fileDateHeaderText", "Date Modified");
        UIManager.put("FileChooser.lookInLabelText", "Look in:");
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

    //region save/open functions
    private String chargeToString(Charge charge) {
        return String.format(Locale.US, "%f\t%f\t%f", charge.getX(), charge.getY(), charge.getValue());
    }

    private Charge stringToCharge(String line) {
        String[] tmp = line.split("\t");
        double x = Double.parseDouble(tmp[0]);
        double y = Double.parseDouble(tmp[1]);
        double q = Double.parseDouble(tmp[2]);

        return new Charge(x, y, q);
    }
    //endregion save/open functions
}
