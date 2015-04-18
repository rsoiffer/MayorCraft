package world;

import core.AbstractComponent;
import core.Color4d;
import core.Main;
import core.Vec2;
import java.util.*;
import noise.Noise;
import voronoi.GraphEdge;
import voronoi.Voronoi;
import static world.GridComponent.GRID_SIZE;
import static world.GridComponent.SQUARE_SIZE;
import static world.World.*;

public class WorldComponent extends AbstractComponent {

    List<Center> centers = new ArrayList();
    List<Edge> edges = new ArrayList();
    List<Corner> corners = new ArrayList();
    ArrayList<ArrayList<Corner>> buckets = new ArrayList();

    public WorldComponent() {
        for (int i = 0; i <= BUCKETS; i++) {
            buckets.add(new ArrayList());
        }

        init();
    }

    private void assignElevations() {
        //Initialize elevation as 0 at the border, start flood fill
        Queue<Corner> q = new LinkedList();
        for (Corner c : corners) {
            if (c.isBorder) {
                c.elevation = 0;
                q.add(c);
            } else {
                c.elevation = Double.POSITIVE_INFINITY;
            }
        }
        //Flood fill through every Corner
        while (!q.isEmpty()) {
            Corner c = q.poll();
            for (Edge e : c.protrudes) {
                Corner n = e.v0;
                if (c == n) {
                    n = e.v1;
                }
                double newElevation = c.elevation + .000001;
                if (e.isLand) {
                    newElevation += c.pos.subtract(n.pos).length() + 10000 * RANDOM.nextDouble(); //Adding this randomness kills the seed - idk why
                }
                if (n.elevation > newElevation) {
                    n.elevation = newElevation;
                    n.downslope = c;
                    q.add(n);
                }
            }
        }
        //Redistribute heights to 0-1 range
        ArrayList<Corner> important = new ArrayList();
        for (Corner c : corners) {
            if (c.elevation > .1) {
                important.add(c);
            } else {
                c.elevation = 0;
            }
        }
        Collections.sort(important);
        for (int i = 0; i < important.size(); i++) {
            important.get(i).elevation = 1.1 * (1 - Math.sqrt(1 - (double) (i + 1) / important.size()));
        }
        Collections.sort(corners);
        //Set center elevations
        for (Center c : centers) {
            c.elevation = 0;
            for (Corner co : c.corners) {
                c.elevation += co.elevation;
            }
            c.elevation /= c.corners.size();
            if (c.isLand) {
                if (c.isOnOcean) {
                    c.color = new Color4d(.8, .8, .5, 1); //Beach
                } else {
                    c.color = new Color4d(.2, .8 - c.elevation * .7, 0, 1); //Land
                }
            } else {
                c.color = waterColor(c.elevation);
            }
        }
        //Order edges
        for (Edge e : edges) {
            if (e.v1.elevation > e.v0.elevation) {
                Corner c = e.v0;
                e.v0 = e.v1;
                e.v1 = c;
            }
        }
    }

    private void assignOceanLand() {
        //Create noise with seed
        Noise noise = new Noise(RANDOM.nextDouble() * 10000);
        for (Corner c : corners) {
            //Get random value between 0 and 1
            double r = noise.multi(c.pos.x, c.pos.y, 8, 2. / WORLD_SIZE) / 2 + .5;
            //Shape is determined by both random and distance
            if (r > c.pos.lengthSquared() / WORLD_SIZE / WORLD_SIZE) {
                c.isLand = true;
            }
        }
        //Determine if each polygon is land or water
        for (Center c : centers) {
            int landCount = 0;
            for (Corner co : c.corners) {
                if (co.isBorder) {
                    c.isBorder = true;
                }
                if (co.isLand) {
                    landCount++;
                }
            }
            c.isLand = landCount > c.corners.size() * .7 && !c.isBorder;
        }
        //Flood fill to find lakes
        Queue<Center> queue = new LinkedList();
        for (Center c : centers) {
            if (c.isBorder) {
                queue.add(c);
                break;
            }
        }
        while (!queue.isEmpty()) {
            Center c = queue.poll();
            for (Center n : c.neighbors) {
                if (!n.isOnOcean) {
                    n.isOnOcean = true;
                    if (!n.isLand) {
                        queue.add(n);
                    }
                }
            }
        }
        //Set the edges based on their polygons
        for (Edge e : edges) {
            e.isLand = e.p0.isLand && e.p1.isLand;
        }
        //Set the corners based on their polygons
        for (Corner c : corners) {
            int landCount = 0;
            for (Center ce : c.touches) {
                if (ce.isLand) {
                    landCount++;
                }
            }
            c.isLand = landCount > 0;
            if (c.isLand && landCount < c.touches.size()) {
                c.isCoast = true;
            }
        }
    }

