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

    public boolean inView(RenderManagerComponent rmc) {
        Vec2 buffer = new Vec2(1, 1).multiply(World.riverWidth(water));
        return LL.subtract(buffer).quadrant(rmc.UR()) == 1 && rmc.LL().quadrant(UR.add(buffer)) == 1;

//        return rmc.nearInView(p0.pos, new Vec2(World.SIZE, World.SIZE).multiply(2 / Math.sqrt(World.POINTS)))
//                || rmc.nearInView(p1.pos, new Vec2(World.SIZE, World.SIZE).multiply(2 / Math.sqrt(World.POINTS)));
//        for (Vec2 pos : noisePath) {
//            if (rmc.nearInView(pos, new Vec2(1, 1).multiply(World.riverWidth(water)))) {
//                return true;
//            }
//        }
//        return false;
    }
}
