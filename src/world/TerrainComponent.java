package world;

import core.AbstractComponent;
import core.Main;
import java.util.ArrayList;
import java.util.HashMap;
import static world.GridComponent.GRID_SIZE;
import static world.World.RANDOM;
import static world.World.WORLD_SIZE;

public class TerrainComponent extends AbstractComponent {

    public HashMap<Terrain, ArrayList<GridPoint>> terrainMap;

    public TerrainComponent() {
        terrainMap = new HashMap();
        for (Terrain t : Terrain.values()) {
            ArrayList<GridPoint> list = new ArrayList();
            terrainMap.put(t, list);
            for (int i = 0; i < WORLD_SIZE / 50000. / Terrain.values().length * WORLD_SIZE; i++) {
                GridPoint gp = null;
                do {
                    gp = Main.gameManager.gc.get(RANDOM.nextInt(GRID_SIZE), RANDOM.nextInt(GRID_SIZE));
                } while (gp.blocked || gp.onRiver);
                list.add(gp);
                gp.blocked = true;
                gp.terrain = t;
            }
        }
    }
}
