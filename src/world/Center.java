package world;

import core.Color4d;
import core.Vec2;
import graphics.RenderManagerComponent;
import java.util.*;

public class Center {

    public Vec2 pos;
    public boolean isLand;
    public boolean isOnOcean;
    public boolean isBorder;
    public double elevation;
    public HashSet<Center> neighbors;
    public HashSet<Edge> borders;
    public TreeSet<Corner> corners;
    public Color4d color;
    public Vec2 LL;
    public Vec2 UR;

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

    public boolean inView(RenderManagerComponent rmc) {
        return LL.quadrant(rmc.UR()) == 1 && rmc.LL().quadrant(UR) == 1;
//        if (rmc.inView(pos)) {
//            return true;
//        }
//        if (!rmc.nearInView(pos, new Vec2(World.SIZE, World.SIZE).multiply(2 / Math.sqrt(World.POINTS)))) {
//            return false;
//        }
//        return true;
//        for (Corner c : corners) {
//            if (rmc.nearInView(c.pos, 100)) {
//                return true;
//            }
//        }
//        for (Edge e : borders) {
//            if (e.inView(rmc)) {
//                return true;
//            }
//        }
//        return false;
    }
}
