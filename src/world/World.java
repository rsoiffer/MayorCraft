package world;

import core.AbstractEntity;
import core.Color4d;

public class World extends AbstractEntity {

    static final int POINTS = 10000;
    static final int BUCKETS = POINTS / 10;
    static final int SIZE = 50000;
    static final int GRID_SIZE = 50;

    public World() {
        //Components
        WorldComponent wc = add(new WorldComponent());
        wc.init();
//        TerrainComponent tc = add(new TerrainComponent());

        //Systems
        add(new WorldSystem(wc));
//        add(new TerrainSystem(tc));
    }

    static double riverWidth(double water) {
        return SIZE / 1500. * Math.sqrt(water);
    }

    static Color4d waterColor(double elevation) {
        return new Color4d(elevation / 4, elevation / 2, 1, 1);
    }
}
