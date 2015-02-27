package worldgen;

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
            xs[i] = Math.random() * 1000 - 500;
            ys[i] = Math.random() * 1000 - 500;
        }
        Voronoi v = new Voronoi(.1);
        List<GraphEdge> l = v.generateVoronoi(xs, ys, -500, 500, -500, 500);
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
