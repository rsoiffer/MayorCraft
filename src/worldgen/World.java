package worldgen;

import core.AbstractEntity;
import core.Vec2;
import java.util.*;
import noise.Noise;
import voronoi.GraphEdge;
import voronoi.Voronoi;

public class World extends AbstractEntity {

    public static final int POINTS = 2000;
    public static final int SIZE = 500;
    List<Center> centers = new ArrayList();
    List<Edge> edges = new ArrayList();
    List<Corner> corners = new ArrayList();

    public World() {
        init();
        //Systems
        add(new WorldSystem(this));
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
            c.isCoast = c.isLand && landCount < c.touches.size();
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

    private Corner getCorner(double x, double y) {
        for (Corner c : corners) {
            if (Math.abs(c.pos.x - x) < .0001 && Math.abs(c.pos.y - y) < .0001) {
                return c;
            }
        }
        return null;
    }

    private void init() {
        //Polygons
        createPolygons();

        //Shape
        assignOceanLand();

        //Elevation
        Queue<Corner> q = new LinkedList();
        for (Corner c : corners) {
            if (c.isBorder) {
                c.elevation = 0;
                q.add(c);
            } else {
                c.elevation = Double.POSITIVE_INFINITY;
            }
        }
        while (!q.isEmpty()) {
            Corner c = q.poll();
            for (Edge e : c.protrudes) {
                Corner n = e.v0;
                if (c == n) {
                    n = e.v1;
                }
                if (e.v0 == e.v1) {
                    System.out.println("bad");
                }
                double newElevation = c.elevation + .001;
                if (e.isLand) {
                    newElevation += 1 + c.pos.subtract(n.pos).length();
                }
                if (n.elevation > newElevation) {
                    n.elevation = newElevation;
                    n.downslope = c;
                    q.add(n);
                }
            }
        }
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

        for (Center c : centers) {
            c.fix();
        }
//         Set the height at the borders to 0, add them to the elevation queue, set the rest to infinite height
//         While the queue is nonempty, grab the first corner
//         For each of its neighbors
//         If the path to the neighbor is shorter, set the neighbor's elevation and downslope, add it to queue
//         Sort the corners by elevation
//         Set each elevation to 1 - sqrt(1 - index/length)
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
            if (ge.x1 == ge.x2 && ge.y1 == ge.y2) {
                continue;
            }
            //Find centers
            Center cen1 = centers.get(ge.site1);
            Center cen2 = centers.get(ge.site2);
            //Make edge
            Edge e = new Edge();
            edges.add(e);
            //Find or make corners
            Corner cor1 = getCorner(ge.x1, ge.y1);
            if (cor1 == null) {
                cor1 = new Corner(ge.x1, ge.y1);
                corners.add(cor1);
            }
            Corner cor2 = getCorner(ge.x2, ge.y2);
            if (cor2 == null) {
                cor2 = new Corner(ge.x2, ge.y2);
                corners.add(cor2);
            }
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
    }
}
