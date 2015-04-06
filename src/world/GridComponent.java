package world;

import core.AbstractComponent;
import core.Vec2;
import static world.World.WORLD_SIZE;

public class GridComponent extends AbstractComponent {

    private boolean[][] grid;
    public boolean changed;
    public static final int SQUARE_SIZE = 50;
    public static int GRID_SIZE;

    public GridComponent() {
        GRID_SIZE = 2 * WORLD_SIZE / SQUARE_SIZE;
        grid = new boolean[GRID_SIZE][GRID_SIZE];
        changed = false;
    }

    public boolean get(GridPoint gp) {
        return grid[gp.x][gp.y];
    }

    public boolean get(int x, int y) {
        return grid[x][y];
    }

    public boolean open(GridPoint g1, GridPoint g2) {
        for (int i = g1.x; i <= g2.x; i++) {
            for (int j = g1.y; j <= g2.y; j++) {
                if (grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Vec2 pos(int i, int j) {
        return new Vec2(-WORLD_SIZE + SQUARE_SIZE / 2 + SQUARE_SIZE * i, -WORLD_SIZE + SQUARE_SIZE / 2 + SQUARE_SIZE * j);
    }

    public void set(GridPoint gp, boolean val) {
        if (val != get(gp)) {
            changed = true;
        }
        grid[gp.x][gp.y] = val;
    }

    public void set(int x, int y, boolean val) {
        if (val != get(x, y)) {
            changed = true;
        }
        grid[x][y] = val;
    }
}
