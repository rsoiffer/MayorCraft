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
//            glColor4d(0, 0, 0, 1);
//            glBegin(GL_LINE_LOOP);
//            {
//                for (Corner co : c.corners) {
//                    glVertex2d(co.pos.x, co.pos.y);
//                }
//            }
//            glEnd();
            glPopMatrix();

        }

        for (Edge e : world.edges) {
            glPushMatrix();
            glDisable(GL_TEXTURE_2D);
            glColor4d(0, 0, 0, 1);
            glBegin(GL_LINES);
            {
                glVertex2d(e.v0.pos.x, e.v0.pos.y);
                glVertex2d(e.v1.pos.x, e.v1.pos.y);
            }
            glEnd();
            glColor4d(1, 0, 1, 1);
            glBegin(GL_LINES);
            {
                glVertex2d(e.p0.pos.x, e.p0.pos.y);
                glVertex2d(e.p1.pos.x, e.p1.pos.y);
            }
            glEnd();
            glPopMatrix();
        }
    }

}
