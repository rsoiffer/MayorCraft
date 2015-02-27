package worldgen;

import core.Vec2;

public class Center {
    public Vec2 pos;
    public boolean isLand;
    public boolean isBorder;
    public double elevation;
    public Center[] neighbors;
    public Edge[] borders;
    public Corner[] corners;
}
