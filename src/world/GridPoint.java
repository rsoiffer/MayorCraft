package world;

import buildings.Building;
import core.Vec2;

public class GridPoint {

    public final int x, y;
    public Center c;
    public Building building;
    public Terrain terrain;
    public boolean blocked;
    public boolean onRiver;

    GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(GridPoint other) {
        return new Vec2(x - other.x, y - other.y).length();
    }

    public Vec2 toVec2() {
        return GridComponent.pos(x, y);
    }
}
