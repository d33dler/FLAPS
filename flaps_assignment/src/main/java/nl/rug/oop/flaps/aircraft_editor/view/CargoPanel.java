package nl.rug.oop.flaps.aircraft_editor.view;

import nl.rug.oop.flaps.aircraft_editor.controller.actions.BlueprintSelectionListener;
import nl.rug.oop.flaps.aircraft_editor.model.EditorCore;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.aircraft.Compartment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CargoPanel extends JPanel implements BlueprintSelectionListener {
    private EditorCore editorCore;
    private SettingsPanel settingsPanel;
    private JButton exCargoLoader;
    private final static String listenerId = EditorCore.cargoListenerID;

    public CargoPanel(EditorCore editorCore, SettingsPanel settingsPanel) {
        this.editorCore = editorCore;
        this.settingsPanel = settingsPanel;
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(1000, 120));
        editorCore.getSelectionModel().addListener(listenerId, this);
        addCargoLoaderButton();
    }

    private void addCargoLoaderButton() {
        this.exCargoLoader = new JButton("Load cargo");
        exCargoLoader.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() ->
                        settingsPanel.setCargoSettings(
                                new CargoSettings(editorCore, (CargoArea) settingsPanel.getCompartmentArea())));
            }
        });
        add(exCargoLoader);
        setVisible(false);
    }

    @Override
    public void compartmentSelected(Compartment area) {
        editorCore.getConfigurator().updateDatabaseTables(settingsPanel);
        editorCore.getConfigurator().setCargoAreaLoad();
        settingsPanel.getFuelPanel().setVisible(false);
        setVisible(true);
    }
}
