package units;

import core.AbstractSystem;
import core.MouseInput;
import movement.PositionComponent;

public class DestinationSystem extends AbstractSystem {

    private PositionComponent position;
    private DestinationComponent destination;

    public DestinationSystem(PositionComponent position, DestinationComponent destination) {
        this.position = position;
        this.destination = destination;
    }

    public void update() {
        destination.des = MouseInput.mouse();

    }
}
