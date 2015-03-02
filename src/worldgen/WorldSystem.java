package worldgen;

import core.AbstractSystem;
import static org.lwjgl.opengl.GL11.*;

public class WorldSystem extends AbstractSystem {

    private World world;

    public WorldSystem(World world) {
        this.world = world;
    }

    @Override
    public void update() {
        for (Center c : world.centers) {
            glPushMatrix();
            glDisable(GL_TEXTURE_2D);

            glBegin(GL_POLYGON);
            {
                for (Corner co : c.corners) {
                    if (c.elevation > 10) {
                        glColor4d(1, 0, 0, 1);
                    } else if (!c.isLand) {
                        glColor4d(0, 0, 1, 1);
                    } else {
                        glColor4d(co.elevation, 1, co.elevation, 1);
                    }
                    glVertex2d(co.pos.x, co.pos.y);
                }
            }
            glEnd();
            glPopMatrix();

        }

        for (Edge e : world.edges) {
            //Graphics.drawLine(e.v0.pos, e.v1.pos, Color4d.BLACK, 1);
        }

        for (Corner c : world.corners) {
            if (c.isLand && !c.isCoast) {
                //Graphics.drawLine(c.pos, c.downslope.pos, Color4d.BLUE, 2);
            }
        }
    }

}
