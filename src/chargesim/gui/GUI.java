package chargesim.gui;


import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

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
}
