package worldgen;

import core.Vec2;
import java.util.*;

public class Center {

    public Vec2 pos;
    public boolean isLand;
    public boolean isBorder;
    public double elevation;
    public ArrayList<Center> neighbors;
    public ArrayList<Edge> borders;
    public ArrayList<Corner> corners;

    Center(double x, double y) {
        pos = new Vec2(x, y);
        neighbors = new ArrayList();
        borders = new ArrayList();
        corners = new ArrayList();
    }

    Vec2 average() {
        Vec2 r = new Vec2();
        for (Corner c : corners) {
            r = r.add(c.pos);
        }
        return r.multiply(1 / corners.size());
    }

    void fix() {
        elevation = 0;
        for (Corner c : corners) {
            elevation += c.elevation;
        }
        elevation /= corners.size();
        Collections.sort(corners, new Comparator() {

            @Override
            public int compare(Object t, Object t1) {
                Corner c1 = (Corner) t;
                Corner c2 = (Corner) t1;
                return (int) Math.signum(c1.pos.subtract(pos).direction() - c2.pos.subtract(pos).direction());
            }

        });
        int landCount = 0;
        for (Corner c : corners) {
            if (c.isBorder) {
                isBorder = true;
            }
            if (c.isLand) {
                landCount++;
            }
        }
        isLand = landCount > corners.size() * .7;
    }
}
