package world;

import core.Vec2;
import graphics.RenderManagerComponent;
import java.util.ArrayList;

public class Edge {

    public Center p0, p1;
    public Corner v0, v1;
    public Vec2 mid;
    public double water;
    public boolean isLand;
    public ArrayList<Vec2> noisePath = new ArrayList();
    public Vec2 LL;
    public Vec2 UR;

    public boolean contains(Vec2 pos) {
        if (water == 0) {
            return false;
        }
        double widthSq = World.riverWidth(water) * World.riverWidth(water);
        for (Vec2 v : noisePath) {
            if (v.subtract(pos).lengthSquared() < widthSq) {
                return true;
            }
        }
        return false;
    }

    public boolean inView(RenderManagerComponent rmc) {
        Vec2 buffer = new Vec2(1, 1).multiply(World.riverWidth(water));
        return LL.subtract(buffer).quadrant(rmc.UR()) == 1 && rmc.LL().quadrant(UR.add(buffer)) == 1;
    }
}
