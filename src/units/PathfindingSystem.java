package units;

import core.AbstractSystem;
import core.Main;
import core.Vec2;
import static java.lang.Double.POSITIVE_INFINITY;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import movement.PositionComponent;
import movement.VelocityComponent;
import static world.GridComponent.GRID_SIZE;
import static world.GridComponent.SQUARE_SIZE;
import world.GridPoint;
import static world.World.WORLD_SIZE;

public class PathfindingSystem extends AbstractSystem {

    private PositionComponent pc;
    private VelocityComponent vc;
    private DestinationComponent dc;
    private PathfindingComponent pac;
    private SelectableComponent sc;

    public PathfindingSystem(PositionComponent pc, VelocityComponent vc, DestinationComponent dc, PathfindingComponent pac, SelectableComponent sc) {
        this.pc = pc;
        this.vc = vc;
        this.dc = dc;
        this.pac = pac;
        this.sc = sc;
    }

    @Override
    public void update() {
        if (dc.changed) {
            pac.path = findPath(dc.des, pc.pos);
            dc.changed = false;
        }
        if (pac.path != null && !pac.path.isEmpty()) {
            while (pac.path.size() >= 2 && visible(pc.pos, pac.path.get(1))) {
                pac.path.remove(0);
            }
            Vec2 target = pac.path.get(0);
//            Graphics.drawLine(pc.pos, target, Color4d.RED, 1);
//            for (int i = 0; i < pac.path.size() - 1; i++) {
//                Graphics.drawLine(pac.path.get(i), pac.path.get(i + 1), Color4d.RED, 1);
//            }
            if (!visible(pc.pos, target)) {
                ArrayList<Vec2> list = findPath(target, pc.pos);
                if (list==null){
                    return;
                }
                for (int i = list.size() - 1; i > 0; i--) {
                    pac.path.add(0, list.get(i));
                }
                //return;
            }
            if (target.subtract(pc.pos).lengthSquared() > 4) {
                vc.vel = target.subtract(pc.pos).setLength(4);
            } else {
                vc.vel = target.subtract(pc.pos);
                pac.path.remove(0);
            }
        } else {
            Vec2 target = dc.des;
            if (target.subtract(pc.pos).lengthSquared() > 4) {
                vc.vel = target.subtract(pc.pos).setLength(4);
            } else {
                vc.vel = target.subtract(pc.pos);
            }
        }
    }

    private class PointData implements Comparable {

        GridPoint point;
        PointData parent;
//        Vec2 parentV;
        double distance;
        double priority;

        PointData(GridPoint point) {
            this.point = point;
            distance = POSITIVE_INFINITY;
        }

        @Override
        public int compareTo(Object other) {
            return (int) Math.signum(priority - ((PointData) other).priority);
        }

        @Override
        public boolean equals(Object other) {
            return ((PointData) other).point == point;
        }
    }

    private boolean blocked(GridPoint gp) {
        return gp.blocked;
    }

