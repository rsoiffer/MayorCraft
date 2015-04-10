package units;

import core.AbstractSystem;
import core.Main;
import core.Vec2;
import static java.lang.Double.POSITIVE_INFINITY;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import static world.GridComponent.GRID_SIZE;
import static world.GridComponent.SQUARE_SIZE;
import world.GridPoint;

public class PathfindingSystem extends AbstractSystem {

    private SelectableComponent sc;

    @Override
    public void update() {
    }

    private class Point implements Comparable {

        GridPoint point;
        Point parent;
        double distance;
        double priority;
        
        @Override
        public int compareTo(Object other) {
            return (int)Math.signum(priority - ((Point)other).priority);
        }
    }

    public ArrayList<GridPoint> findPath(GridPoint start, GridPoint goal) {
        PriorityQueue<GridPoint> open = new PriorityQueue();
        HashSet<GridPoint> closed = new HashSet();
        HashMap<GridPoint, GridPoint> parents = new HashMap();
        HashMap<GridPoint, Double> distances = new HashMap();
        parents.put(start, start);
        distances.put(start, 0.);
        open.add(start, 0.);
        while (!open.isEmpty()) {
            GridPoint gp = open.poll();
            if (gp == goal) {
                ArrayList<GridPoint> r = new ArrayList();
                while (parents.get(gp) != gp) {
                    r.add(gp);
                    gp = parents.get(gp);
                }
                return r;
            }
            closed.add(gp);
            for (GridPoint n : neighbors(gp)) {
                if (!closed.contains(n)) {
                    if (!open.contains(n)) {
                        distances.put(n, POSITIVE_INFINITY);
                        parents.put(n, null);
                    }
                    if (distances.get(gp) + gp.distanceTo(n) < distances.get(n)) {
                        parents.put(n, gp);
                        distances.put(n, distances.get(gp) + gp.distanceTo(n));
                        if (open.contains(n)) {
                            open.remove(n);
                        }
                        open.add(n, distances.get(n) + n.distanceTo(goal));
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
                        if (visible(gp, Main.gameManager.gc.get(i, j))) {
                            r.add(Main.gameManager.gc.get(i, j));
                        }
                    }
                }
            }
        }
        return r;
    }

    private boolean visible(GridPoint gp1, GridPoint gp2) {
        Vec2 width = gp2.toVec2().subtract(gp1.toVec2()).setLength(sc.size).normal();
        return visibleVec(gp1.toVec2().add(width), gp2.toVec2().add(width)) && visibleVec(gp1.toVec2().subtract(width), gp2.toVec2().subtract(width));
    }

    private boolean visibleVec(Vec2 v0, Vec2 v1) {
        double x0 = v0.x / SQUARE_SIZE;
        double y0 = v0.y / SQUARE_SIZE;
        double x1 = v1.x / SQUARE_SIZE;
        double y1 = v1.y / SQUARE_SIZE;
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
            if (Main.gameManager.gc.get(x, y).blocked) {
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
            if (Main.gameManager.gc.get(x, y).blocked) {
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
