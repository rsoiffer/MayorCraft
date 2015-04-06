package world;

import core.Vec2;
import java.util.HashSet;
import static world.World.WORLD_SIZE;

public class Corner implements Comparable {

    public Vec2 pos;
    public boolean isBorder;
    public boolean isLand;
    public boolean isCoast;
    public double elevation;
    public HashSet<Center> touches;
    public HashSet<Edge> protrudes;
    public HashSet<Corner> adjacent;
    public double water;
    public Corner downslope;
    public Corner watershed;

    Corner(double x, double y) {
        pos = new Vec2(x, y);
        isBorder = Math.abs(x) == WORLD_SIZE || Math.abs(y) == WORLD_SIZE;
        touches = new HashSet();
        protrudes = new HashSet();
        adjacent = new HashSet();
    }

    @Override
    public int compareTo(Object t) {
        if (t instanceof Corner) {
            Corner c = (Corner) t;
            if (c.elevation < elevation) {
                return 1;
            }
            if (c.elevation > elevation) {
                return -1;
            }
        }
        return 0;
    }

    public Edge edgeTo(Corner c) {
        for (Edge e : protrudes) {
            if (e.v0 == c || e.v1 == c) {
                return e;
            }
        }
        return null;
    }
}
