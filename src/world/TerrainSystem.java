package world;

import core.AbstractSystem;
import core.Color4d;
import graphics.SpriteContainer;
import graphics.Texture;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;

public class TerrainSystem extends AbstractSystem {

    private TerrainComponent tc;

    public TerrainSystem(TerrainComponent tc) {
        this.tc = tc;
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void update() {
        try {
            glEnable(GL_TEXTURE_2D);
            Texture tree = SpriteContainer.loadSprite("tree", 1, 1).get(0);
            tree.bind();
            Color4d.WHITE.glColor();
            for (GridPoint gp : tc.trees) {
                glTranslated(gp.toVec2().x - tree.getImageWidth() / 2, gp.toVec2().y - tree.getImageHeight() / 2, 0);
                glBegin(GL_QUADS);
                {
                    glTexCoord2d(0, 0);
                    glVertex2d(0, tree.getImageHeight()); //Height reversed because sprite y axis upside-down
                    glTexCoord2d(0, tree.getHeight());
                    glVertex2d(0, 0);
                    glTexCoord2d(tree.getWidth(), tree.getHeight());
                    glVertex2d(tree.getImageWidth(), 0);
                    glTexCoord2d(tree.getWidth(), 0);
                    glVertex2d(tree.getImageWidth(), tree.getImageHeight());
                }
                glEnd();
                glTranslated(-gp.toVec2().x + tree.getImageWidth() / 2, -gp.toVec2().y + tree.getImageHeight() / 2, 0);
            }
        } catch (IOException ex) {
        }
    }
}
