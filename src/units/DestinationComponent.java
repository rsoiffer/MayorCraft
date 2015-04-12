package units;

import core.AbstractComponent;
import core.Vec2;

public class DestinationComponent extends AbstractComponent {

    public Vec2 des;
    public boolean changed;

    public DestinationComponent() {
        this(new Vec2());
    }

    public DestinationComponent(Vec2 des) {
        this.des = des;
//        changed = true;
    }

}
