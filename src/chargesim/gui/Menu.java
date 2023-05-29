package chargesim.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

public class Menu extends JMenuBar {

    public interface Listener {   
        void backgroundColorChosen(Color color);
        void forceLineColorChosen(Color color);
        void equipotentialColorChosen(Color color);
        void addChargeClicked();
        void addDipoleClicked() throws URISyntaxException;
        void addQuadrupoleClicked() throws URISyntaxException;
        void newItemChosen();
        void onSaveClicked();
        void onOpenClicked();
        void equiShowChosen(boolean b);
        void fieldForceShowChosen(boolean b);
    }

    //region fields
    //menu glowne
    JMenu menu = new JMenu("Menu");

    //przyciski w menu glownym
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem newItem = new JMenuItem("New");

    //menu dodaj
    JMenu menuAdd = new JMenu("Add");

    //przyciski w menu dodaj
    JMenuItem chargeItem = new JMenuItem("Charge");
    JMenuItem dipoleItem = new JMenuItem("Dipole");
    JMenuItem quadrupoleItem = new JMenuItem("Quadrupole");

    //menu language
    JMenu menuLanguage = new JMenu("Language");

    //przyciski w menu language
    JMenuItem polishItem = new JMenuItem("Polish");
    JMenuItem englishItem = new JMenuItem("English");
    
    //menu show
    JMenu menuShow = new JMenu("Show");
    
    //przyciski w menu show
    JCheckBoxMenuItem equiShowItem = new JCheckBoxMenuItem("Equipotential lines");
    JCheckBoxMenuItem forceLineShowItem = new JCheckBoxMenuItem("Field force lines");

    //menu color
    JMenu menuColor = new JMenu("Colors");

    //przyciski menu color
    JMenuItem backgroundColorItem = new JMenuItem("Background");

    JMenuItem equipotentialColorItem = new JMenuItem("Equipotential  lines");

    JMenuItem fieldForceColorItem = new JMenuItem("Field force lines");

    //endregion fields

    public Menu.Listener listener;

    public Menu() {
        super();
        add(menu);

        menu.add(saveItem);
        menu.add(openItem);
        menu.add(newItem);

        add(menuAdd);
        menuAdd.add(chargeItem);
        menuAdd.add(dipoleItem);
        menuAdd.add(quadrupoleItem);

        add(menuShow);
        equiShowItem.setState(true);
        forceLineShowItem.setState(true);
        menuShow.add(equiShowItem);
        menuShow.add(forceLineShowItem);
        
        equiShowItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.equiShowChosen(equiShowItem.getState());
				
			}
		});
        
        forceLineShowItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.fieldForceShowChosen(forceLineShowItem.getState());
				
			}
		});

        add(menuColor);
        menuColor.add(backgroundColorItem);
        menuColor.add(equipotentialColorItem);
        menuColor.add(fieldForceColorItem);

        backgroundColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color backgorundColor = JColorChooser.showDialog(null, "Pick a background color", Color.BLACK);
                listener.backgroundColorChosen(backgorundColor);
            }
        });

        equipotentialColorItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color isolineColor = JColorChooser.showDialog(null, "Pick a isoline color", Color.BLACK);
                listener.equipotentialColorChosen(isolineColor);
			}
		});
        
        fieldForceColorItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color forceLineColor = JColorChooser.showDialog(null, "Pick a pick force line color", Color.BLACK);
				listener.forceLineColorChosen(forceLineColor);
			}
		});
        
        add(menuLanguage);
        menuLanguage.add(polishItem);
        menuLanguage.add(englishItem);
        
        chargeItem.addActionListener(e -> listener.addChargeClicked());
        
        newItem.addActionListener(e -> listener.newItemChosen());

        saveItem.addActionListener(e -> listener.onSaveClicked());

        openItem.addActionListener(e -> listener.onOpenClicked());

        dipoleItem.addActionListener(e-> {
            try {
                listener.addDipoleClicked();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });

        quadrupoleItem.addActionListener(e-> {
            try {
                listener.addQuadrupoleClicked();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });


    }
}
