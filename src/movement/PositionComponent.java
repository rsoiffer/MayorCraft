package movement;

import core.AbstractComponent;
import core.Vec2;

public class PositionComponent extends AbstractComponent {

    public Vec2 pos;

    public PositionComponent() {
        this(new Vec2());
    }

    public PositionComponent(Vec2 pos) {
        this.pos = pos;
    }

}
