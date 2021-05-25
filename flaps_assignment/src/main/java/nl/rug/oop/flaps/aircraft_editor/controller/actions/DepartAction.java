package nl.rug.oop.flaps.aircraft_editor.controller.actions;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action is invoked to perform the actual departure of an aircraft from an
 * airport.
 *
 * Use the setEnabled() method to enable/disable the corresponding button
 *
 * @author T.O.W.E.R.
 */
@Log
public class DepartAction extends AbstractAction {
    private final WorldSelectionModel selectionModel;

    public DepartAction(WorldSelectionModel selectionModel) {
        super("Depart");
        this.selectionModel = selectionModel;
        // TODO: make sure the button gets enabled when the aircraft can depart
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        var sm = this.selectionModel; // Just to keep things succinct.
        if (sm.getSelectedAirport() != null && sm.getSelectedAircraft() != null && sm.getSelectedDestinationAirport() != null) {
            var aircraft = sm.getSelectedAircraft();
            if (aircraft.getType().getTakeoffClipPath() != null) {
                this.playTakeoffClip(aircraft.getType());
            }
            var start = sm.getSelectedAirport();
            var end = sm.getSelectedDestinationAirport();
            start.removeAircraft(aircraft);
            end.addAircraft(aircraft);
            sm.setSelectedAirport(end);
            sm.setSelectedDestinationAirport(null);
            sm.setSelectedAircraft(aircraft);
            aircraft.removeFuel(aircraft.getFuelConsumption(start, end));
        }
    }

    private void playTakeoffClip(AircraftType type) {
        new Thread(() -> {
            try (Clip clip = AudioSystem.getClip()) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(type.getTakeoffClipPath().toFile());
                clip.open(ais);
                clip.start();
                Thread.sleep((long) (ais.getFrameLength() / ais.getFormat().getFrameRate()) * 1000);
            } catch (Exception e) {
                log.warning("Could not play takeoff clip: " + e.getMessage());
            }
        }).start();
    }
}
