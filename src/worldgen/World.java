package worldgen;

import core.AbstractEntity;
import core.Vec2;
import java.util.*;
import noise.Noise;
import voronoi.GraphEdge;
import voronoi.Voronoi;

public class World extends AbstractEntity {

    public static final int POINTS = 10000;
    private static final int BUCKETS = POINTS / 10;
    public static final int SIZE = 500;
    List<Center> centers = new ArrayList();
    List<Edge> edges = new ArrayList();
    List<Corner> corners = new ArrayList();
    ArrayList<ArrayList<Corner>> buckets = new ArrayList();

    public World() {
        init();
        //Systems
        add(new WorldSystem(this));
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
//                if (e.v0 == e.v1) {
//                    System.out.println("bad");
//                }
                double newElevation = c.elevation + .001;
                if (e.isLand) {
                    newElevation += 10000 * Math.random() + c.pos.subtract(n.pos).length();
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
            }
        }
        Collections.sort(important);
        for (int i = 0; i < important.size(); i++) {
            important.get(i).elevation = 1.1 * (1 - Math.sqrt(1 - (double) i / important.size()));
        }
        Collections.sort(corners);
        //Set center elevations
        for (Center c : centers) {
            c.elevation = 0;
            for (Corner co : c.corners) {
                c.elevation += co.elevation;
            }
            c.elevation /= c.corners.size();
        }
    }

    private void assignOceanLand() {
        //Create noise with seed
        Noise noise = new Noise(Math.random() * 1000);
        for (Corner c : corners) {
            //Get random value between 0 and 1
            double r = noise.multi(c.pos.x, c.pos.y, 8, .004) / 2 + .5;
            //Shape is determined by both random and distance
            if (r > c.pos.lengthSquared() / SIZE / SIZE) {
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
        for (Edge e : edges) {
            e.noisePath.add(e.v0.pos);
            if (e.water > 0) {
                subdivide(e.v0.pos, e.p0.pos, e.v1.pos, e.p1.pos, 4 * Math.sqrt(e.water), e.noisePath);
            } else if (e.p0.isLand != e.p1.isLand) {
                subdivide(e.v0.pos, e.p0.pos, e.v1.pos, e.p1.pos, 2, e.noisePath);
            } else {
                subdivide(e.v0.pos, e.p0.pos, e.v1.pos, e.p1.pos, 8, e.noisePath);
            }
            e.noisePath.add(e.v1.pos);
        }
    }

    private void createPolygons() {
        //Initialize arrays
        double[] xs = new double[POINTS];
        double[] ys = new double[POINTS];
        //Choose random points
        for (int i = 0; i < POINTS; i++) {
            xs[i] = Math.random() * SIZE * 2 - SIZE;
            ys[i] = Math.random() * SIZE * 2 - SIZE;
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
        for (int i = 0; i < SIZE / 2; i++) {
            Corner c = corners.get((int) (corners.size() * Math.random()));
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
        for (Corner c : buckets.get((int) ((x + SIZE) / 2 * BUCKETS / SIZE))) {
            if (Math.abs(c.pos.x - x) < .0001 && Math.abs(c.pos.y - y) < .0001) {
                return c;
            }
        }
        Corner c = new Corner(x, y);
        corners.add(c);
        buckets.get((int) ((x + SIZE) / 2 * BUCKETS / SIZE)).add(c);
        return c;
    }

    private void init() {
        for (int i = 0; i <= BUCKETS; i++) {
            buckets.add(new ArrayList());
        }

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

    private void subdivide(Vec2 a, Vec2 b, Vec2 c, Vec2 d, double minLength, ArrayList<Vec2> list) {
        if (a.subtract(c).lengthSquared() < minLength * minLength || b.subtract(d).lengthSquared() < minLength * minLength) {
            return;
        }
        // Subdivide the quadrilateral
        double p = .2 + .6 * Math.random(); // vertical (along A-D and B-C)
        double q = .2 + .6 * Math.random(); // horizontal (along A-B and D-C)
        // Midpoints
        Vec2 e = a.interpolate(d, p);
        Vec2 f = b.interpolate(c, p);
        Vec2 g = a.interpolate(b, q);
        Vec2 i = d.interpolate(c, q);
        // Central point
        Vec2 h = e.interpolate(f, q);
        // Divide the quad into subquads, but meet at H
        double s = .6 + .8 * Math.random();
        double t = .6 + .8 * Math.random();
        subdivide(a, g.interpolate(b, s), h, e.interpolate(d, t), minLength, list);
        list.add(h);
        subdivide(h, f.interpolate(c, s), c, i.interpolate(d, t), minLength, list);
    }

    private void voronoi(double[] xs, double[] ys) {
        centers.clear();
        edges.clear();
        corners.clear();
        Voronoi v = new Voronoi(.1);
        List<GraphEdge> gel = v.generateVoronoi(xs, ys, -SIZE, SIZE, -SIZE, SIZE);
        //Create all centers
        for (int i = 0; i < POINTS; i++) {
            centers.add(new Center(xs[i], ys[i]));
        }
        for (GraphEdge ge : gel) {
            processGraphEdge(ge);
        }
    }
}
