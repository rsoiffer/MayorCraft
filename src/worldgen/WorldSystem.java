package worldgen;

import core.*;
import graphics.Graphics;
import static org.lwjgl.opengl.GL11.*;

public class WorldSystem extends AbstractSystem {

    private World world;

    public WorldSystem(World world) {
        this.world = world;
    }

    public Color4d getColor(Center c, Corner co) {
        if (c.elevation > 10) {
            return Color4d.RED; //Error
        }
        if (c.isLand) {
            if (c.isOnOcean) {
                return new Color4d(.8, .8, .5, 1); //Beach
            } else {
                return new Color4d(.2, 1 - c.elevation * .9, 0, 1); //Land
            }
        } else {
            if (c.isOnOcean) {
                return new Color4d(0, 0, .7, 1); //Ocean
            } else {
                return new Color4d(.2, .6, 1, 1); //Lake
            }
        }
    }

    @Override
    public void update() {
        Graphics.fillRect(new Vec2(-World.SIZE, -World.SIZE), new Vec2(2 * World.SIZE, 2 * World.SIZE), new Color4d(0, 0, .7, 1));

        for (Center c : world.centers) {
            //Straight edges
//            glBegin(GL_POLYGON);
//            {
//                for (Corner co : c.corners) {
//                    getColor(c, co).glColor();
//                    glVertex2d(co.pos.x, co.pos.y);
//                }
//            }
//            glEnd();
            //Noisy edges
            getColor(c, null).glColor();
            for (Edge e : c.borders) {
                glBegin(GL_TRIANGLE_FAN);
                {
                    glVertex2d(c.pos.x, c.pos.y);
                    for (int i = 0; i < e.noisePath.size(); i++) {
                        glVertex2d(e.noisePath.get(i).x, e.noisePath.get(i).y);

                    }
                }
                glEnd();
            }
        }

        for (Edge e : world.edges) {
            //Straight edges
//            Graphics.drawLine(e.v0.pos, e.v1.pos, Color4d.BLACK, 1);
            //Noisy edges
            if (e.water > 0 && e.isLand) {
                Color4d.BLUE.glColor();
                glLineWidth(2 * (float) Math.sqrt(e.water));
            } else {
                Color4d.BLACK.glColor();
                glLineWidth(1);
            }
            glBegin(GL_LINE_STRIP);
            {
                for (Vec2 v : e.noisePath) {
                    glVertex2d(v.x, v.y);
                }
            }
            glEnd();
        }

        for (Corner c : world.corners) {
            if (c.isLand && !c.isCoast) {
                //Graphics.drawLine(c.pos, c.downslope.pos, Color4d.BLUE, 2);
            }
        }
    }

}
