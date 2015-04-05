package units;

import core.AbstractSystem;
import movement.PositionComponent;
import movement.VelocityComponent;

public class DestinationSystem extends AbstractSystem {

    private PositionComponent pc;
    private VelocityComponent vc;
    private DestinationComponent dc;

    public DestinationSystem(PositionComponent position, VelocityComponent velocity, DestinationComponent destination) {
        this.pc = position;
        this.vc = velocity;
        this.dc = destination;

    }

    @Override
    public void update() {
//        if (MouseInput.isDown(0)) {
//            dc.des = MouseInput.mouse();
//        }
        if (dc.des.subtract(pc.pos).lengthSquared() > 4) {
            vc.vel = dc.des.subtract(pc.pos).setLength(4);
        } else {
            vc.vel = dc.des.subtract(pc.pos);
        }
    }
    /*Calcualtes the distance, and makes speed movements in the x+y direction

     */
}
