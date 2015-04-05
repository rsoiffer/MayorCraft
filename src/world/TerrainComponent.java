package world;

import core.AbstractComponent;
import core.Vec2;
import graphics.SpriteContainer;
import graphics.Texture;
import java.io.IOException;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

public class TerrainComponent extends AbstractComponent {

    public ArrayList<GridPoint> trees;
    public BackgroundBlock[][] blockArray;
    private final int ARR_DIM = World.SIZE / BackgroundBlock.SIZE;

    public TerrainComponent() {
        blockArray = new BackgroundBlock[ARR_DIM][ARR_DIM];
        for (int i = 0; i < ARR_DIM; i++) {
            for (int j = 0; j < ARR_DIM; j++) {
                blockArray[i][j] = new BackgroundBlock(-World.SIZE + BackgroundBlock.SIZE + 2 * BackgroundBlock.SIZE * i, -World.SIZE + BackgroundBlock.SIZE + 2 * BackgroundBlock.SIZE * j);
            }
        }
        ArrayList<Vec2> trees = new ArrayList();
        for (int k = 0; k < 10000; k++) {
            //Vec2 pos = Vec2.random(World.SIZE);
            trees.add(Vec2.random(World.SIZE));
        }
        for (int i = 0; i < ARR_DIM; i++) {
            for (int j = 0; j < ARR_DIM; j++) {
                BackgroundBlock b = blockArray[i][j];
                b.drawStart();
                try {
                    Texture tree = SpriteContainer.loadSprite("tree", 1, 1).get(0);
                    tree.bind();
                    for (Vec2 pos : trees) {
                        glBegin(GL_QUADS);
                        {
                            glTexCoord2d(0, 0);
                            glVertex2d(pos.x, pos.y + tree.getImageHeight()); //Height reversed because sprite y axis upside-down
                            glTexCoord2d(0, tree.getHeight());
                            glVertex2d(pos.x, pos.y);
                            glTexCoord2d(tree.getWidth(), tree.getHeight());
                            glVertex2d(pos.x + tree.getImageWidth(), pos.y);
                            glTexCoord2d(tree.getWidth(), 0);
                            glVertex2d(pos.x + tree.getImageWidth(), pos.y + tree.getImageHeight());
                        }
                        glEnd();
                    }
                } catch (IOException ex) {
                }
                b.drawEnd();
            }
        }
    }

    public ArrayList<BackgroundBlock> all() {
        ArrayList r = new ArrayList(ARR_DIM * ARR_DIM);
        for (int i = 0; i < ARR_DIM; i++) {
            for (int j = 0; j < ARR_DIM; j++) {
                r.add(blockArray[i][j]);
            }
        }
        return r;
    }
}
