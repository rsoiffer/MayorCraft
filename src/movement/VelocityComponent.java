package movement;

import core.AbstractComponent;
import core.Vec2;

public class VelocityComponent extends AbstractComponent {

    public Vec2 vel;

    public VelocityComponent() {
        vel = new Vec2();
    }
}
