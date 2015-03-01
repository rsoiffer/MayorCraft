package worldgen;

import core.Vec2;
import java.util.ArrayList;

public class Corner implements Comparable {

    public Vec2 pos;
    public boolean isBorder;
    public boolean isLand;
    public boolean isCoast;
    public double elevation;
    public ArrayList<Center> touches;
    public ArrayList<Edge> protrudes;
    public ArrayList<Corner> adjacent;
    public double water;
    public Corner downslope;

    Corner(double x, double y) {
        pos = new Vec2(x, y);
        isBorder = Math.abs(x) == World.SIZE || Math.abs(y) == World.SIZE;
        touches = new ArrayList();
        protrudes = new ArrayList();
        adjacent = new ArrayList();
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
}