    private void createNoisyLines() {
        //Create noisy lines
        for (Edge e : edges) {
            e.noisePath.add(e.v0.pos);
            e.noisePath.add(e.v1.pos);
//            Vec2 impt = e.p0.pos;
//            if ((centers.indexOf(e.p1) > centers.indexOf(e.p0) || e.p0.elevation == 0) && e.p1.elevation > 0) {
//                impt = e.p1.pos;
//            }
            if (e.water > 0) {
                subdivide(e.v0.pos, e.p0.pos, e.v1.pos, e.p1.pos, 20/* Math.sqrt(e.water)*/, e);
            } else if (e.p0.isLand != e.p1.isLand) {
                subdivide(e.v0.pos, e.p0.pos, e.v1.pos, e.p1.pos, 12, e);
            } else {
                subdivide(e.v0.pos, e.p0.pos, e.v1.pos, e.p1.pos, 12, e);
            }
        }
        //Define edge bounds
        for (Edge e : edges) {
            e.LL = e.UR = e.v0.pos;
            for (Vec2 v : e.noisePath) {
                if (v.x > e.UR.x) {
                    e.UR = e.UR.setX(v.x);
                }
                if (v.y > e.UR.y) {
                    e.UR = e.UR.setY(v.y);
                }
                if (v.x < e.LL.x) {
                    e.LL = e.LL.setX(v.x);
                }
                if (v.y < e.LL.y) {
                    e.LL = e.LL.setY(v.y);
                }
            }
        }
        //Define polygon bounds
        for (Center c : centers) {
            c.LL = c.UR = c.pos;
            for (Edge e : c.borders) {
                if (e.UR.x > c.UR.x) {
                    c.UR = c.UR.setX(e.UR.x);
                }
                if (e.UR.y > c.UR.y) {
                    c.UR = c.UR.setY(e.UR.y);
                }
                if (e.LL.x < c.LL.x) {
                    c.LL = c.LL.setX(e.LL.x);
                }
                if (e.LL.y < c.LL.y) {
                    c.LL = c.LL.setY(e.LL.y);
                }
            }
        }
        //Calculate blocked areas
        for (Center c : centers) {
            for (int i = ((int) c.LL.x + WORLD_SIZE) / SQUARE_SIZE; i < ((int) c.UR.x + WORLD_SIZE) / SQUARE_SIZE + 1; i++) {
                for (int j = ((int) c.LL.y + WORLD_SIZE) / SQUARE_SIZE; j < ((int) c.UR.y + WORLD_SIZE) / SQUARE_SIZE + 1; j++) {
                    if (i < GRID_SIZE && j < GRID_SIZE) {
                        GridPoint gp = Main.gameManager.gc.get(i, j);
                        if (c.contains(gp.toVec2())) {
                            gp.blocked = !c.isLand;
                            gp.c = c;
                            c.gridPoints.add(gp);
                        }
                    }
                }
            }
        }
        //Calculate river areas
        for (Edge e : edges) {
            if (e.water > 0) {
                for (int i = ((int) e.LL.x + WORLD_SIZE) / SQUARE_SIZE - (int) riverWidth(e.water); i < ((int) e.UR.x + WORLD_SIZE) / SQUARE_SIZE + 1 + (int) riverWidth(e.water); i++) {
                    for (int j = ((int) e.LL.y + WORLD_SIZE) / SQUARE_SIZE - (int) riverWidth(e.water); j < ((int) e.UR.y + WORLD_SIZE) / SQUARE_SIZE + 1 + (int) riverWidth(e.water); j++) {
                        if (i < GRID_SIZE && j < GRID_SIZE) {
                            GridPoint gp = Main.gameManager.gc.get(i, j);
                            if (e.contains(gp.toVec2())) {
                                gp.onRiver = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private void createPolygons() {
        //Initialize arrays
        double[] xs = new double[POINTS];
        double[] ys = new double[POINTS];
        //Choose random points
        for (int i = 0; i < POINTS; i++) {
            xs[i] = RANDOM.nextDouble() * WORLD_SIZE * 2 - WORLD_SIZE;
            ys[i] = RANDOM.nextDouble() * WORLD_SIZE * 2 - WORLD_SIZE;
        }
        //Calculate voronoi
        voronoi(xs, ys);
        //Smooth centers
        for (int i = 0; i < POINTS; i++) {
            Vec2 a = centers.get(i).average();
            xs[i] = a.x;
            ys[i] = a.y;
        }
        //Calculate voronoi
        voronoi(xs, ys);
        //Smooth centers
        for (int i = 0; i < POINTS; i++) {
            Vec2 a = centers.get(i).average();
            xs[i] = a.x;
            ys[i] = a.y;
        }
        //Calculate voronoi
        voronoi(xs, ys);
    }

    private void createRivers() {
        for (int i = 0; i < POINTS / 10; i++) {
            Corner c = corners.get((int) (corners.size() * RANDOM.nextDouble()));
            if (c.elevation < .3) {
                continue;
            }
            while (c.elevation > 0) {
                c.water++;
                c.edgeTo(c.downslope).water++;
                c = c.downslope;
            }
        }
    }

    private Corner getCorner(double x, double y) {
        for (Corner c : buckets.get((int) ((x + WORLD_SIZE) / 2 * BUCKETS / WORLD_SIZE))) {
            if (Math.abs(c.pos.x - x) < .0001 && Math.abs(c.pos.y - y) < .0001) {
                return c;
            }
        }
        Corner c = new Corner(x, y);
        corners.add(c);
        buckets.get((int) ((x + WORLD_SIZE) / 2 * BUCKETS / WORLD_SIZE)).add(c);
        return c;
    }

    void init() {
        //Polygons
        createPolygons();

        //Shape
        assignOceanLand();

        //Elevation
        assignElevations();

        //Rivers
        createRivers();

        //Noise
        createNoisyLines();
    }

    private void processGraphEdge(GraphEdge ge) {
        if (ge.x1 == ge.x2 && ge.y1 == ge.y2) {
            return;
        }
        //Find centers
        Center cen1 = centers.get(ge.site1);
        Center cen2 = centers.get(ge.site2);
        //Make edge
        Edge e = new Edge();
        edges.add(e);
        //Find or make corners - this is slow for some reason
        Corner cor1 = getCorner(ge.x1, ge.y1);
        Corner cor2 = getCorner(ge.x2, ge.y2);
        //Add to centers
        cen1.neighbors.add(cen2);
        cen1.borders.add(e);
        cen1.corners.add(cor1);
        cen1.corners.add(cor2);
        cen2.neighbors.add(cen1);
        cen2.borders.add(e);
        cen2.corners.add(cor1);
        cen2.corners.add(cor2);
        //Add to edge
        e.p0 = cen1;
        e.p1 = cen2;
        e.v0 = cor1;
        e.v1 = cor2;
        e.mid = cor1.pos.add(cor2.pos).multiply(.5);
        //Add to corners
        cor1.adjacent.add(cor2);
        cor1.protrudes.add(e);
        cor1.touches.add(cen1);
        cor1.touches.add(cen2);
        cor2.adjacent.add(cor1);
        cor2.protrudes.add(e);
        cor2.touches.add(cen1);
        cor2.touches.add(cen2);
    }

    private void subdivide(Vec2 a, Vec2 b, Vec2 c, Vec2 d, double minLength, Edge e) {
        if (a.subtract(c).lengthSquared() < minLength * minLength) {// || b.subtract(d).lengthSquared() < minLength * minLength) {
            return;
        }

        Vec2 hor = c.subtract(a).normalize();
        if (b.dot(hor) > c.dot(hor)) {
            b = b.interpolate(a, (c.dot(hor) - a.dot(hor)) / (b.dot(hor) - a.dot(hor)));
        }
        if (b.dot(hor) < a.dot(hor)) {
            b = b.interpolate(c, (a.dot(hor) - c.dot(hor)) / (b.dot(hor) - c.dot(hor)));
        }
        if (d.dot(hor) > c.dot(hor)) {
            d = d.interpolate(a, (c.dot(hor) - a.dot(hor)) / (d.dot(hor) - a.dot(hor)));
        }
        if (d.dot(hor) < a.dot(hor)) {
            d = d.interpolate(c, (a.dot(hor) - c.dot(hor)) / (d.dot(hor) - c.dot(hor)));
        }

        double bv = Math.abs(b.cross(hor) - a.cross(hor));
        double dv = Math.abs(d.cross(hor) - a.cross(hor));

        Vec2 p;
        double h;
        int count = 0;
        do {
            if (count++ > 100) {
                return;
            }
            h = .3 + .4 * RANDOM.nextDouble();
            double v = 1 - .6 * RANDOM.nextDouble() * Math.min(1, (c.dot(hor) - a.dot(hor)) / (bv + dv));
            if (RANDOM.nextDouble() * (bv + dv) - dv > 0) {
                p = a.interpolate(c, h).interpolate(b, v);
            } else {
                p = a.interpolate(c, h).interpolate(d, v);
            }
        } while (a.subtract(e.p0.pos).cross(p.subtract(e.p0.pos)) * e.noisePath.get(0).subtract(e.p0.pos).cross(e.noisePath.get(e.noisePath.size() - 1).subtract(e.p0.pos)) < 0
                || p.subtract(e.p0.pos).cross(c.subtract(e.p0.pos)) * e.noisePath.get(0).subtract(e.p0.pos).cross(e.noisePath.get(e.noisePath.size() - 1).subtract(e.p0.pos)) < 0
                || a.subtract(e.p1.pos).cross(p.subtract(e.p1.pos)) * e.noisePath.get(0).subtract(e.p1.pos).cross(e.noisePath.get(e.noisePath.size() - 1).subtract(e.p1.pos)) < 0
                || p.subtract(e.p1.pos).cross(c.subtract(e.p1.pos)) * e.noisePath.get(0).subtract(e.p1.pos).cross(e.noisePath.get(e.noisePath.size() - 1).subtract(e.p1.pos)) < 0);
//      while (p.dot(hor) > c.dot(hor) || p.dot(hor) < a.dot(hor));
//        while (a.interpolate(d, h).subtract(p).cross(b.subtract(p)) < 0 || a.interpolate(b, h).subtract(p).cross(d.subtract(p)) < 0);

        subdivide(a, a.interpolate(b, h), p, a.interpolate(d, h), minLength, e);
        e.noisePath.add(e.noisePath.size() - 1, p);
        subdivide(p, b.interpolate(c, h), c, d.interpolate(c, h), minLength, e);
//        //Subdivide the quadrilateral
//        double p = .3 + .4 * random.nextDouble(); // vertical (along A-D and B-C)
//        double q = .3 + .4 * random.nextDouble(); // horizontal (along A-B and D-C)
//        // Midpoints
//        Vec2 e = a.interpolate(d, p);
//        Vec2 f = b.interpolate(c, p);
//        Vec2 g = a.interpolate(b, q);
//        Vec2 i = d.interpolate(c, q);
//        // Central point
//        Vec2 h = e.interpolate(f, q);
//        // Divide the quad into subquads, but meet at H
//        double s = .6 + .8 * random.nextDouble();
//        double t = .6 + .8 * random.nextDouble();
//        subdivide(a, g.interpolate(b, s), h, e.interpolate(d, t), minLength, list);
//        list.add(h);
//        subdivide(h, f.interpolate(c, s), c, i.interpolate(d, t), minLength, list);
    }

    private void voronoi(double[] xs, double[] ys) {
        centers.clear();
        edges.clear();
        corners.clear();
        Voronoi v = new Voronoi(.1);
        List<GraphEdge> gel = v.generateVoronoi(xs, ys, -WORLD_SIZE, WORLD_SIZE, -WORLD_SIZE, WORLD_SIZE);
        //Create all centers
        for (int i = 0; i < POINTS; i++) {
            centers.add(new Center(xs[i], ys[i]));
        }
        for (GraphEdge ge : gel) {
            processGraphEdge(ge);
        }
    }
}
