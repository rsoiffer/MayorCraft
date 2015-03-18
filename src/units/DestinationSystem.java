package units;

import core.AbstractSystem;
import core.MouseInput;
import movement.VelocityComponent;
import movement.PositionComponent;

public class DestinationSystem extends AbstractSystem {

    private PositionComponent pc;
    private VelocityComponent vc;
    private DestinationComponent dc;

    public DestinationSystem(PositionComponent position, VelocityComponent velocity, DestinationComponent destination) {
        this.pc = position;
        this.vc = velocity;
        this.dc=destination;
        
    }

    @Override
    public void update() {
        if (MouseInput.isDown(0)) {
            dc.des= MouseInput.mouse();
        }
        vc.vel = dc.des.subtract(pc.pos).setLength(2);
    }
    /*Calcualtes the distance, and makes speed movements in the x+y direction
     
     */
}
