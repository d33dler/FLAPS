package nl.rug.oop.flaps.simulation.model.cargo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoFreight {
    private String id;
    private CargoType cargoType;
    private int unitCount;
    private double totalWeight;

    public CargoFreight(CargoType type, int unitCount, double totalWeight, String id) {
        this.cargoType = type;
        this.unitCount = unitCount;
        this.totalWeight = totalWeight;
        this.id = id;
    }

    public CargoFreight() {
    }
}
