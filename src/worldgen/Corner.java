package worldgen;

import core.Vec2;

public class Corner {
    public Vec2 pos;
    public boolean border;
    public boolean isLand;
    public boolean isCoast;
    public double elevation;
    public Center[] touches;
    public Edge[] protrudes;
    public Corner[] adjacent;
    public double water;
    public Corner downslope;
}
