package worldgen;

import core.AbstractEntity;
import java.util.*;
import voronoi.GraphEdge;
import voronoi.Voronoi;

public class World extends AbstractEntity {

    public static final int SIZE = 500;
    List<Center> centers = new ArrayList();
    List<Edge> edges = new ArrayList();
    List<Corner> corners = new ArrayList();

    public World() {
        init();
        //Systems
        add(new WorldSystem(this));
    }

    private void calculateVoronoi(double[] xs, double[] ys) {
        centers.clear();
        edges.clear();
        corners.clear();
        Voronoi v = new Voronoi(.1);
        List<GraphEdge> gel = v.generateVoronoi(xs, ys, -SIZE, SIZE, -SIZE, SIZE);
        //Create all centers
        for (int i = 0; i < 100; i++) {
            centers.add(new Center(xs[i], ys[i]));
        }
        for (GraphEdge ge : gel) {
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
            if (!cen1.corners.contains(cor1)) {
                cen1.corners.add(cor1);
            }
            if (!cen1.corners.contains(cor2)) {
                cen1.corners.add(cor2);
            }
            cen2.neighbors.add(cen1);
            cen2.borders.add(e);
            if (!cen2.corners.contains(cor1)) {
                cen2.corners.add(cor1);
            }
            if (!cen2.corners.contains(cor2)) {
                cen2.corners.add(cor2);
            }
            //Add to edge
            e.p0 = cen1;
            e.p1 = cen2;
            e.v0 = cor1;
            e.v1 = cor2;
            e.mid = cor1.pos.add(cor2.pos).multiply(.5);
            //Add to corners
            cor1.adjacent.add(cor2);
            cor1.protrudes.add(e);
            if (!cor1.touches.contains(cen1)) {
                cor1.touches.add(cen1);
            }
            if (!cor1.touches.contains(cen2)) {
                cor1.touches.add(cen2);
            }
            cor2.adjacent.add(cor1);
            cor2.protrudes.add(e);
            if (!cor2.touches.contains(cen1)) {
                cor2.touches.add(cen1);
            }
            if (!cor2.touches.contains(cen2)) {
                cor2.touches.add(cen2);
            }
        }
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
        double[] xs = new double[100];
        double[] ys = new double[100];
        for (int i = 0; i < 100; i++) {
            xs[i] = Math.random() * 1000 - SIZE;
            ys[i] = Math.random() * 1000 - SIZE;
        }
        calculateVoronoi(xs, ys);
        for (int i = 0; i < 100; i++) {
            Center c = centers.get(i);
            xs[i] = c.average().x;
            ys[i] = c.average().y;
        }
        //calculateVoronoi(xs, ys);
//         Choose a ton of random points
//         Calculate their Voronoi polygons through Fortune's algorithm
//         Smooth the distribution by replacing each point with the average of its polygon's point
//         Recalculate the Voronoi polygons

        //Shape
        for (Corner c : corners) {
            if (c.pos.lengthSquared() < SIZE * SIZE * .8) {
                c.isLand = true;
            }
        }
//         Determine whether each corner is land or water

        //Elevation
        Queue<Corner> q = new LinkedList();
        for (Corner c : corners) {
            if (c.isBorder) {
                c.elevation = 0;
                q.add(c);
            } else {
                c.elevation = 9999;
            }
        }
        while (!q.isEmpty()) {
            Corner c = q.poll();
            for (Corner n : c.adjacent) {
                double newElevation = c.elevation;
                if (n.isLand && c.isLand) {
                    newElevation++;
                }
                if (n.elevation > newElevation) {
                    n.elevation = newElevation;
                    n.downslope = c;
                    q.add(n);
                }
            }
        }
        ArrayList<Corner> land = new ArrayList();
        for (Corner c : corners) {
            if (c.isLand) {
                land.add(c);
            }
        }
        Collections.sort(land);
        for (int i = 0; i < land.size(); i++) {
            land.get(i).elevation = 1.1 * (1 - Math.sqrt(1 - (double) i / land.size()));
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
}

/*package worldgen;

 import core.AbstractEntity;
 import java.util.List;
 import voronoi.GraphEdge;
 import voronoi.Voronoi;

 public class World extends AbstractEntity {

 public World() {
 //Create world code

 //Polygons
 double[] xs = new double[100];
 double[] ys = new double[100];
 for (int i = 0; i < 100; i++) {
 xs[i] = Math.random() * 1000 - SIZE;
 ys[i] = Math.random() * 1000 - SIZE;
 }
 Voronoi v = new Voronoi(.1);
 List<GraphEdge> l = v.generateVoronoi(xs, ys, -SIZE, SIZE, -SIZE, SIZE);
 for (GraphEdge e : l) {

 }
 //         Choose a ton of random points
 //         Calculate their Voronoi polygons through Fortune's algorithm
 //         Smooth the distribution by replacing each point with the average of its polygon's point
 //         Recalculate the Voronoi polygons

 //Shape
 //         Determine whether each corner is land or water
 //Elevation
 //         Set the height at the borders to 0, add them to the elevation queue, set the rest to infinite height
 //         While the queue is nonempty, grab the first corner
 //         For each of its neighbors
 //         If the path to the neighbor is shorter, set the neighbor's elevation and downslope, add it to queue
 //         Sort the corners by elevation
 //         Set each elevation to 1 - sqrt(1 - index/length)
 }
 }
 */
