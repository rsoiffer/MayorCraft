package worldgen;

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

    public boolean inView(RenderManagerComponent rmc) {
        if (!rmc.potentiallyInView(p0.pos, new Vec2(World.SIZE, World.SIZE).multiply(2 / Math.sqrt(World.POINTS)))) {
            return false;
        }
        if (!rmc.potentiallyInView(p1.pos, new Vec2(World.SIZE, World.SIZE).multiply(2 / Math.sqrt(World.POINTS)))) {
            return false;
        }
//        for (Vec2 pos : noisePath) {
//            if (rmc.potentiallyInView(pos, new Vec2(50, 50))) {
//                return true;
//            }
//        }
        return true;
        //return false;
    }
}
