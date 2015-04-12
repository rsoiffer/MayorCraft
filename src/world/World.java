package world;

import core.AbstractEntity;
import core.Color4d;
import java.util.Random;

public class World extends AbstractEntity {

    static final int POINTS = 10000;
    static final int BUCKETS = POINTS / 10;
    public static final int WORLD_SIZE = 50000;
    public static Random RANDOM;
    public static int SEED = (int) (Math.random() * 10000);

    public World() {
        RANDOM = new Random(SEED);
        //Components
        WorldComponent wc = add(new WorldComponent());
        TerrainComponent tc = add(new TerrainComponent());

        //Systems
        add(new WorldSystem(wc));
        add(new TerrainSystem(tc));
    }

    static double riverWidth(double water) {
        return WORLD_SIZE / 1500. * Math.sqrt(water);
    }

    static Color4d waterColor(double elevation) {
        return new Color4d(elevation / 4, elevation / 2, 1, 1);
    }
}
