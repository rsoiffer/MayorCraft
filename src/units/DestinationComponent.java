package units;

import core.AbstractComponent;
import core.Vec2;
import java.util.ArrayList;

public class DestinationComponent extends AbstractComponent {

    public Vec2 des;
    public ArrayList<Vec2> path = new ArrayList();

    public DestinationComponent() {
        this(new Vec2());
    }

    public DestinationComponent(Vec2 des) {
        this.des = des;
    }

}
