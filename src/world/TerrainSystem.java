package world;

import core.AbstractSystem;
import core.Color4d;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public class TerrainSystem extends AbstractSystem {

    private TerrainComponent tc;

    public TerrainSystem(TerrainComponent tc) {
        this.tc = tc;
    }

    @Override
    public void update() {
        glEnable(GL_TEXTURE_2D);
        Color4d.WHITE.glColor();
        for (BackgroundBlock b : tc.all()) {
            b.draw();
        }
    }

}
