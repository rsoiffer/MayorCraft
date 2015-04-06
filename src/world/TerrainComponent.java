package world;

import core.AbstractComponent;
import core.Main;
import java.util.ArrayList;
import static world.GridComponent.GRID_SIZE;
import static world.World.RANDOM;
import static world.World.WORLD_SIZE;

public class TerrainComponent extends AbstractComponent {

    public ArrayList<GridPoint> trees;

    public TerrainComponent() {
        trees = new ArrayList();
        for (int i = 0; i < WORLD_SIZE / 50000. * WORLD_SIZE; i++) {
            int x, y;
            do {
                x = RANDOM.nextInt(GRID_SIZE);
                y = RANDOM.nextInt(GRID_SIZE);
            } while (Main.gameManager.gc.get(x, y));
            trees.add(new GridPoint(x, y));
            Main.gameManager.gc.set(x, y, true);
        }
    }
}
