package nl.rug.oop.flaps.aircraft_editor.model;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.controller.AircraftDataTracker;
import nl.rug.oop.flaps.aircraft_editor.controller.configcore.Configurator;
import nl.rug.oop.flaps.aircraft_editor.model.listeners.interfaces.BlueprintSelectionListener;
import nl.rug.oop.flaps.aircraft_editor.model.listeners.interfaces.CargoUnitsListener;
import nl.rug.oop.flaps.aircraft_editor.model.listeners.interfaces.FuelSupplyListener;
import nl.rug.oop.flaps.aircraft_editor.model.undomodel.UndoRedoManager;
import nl.rug.oop.flaps.aircraft_editor.view.maineditor.EditorFrame;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.areas.Compartment;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;
import nl.rug.oop.flaps.simulation.model.world.World;

/**
 * EditorCore class -  the core of the simulator; Stores the listenerIds,
 * initializes the configurator, aircraftDataTracker and other important model object classes;
 */
@Getter
@Setter
public class EditorCore implements CargoUnitsListener, BlueprintSelectionListener, FuelSupplyListener {
    private World world;
    private Aircraft aircraft;
    private final BlueprintSelectionModel bpSelectionModel;
    private AircraftLoadingModel aircraftLoadingModel;
    private Remapper remapper;
    private UndoRedoManager undoRedoManager;
    private EditorFrame editorFrame;
    private Configurator configurator;
    private CargoDatabase cargoDatabase;
    private AircraftDataTracker dataTracker;
    private GeographicCoordinates originCoordinates;
    private Airport source;
    private Airport destination;


    private static final double NEARBY_UNIT_RANGE = 250.0;
    public static final String generalListenerID = "000AREA_abs";
    public static final String cargoListenerID = "100CARGO_ml";
    public static final String fuelListenerID = "100FUEL_ml";
    public static final String engineListenerID = "200ENGINE_ml";

    public EditorCore(Aircraft aircraft, BlueprintSelectionModel bpSelectionModel, EditorFrame editorFrame) {
        this.world = aircraft.getWorld();
        this.aircraft = aircraft;
        this.editorFrame = editorFrame;
        this.bpSelectionModel = bpSelectionModel;
        this.bpSelectionModel.addListener(generalListenerID, this);
        this.bpSelectionModel.setEditorCore(this);
        init();
    }

    /**
     * Initialize Configurator,UndoRedoManager,AircraftDataTracker;
     * add editorCore as listener to the AircraftLoadingModel class object;
     */
    private void init() {
        setupRemapper();
        getRoute();
        initRemap();
        this.aircraftLoadingModel = editorFrame.getAircraftLoadingModel();
        aircraftLoadingModel.addListener((CargoUnitsListener) this);
        aircraftLoadingModel.addListener((FuelSupplyListener) this);
        this.dataTracker = new AircraftDataTracker(this, aircraft);
        this.bpSelectionModel.setDataTracker(dataTracker);
        this.aircraftLoadingModel.setDataTracker(dataTracker);
        this.undoRedoManager = new UndoRedoManager(this);
        this.configurator = new Configurator(this);
        undoRedoManager.setConfigurator(configurator);
    }

    /**
     * Setup the remapper and add it to the blueprintSelectionModel
     */
    private void setupRemapper() {
        this.remapper = new Remapper(this,bpSelectionModel);
        bpSelectionModel.setRemapper(remapper);
        bpSelectionModel.setAreasMap(remapper.getAreasMap());
    }

    /**
     * Collect the travel route details for the aircraft;
     */
    private void getRoute() {
        this.source = world.getSelectionModel().getSelectedAirport();
        this.destination = world.getSelectionModel().getSelectedDestinationAirport();
        if ((destination != null) && destination.canAcceptIncomingAircraft()) {
            this.originCoordinates = world.getSelectionModel().getSelectedAirport().getGeographicCoordinates();
        }
    }

    /**
     * init remapping methods for all compartment coordinates
     */
    private void initRemap() {
        remapper.updateCompartmentCoords();
        remapper.listToCoordsMap(this.aircraft.getType().getCargoAreas());
        remapper.listToCoordsMap(this.aircraft.getType().getFuelTanks());
        remapper.listToCoordsMap(this.aircraft.getType().getEngines());
        remapper.setMapBoundaries();
    }

    /**
     * All the methods below are required to execute the default updates;
     */
    @Override
    public void compartmentSelected(Compartment area, AircraftDataTracker dataTracker) {
        BlueprintSelectionListener.super.compartmentSelected(area, dataTracker);
    }

    @Override
    public void fireCargoTradeUpdate(AircraftDataTracker dataTracker) {
        CargoUnitsListener.super.fireCargoTradeUpdate(dataTracker);
    }

    @Override
    public void fireFuelSupplyUpdate(AircraftDataTracker dataTracker) {
        FuelSupplyListener.super.fireFuelSupplyUpdate(dataTracker);
    }
}

