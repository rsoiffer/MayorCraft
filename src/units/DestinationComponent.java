package units;

import buildings.Building;
import core.AbstractComponent;
import core.Vec2;
import world.GridPoint;

public class DestinationComponent extends AbstractComponent {

    public Vec2 des;
    public boolean changed;
    public boolean atDest;
    public Building building;
    public GridPoint terrain;

    public DestinationComponent(Vec2 des) {
        this.des = des;
    }
}
