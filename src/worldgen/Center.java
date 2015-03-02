package worldgen;

import core.Vec2;
import java.util.*;

public class Center {

    public Vec2 pos;
    public boolean isLand;
    public boolean isBorder;
    public double elevation;
    public HashSet<Center> neighbors;
    public HashSet<Edge> borders;
    public TreeSet<Corner> corners;

    Center(double x, double y) {
        pos = new Vec2(x, y);
        neighbors = new HashSet();
        borders = new HashSet();
        corners = new TreeSet(new Comparator() {

            @Override
            public int compare(Object t, Object t1) {
                Corner c1 = (Corner) t;
                Corner c2 = (Corner) t1;
                return (int) Math.signum(c1.pos.subtract(pos).direction() - c2.pos.subtract(pos).direction());
            }

        });
    }

    Vec2 average() {
        Vec2 r = new Vec2();
        for (Corner c : corners) {
            r = r.add(c.pos);
        }
        return r.multiply(1. / corners.size());
    }

    void fix() {
        elevation = 0;
        for (Corner c : corners) {
            elevation += c.elevation;
        }
        elevation /= corners.size();
    }
}
