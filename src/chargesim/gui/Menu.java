package chargesim.gui;

import javax.print.attribute.standard.MediaSize.Engineering;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

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
        void onPngSaveClicked();
        void polishItemClicked();
        void englishItemClicked();
    }

    //region fields
    //main menu
    JMenu menu = new JMenu("Menu");
    JMenu saveMenu = new JMenu("Save");
    JMenuItem saveAsPngItem = new JMenuItem("as png");
    JMenuItem saveItem = new JMenuItem("as charge sim file");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem newItem = new JMenuItem("New");

    //menu add
    JMenu menuAdd = new JMenu("Add");
    JMenuItem chargeItem = new JMenuItem("Charge");
    JMenuItem dipoleItem = new JMenuItem("Dipole");
    JMenuItem quadrupoleItem = new JMenuItem("Quadrupole");

    //menu language
    JMenu menuLanguage = new JMenu("Language");
    JMenuItem polishItem = new JMenuItem("Polish");
    JMenuItem englishItem = new JMenuItem("English");
    
    //menu show
    JMenu menuShow = new JMenu("View");
    JCheckBoxMenuItem equiShowItem = new JCheckBoxMenuItem("Equipotential lines");
    JCheckBoxMenuItem forceLineShowItem = new JCheckBoxMenuItem("Field force lines");

    //menu color
    JMenu menuColor = new JMenu("Colors");
    JMenuItem backgroundColorItem = new JMenuItem("Background");
    JMenuItem equipotentialColorItem = new JMenuItem("Equipotential  lines");
    JMenuItem fieldForceColorItem = new JMenuItem("Field force lines");

    //endregion fields
    
    //language variables
    private String sPickBackroundColor = "Pick a background color";
    private String sPickEquiColor = "Pick a isoline color";
    private String sPickForceLineColor = "Pick a pick force line color";
    
    public Menu.Listener listener;

    public Menu() {
        super();
        add(menu);
        
        menu.add(saveMenu);
        saveMenu.add(saveItem);
        saveMenu.add(saveAsPngItem);
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
        
        add(menuColor);
        menuColor.add(backgroundColorItem);
        menuColor.add(equipotentialColorItem);
        menuColor.add(fieldForceColorItem);

        add(menuLanguage);
        menuLanguage.add(polishItem);
        menuLanguage.add(englishItem);
        
        //main menu actionlisteners
        saveItem.addActionListener(e -> listener.onSaveClicked());
        
        saveAsPngItem.addActionListener(e -> listener.onPngSaveClicked());

        openItem.addActionListener(e -> listener.onOpenClicked());
        
        newItem.addActionListener(e -> listener.newItemChosen());
        
        //menu add actionlisteners
        chargeItem.addActionListener(e -> listener.addChargeClicked());
        
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
               
        //menu show actionlisteners
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


        //menu colors actionlisteners
        backgroundColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                Color backgroundColor = JColorChooser.showDialog(GUI.panelCenter, sPickBackroundColor, Color.BLACK);              
                listener.backgroundColorChosen(backgroundColor);
            }
        });

        equipotentialColorItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color isolineColor = JColorChooser.showDialog(GUI.panelCenter, sPickEquiColor, Color.BLACK);
                listener.equipotentialColorChosen(isolineColor);
			}
		});
        
        fieldForceColorItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color forceLineColor = JColorChooser.showDialog(GUI.panelCenter, sPickForceLineColor, Color.BLACK);
				listener.forceLineColorChosen(forceLineColor);
			}
		});
                  
        //menu language listeners
        polishItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				saveMenu.setText("Zapisz");
				saveAsPngItem.setText("jako png");
				saveItem.setText("jako charge sim file");
				newItem.setText("Nowy");
				openItem.setText("Otwórz");
				
				menuAdd.setText("Dodaj");
				chargeItem.setText("Ładunek");
				dipoleItem.setText("Dipol");
				quadrupoleItem.setText("Kwadrupol");
				
				menuLanguage.setText("Język");
				polishItem.setText("Polski");
				englishItem.setText("Angielski");
				
				menuShow.setText("Widok");
				equiShowItem.setText("Linie ekwipotencjalne");
				forceLineShowItem.setText("Linie sił pola");
				
				menuColor.setText("Kolory");
				backgroundColorItem.setText("Tło");
				equipotentialColorItem.setText("Linie ekwipotencjalne");
				fieldForceColorItem.setText("Linie sił pola");								
				
				sPickBackroundColor = "Wybierz kolor tła";
				sPickEquiColor = "Wybierz kolor linii ekwipotencjalnych";
				sPickForceLineColor = "Wybierz kolor linii sił pola";
				
				listener.polishItemClicked();								

			}
		});
        
        englishItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveMenu.setText("Save");
				saveAsPngItem.setText("as png");
				saveItem.setText("as charge sim file");
				newItem.setText("New");
				openItem.setText("Open");
				
				menuAdd.setText("Add");
				chargeItem.setText("Charge");
				dipoleItem.setText("Dipole");
				quadrupoleItem.setText("Quadrupole");
				
				menuLanguage.setText("Language");
				polishItem.setText("Polish");
				englishItem.setText("English");
				
				menuShow.setText("View");
				equiShowItem.setText("Equipotential lines");
				forceLineShowItem.setText("Field force lines");
				
				menuColor.setText("Colors");
				backgroundColorItem.setText("Background");
				equipotentialColorItem.setText("Equipotential lines");
				fieldForceColorItem.setText("Field force lines");
				
			    sPickBackroundColor = "Pick a background color";
			    sPickEquiColor = "Pick a isoline color";
			    sPickForceLineColor = "Pick a pick force line color";
				
				listener.englishItemClicked();
			}
		});
    }
}
