package world;

import core.Vec2;
import static world.GridComponent.SQUARE_SIZE;
import static world.World.WORLD_SIZE;

public class GridPoint {

    public final int x, y;

    public GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GridPoint(Vec2 v) {
        x = ((int) v.x + WORLD_SIZE) / SQUARE_SIZE;
        y = ((int) v.y + WORLD_SIZE) / SQUARE_SIZE;
    }

    public Vec2 toVec2() {
        return GridComponent.pos(x, y);
    }
}
