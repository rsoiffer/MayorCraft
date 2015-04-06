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

    public boolean contains(Vec2 v) {
        if (!v.containedBy(LL, UR)) {
            return false;
        }
//        for (Edge e : borders) {
//            if (e.v1.pos.subtract(e.v0.pos).cross(pos.subtract(e.v0.pos)) * e.v1.pos.subtract(e.v0.pos).cross(this.pos.subtract(e.v0.pos)) < 0) {
//                return false;
//            }
//        }
        for (Edge e : borders) {
            double dir = e.v1.pos.subtract(e.v0.pos).cross(pos.subtract(e.v0.pos)); //We only need the +-
            Vec2 toCenter = v.subtract(pos);
            //If you're in the correct big triangle
            if (toCenter.cross(e.v0.pos.subtract(pos)) * dir < 0 && toCenter.cross(e.v1.pos.subtract(pos)) * dir > 0) {
                for (int i = 0; i < e.noisePath.size() - 1; i++) {
                    Vec2 v0 = e.noisePath.get(i);
                    Vec2 v1 = e.noisePath.get(i + 1);
                    //If you're in the correct small triangle
                    if (toCenter.cross(v0.subtract(pos)) * dir < 0 && toCenter.cross(v1.subtract(pos)) * dir > 0) {
                        return v1.subtract(v0).cross(v.subtract(v0)) * dir > 0;
                    }
                }
            }
        }
        return true;
    }

    public boolean inView(RenderManagerComponent rmc) {
        return LL.quadrant(rmc.UR()) == 1 && rmc.LL().quadrant(UR) == 1;
    }
}
