package movement;

import core.AbstractComponent;
import core.Vec2;

public class GravityComponent extends AbstractComponent {

    public Vec2 g;

    public GravityComponent() {
        g = new Vec2(0, -.6);
    }
}
