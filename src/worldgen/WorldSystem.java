package worldgen;

import core.AbstractSystem;
import core.Color4d;
import graphics.Graphics;
import static org.lwjgl.opengl.GL11.*;

public class WorldSystem extends AbstractSystem {

    private World world;

    public WorldSystem(World world) {
        this.world = world;
    }

    public Color4d getColor(Center c, Corner co) {
        if (c.elevation > 10) {
            return Color4d.RED;
        } else if (!c.isLand) {
            if (c.isOnOcean) return new Color4d(0,0,.7,1);
            return new Color4d(.2,.6,1,1);
        } else if (c.isOnOcean) {
            return new Color4d(.8,.8,.5,1);
        }else {
            return new Color4d(c.elevation, 1, c.elevation, 1);
        }
    }

    @Override
    public void update() {
        for (Center c : world.centers) {
            glPushMatrix();
            glDisable(GL_TEXTURE_2D);

            glBegin(GL_POLYGON);
            {
                for (Corner co : c.corners) {
                    getColor(c, co).glColor();
                    glVertex2d(co.pos.x, co.pos.y);
                }
            }
            glEnd();
            glPopMatrix();

        }

        for (Edge e : world.edges) {
            Graphics.drawLine(e.v0.pos, e.v1.pos, Color4d.BLACK, 1);
        }

        for (Corner c : world.corners) {
            if (c.isLand && !c.isCoast) {
                //Graphics.drawLine(c.pos, c.downslope.pos, Color4d.BLUE, 2);
            }
        }
    }

}
