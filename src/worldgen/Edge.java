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
        for (Vec2 pos : noisePath) {
            if (rmc.potentiallyInView(pos, new Vec2(10, 10).multiply(Math.sqrt(water)))) {
                return true;
            }
        }
        return false;
    }
}
