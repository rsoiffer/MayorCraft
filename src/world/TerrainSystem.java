package world;

import core.AbstractSystem;
import core.Color4d;
import core.Main;
import core.Vec2;
import graphics.SpriteContainer;
import graphics.Texture;
import java.io.IOException;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

public class TerrainSystem extends AbstractSystem {

    private TerrainComponent tc;

    public TerrainSystem(TerrainComponent tc) {
        this.tc = tc;
    }

    private void drawTerrain(ArrayList<GridPoint> list, String texName) {
        try {
            Texture tex = SpriteContainer.loadSprite(texName, 1, 1).get(0);
            tex.bind();
            Vec2 halfSize = tex.size().multiply(.5);
            glBegin(GL_QUADS);
            for (GridPoint gp : list) {
                if (Main.gameManager.rmc.nearInView(gp.toVec2(), halfSize)) {
                    //glTranslated(gp.toVec2().x - tex.getImageWidth() / 2, gp.toVec2().y - tex.getImageHeight() / 2, 0);
                    double x = gp.toVec2().x - tex.getImageWidth() / 2;
                    double y = gp.toVec2().y - tex.getImageHeight() / 2;
                    {
                        glTexCoord2d(0, 0);
                        glVertex2d(x, y + tex.getImageHeight()); //Height reversed because sprite y axis upside-down
                        glTexCoord2d(0, tex.getHeight());
                        glVertex2d(x, y);
                        glTexCoord2d(tex.getWidth(), tex.getHeight());
                        glVertex2d(x + tex.getImageWidth(), y);
                        glTexCoord2d(tex.getWidth(), 0);
                        glVertex2d(x + tex.getImageWidth(), y + tex.getImageHeight());
                    }
                    //glTranslated(-gp.toVec2().x + tex.getImageWidth() / 2, -gp.toVec2().y + tex.getImageHeight() / 2, 0);
                }
            }
            glEnd();
        } catch (IOException ex) {
        }
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void update() {
        glEnable(GL_TEXTURE_2D);
        Color4d.WHITE.glColor();
        drawTerrain(tc.trees, "tree");
    }
}
