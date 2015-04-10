package units;

import core.AbstractSystem;
import core.Main;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import static world.GridComponent.GRID_SIZE;
import world.GridPoint;

public class PathfindingSystem extends AbstractSystem {

    @Override
    public void update() {
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
                        distances.put(n, distances.get(gp) + gp.distanceTo(n);
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
                        if (visible(gp, Main.gameManager.gc.get(i, j))   {
                            r.add(Main.gameManager.gc.get(i, j));
                        }
                    }
                }
            }
        }
        return r;
    }

    private boolean visible(GridPoint gp1, GridPoint gp2) {
        double x0 = gp1.toVec2().x;
        double y0 = gp1.toVec2().y;
        double x1 = gp2.toVec2().x;
        double y1 = gp2.toVec2().y;

        double dx = Math.abs(x1 - x0);
        double dy = Math.abs(y1 - y0);

        int x = (int) Math.floor(x0);
        int y = (int) Math.floor(y0);

        double dt_dx = 1.0 / dx;
        double dt_dy = 1.0 / dy;

        double t = 0;

        int n = 1;
        int x_inc, y_inc;
        double t_next_vertical, t_next_horizontal;

        if (dx == 0) {
            x_inc = 0;
            t_next_horizontal = dt_dx; // infinity
        } else if (x1 > x0) {
            x_inc = 1;
            n += int
            (floor(x1)) - x;
            t_next_horizontal = (floor(x0) + 1 - x0) * dt_dx;
        } else {
            x_inc = -1;
            n += x - int
            (floor(x1)
            );
t_next_horizontal = (x0 - floor(x0)) * dt_dx;
        }

        if (dy == 0) {
            y_inc = 0;
            t_next_vertical = dt_dy; // infinity
        } else if (y1 > y0) {
            y_inc = 1;
            n += int
            (floor(y1)) - y;
            t_next_vertical = (floor(y0) + 1 - y0) * dt_dy;
        } else {
            y_inc = -1;
            n += y - int
            (floor(y1)
            );
t_next_vertical = (y0 - floor(y0)) * dt_dy;
        }

        for (; n > 0; --n) {
            visit(x, y);

            if (t_next_vertical < t_next_horizontal) {
                y += y_inc;
                t = t_next_vertical;
                t_next_vertical += dt_dy;
            } else {
                x += x_inc;
                t = t_next_horizontal;
                t_next_horizontal += dt_dx;
            }
        }
    }

}
