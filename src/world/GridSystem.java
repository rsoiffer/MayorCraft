package world;

import core.*;
import graphics.Graphics;
import static world.GridComponent.*;

public class GridSystem extends AbstractSystem {

    private GridComponent gc;
    public static boolean SHOW_COLLISION_BOXES = false;

    public GridSystem(GridComponent gc) {
        this.gc = gc;
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void update() {
        gc.changed = false;
        if (SHOW_COLLISION_BOXES) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (gc.get(i, j)) {
                        if (Main.gameManager.rmc.inView(GridComponent.pos(i, j))) {
                            Graphics.drawRegPoly(GridComponent.pos(i, j), 4, SQUARE_SIZE / 2, Color4d.RED);
                        }
                    }
                }
            }
        }
    }
}