    private ArrayList<Vec2> findPath(Vec2 startV, Vec2 goalV) {
        GridPoint start = Main.gameManager.gc.get(startV);
        GridPoint goal = Main.gameManager.gc.get(goalV);
        if (blocked(start) || blocked(goal)) {
            return null;
        }

        PriorityQueue<PointData> open = new PriorityQueue();
        HashSet<GridPoint> closed = new HashSet();
        HashMap<GridPoint, PointData> dataMap = new HashMap();

        PointData startData = new PointData(start);
        startData.distance = 0;
        startData.parent = startData;
//        startData.parentV = startV;

        open.add(startData);
        dataMap.put(start, startData);

        while (!open.isEmpty()) {
//            System.out.println("1");
            PointData pd = open.poll();
            if (pd.point == goal) {
                ArrayList<Vec2> r = new ArrayList();
                r.add(pd.point.toVec2());
//                r.add(pd.parentV);
                while (pd.parent != pd) {
//                    System.out.println("2");
                    pd = pd.parent;
                    r.add(pd.point.toVec2());
                }
                r.add(startV);
                return r;
            }
            closed.add(pd.point);
            for (GridPoint n : neighbors(pd.point)) {
                if (!closed.contains(n)) {
                    PointData nd = dataMap.get(n);
                    if (nd == null) {
                        nd = new PointData(n);
                        dataMap.put(n, nd);
                    }
                    if (visible(pd.parent.point.toVec2(), n.toVec2())) {
                        if (pd.parent.distance + pd.parent.point.distanceTo(n) < nd.distance) {
                            nd.parent = pd.parent;
//                            nd.parentV = pd.parentV;
                            nd.distance = pd.parent.distance + pd.parent.point.distanceTo(n);
                            if (open.contains(n)) {
                                open.remove(n);
                            }
                            nd.priority = nd.distance + n.distanceTo(goal);
                            open.add(nd);
                        }
                    } else {
                        if (pd.distance + pd.point.distanceTo(n) < nd.distance) {
                            nd.parent = pd;
//                            nd.parentV = pd.point.toVec2();
                            nd.distance = pd.distance + pd.point.distanceTo(n);
                            if (open.contains(n)) {
                                open.remove(n);
                            }
                            nd.priority = nd.distance + n.distanceTo(goal);
                            open.add(nd);
                        }
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<GridPoint> neighbors(GridPoint gp) {
        ArrayList<GridPoint> r = new ArrayList();
        for (int i = gp.x - 1; i <= gp.x + 1; i++) {
            for (int j = gp.y - 1; j <= gp.y + 1; j++) {
                if (i != gp.x || j != gp.y) {
                    if (i >= 0 && i < GRID_SIZE && j >= 0 && j < GRID_SIZE) {
                        if (visible(gp.toVec2(), Main.gameManager.gc.get(i, j).toVec2())) {
                            r.add(Main.gameManager.gc.get(i, j));
                        }
                    }
                }
            }
        }
        return r;
    }

    private boolean visible(Vec2 v0, Vec2 v1) {
        Vec2 width;
        if (v0.quadrant(v1) % 2 == 1) {
            width = new Vec2(sc.size, -sc.size);
        } else {
            width = new Vec2(sc.size, sc.size);
        }
//        width = width.multiply(1.1);
//        Vec2 width = v1.subtract(v0).setLength(sc.size * Math.sqrt(2)).normal();
        return visibleVec(v0.add(width), v1.add(width)) && visibleVec(v0.subtract(width), v1.subtract(width));
    }

    private boolean visibleVec(Vec2 v0, Vec2 v1) {
        double x0 = (v0.x + WORLD_SIZE) / SQUARE_SIZE;
        double y0 = (v0.y + WORLD_SIZE) / SQUARE_SIZE;
        double x1 = (v1.x + WORLD_SIZE) / SQUARE_SIZE;
        double y1 = (v1.y + WORLD_SIZE) / SQUARE_SIZE;
        double dx = Math.abs(x1 - x0);
        double dy = Math.abs(y1 - y0);

        int x = (int) Math.floor(x0);
        int y = (int) Math.floor(y0);

        int n = 1;
        int x_inc, y_inc;
        double error;

        if (dx == 0) {
            x_inc = 0;
            error = Double.POSITIVE_INFINITY;
        } else if (x1 > x0) {
            x_inc = 1;
            n += Math.floor(x1) - x;
            error = (Math.floor(x0) + 1 - x0) * dy;
        } else {
            x_inc = -1;
            n += x - Math.floor(x1);
            error = (x0 - Math.floor(x0)) * dy;
        }

        if (dy == 0) {
            y_inc = 0;
            error = Double.NEGATIVE_INFINITY;
        } else if (y1 > y0) {
            y_inc = 1;
            n += Math.floor(y1) - y;
            error -= (Math.floor(y0) + 1 - y0) * dx;
        } else {
            y_inc = -1;
            n += y - Math.floor(y1);
            error -= (y0 - Math.floor(y0)) * dx;
        }

        for (; n > 0; --n) {
            if (blocked(Main.gameManager.gc.get(x, y))) {
                return false;
            }

            if (error > 0) {
                y += y_inc;
                error -= dx;
            } else {
                x += x_inc;
                error += dy;
            }
        }
        return true;
    }

    private boolean visibleInt(GridPoint gp1, GridPoint gp2) {
        int x0 = gp1.x;
        int y0 = gp1.y;
        int x1 = gp2.x;
        int y1 = gp2.y;
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int x = x0;
        int y = y0;
        int n = 1 + dx + dy;
        int x_inc = (x1 > x0) ? 1 : -1;
        int y_inc = (y1 > y0) ? 1 : -1;
        int error = dx - dy;
        dx *= 2;
        dy *= 2;

        for (; n > 0; --n) {
            if (blocked(Main.gameManager.gc.get(x, y))) {
                return false;
            }

            if (error > 0) {
                x += x_inc;
                error -= dy;
            } else {
                y += y_inc;
                error += dx;
            }
        }
        return true;
    }
}
