package world;

import core.AbstractComponent;
import core.Vec2;
import java.util.ArrayList;
import static world.World.WORLD_SIZE;

public class GridComponent extends AbstractComponent {

    private GridPoint[][] grid;
    public boolean changed;
    public static final int SQUARE_SIZE = 50;
    public static int GRID_SIZE;

    public GridComponent() {
        GRID_SIZE = 2 * WORLD_SIZE / SQUARE_SIZE;
        grid = new GridPoint[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new GridPoint(i, j);
            }
        }
        changed = false;
    }

    public GridPoint get(int x, int y) {
        return grid[x][y];
    }

    public GridPoint get(Vec2 v) {
        return grid[((int) v.x + WORLD_SIZE) / SQUARE_SIZE][((int) v.y + WORLD_SIZE) / SQUARE_SIZE];
    }

    public static Vec2 gridlock(Vec2 v) {
        return new Vec2(Math.round(v.x / SQUARE_SIZE) * SQUARE_SIZE, Math.round(v.y / SQUARE_SIZE) * SQUARE_SIZE);
    }

//    public boolean open(Vec2 v1, Vec2 v2) {
//        GridPoint g1 = get(v1);
//        GridPoint g2 = get(v2);
//        for (int i = g1.x; i <= g2.x; i++) {
//            for (int j = g1.y; j <= g2.y; j++) {
//                if (grid[i][j].blocked) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
    public ArrayList<GridPoint> points(Vec2 v1, Vec2 v2) {
        ArrayList<GridPoint> r = new ArrayList();
        GridPoint g1 = get(v1);
        GridPoint g2 = get(v2);
        for (int i = g1.x; i <= g2.x; i++) {
            for (int j = g1.y; j <= g2.y; j++) {
                r.add(grid[i][j]);
            }
        }
        return r;
    }

    public static Vec2 pos(int i, int j) {
        return new Vec2(-WORLD_SIZE + SQUARE_SIZE / 2 + SQUARE_SIZE * i, -WORLD_SIZE + SQUARE_SIZE / 2 + SQUARE_SIZE * j);
    }
}
