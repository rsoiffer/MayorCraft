package worldgen;

import core.*;
import graphics.Graphics;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;

public class WorldSystem extends AbstractSystem {

    private World world;
    private double rot;
    private Vec2 pos;
    private int zoom;

    public WorldSystem(World world) {
        this.world = world;
        pos = new Vec2();
    }

    public Color4d getColor(Center c, Corner co) {
        if (c.elevation > 10) {
            return Color4d.RED; //Error
        }
        if (c.isLand) {
            if (c.isOnOcean) {
                return new Color4d(.8, .8, .5, 1); //Beach
            } else {
                return new Color4d(.2, .8 - c.elevation * .7, 0, 1); //Land
            }
        } else {
            if (c.isOnOcean) {
                return new Color4d(0, 0, .7, 1); //Ocean
            } else {
                return new Color4d(.2, .4, 1, 1); //Lake
            }
        }
    }

    @Override
    public void update() {
        if (Keys.isDown(KEY_LEFT)) {
            rot += .05;
        }
        if (Keys.isDown(KEY_RIGHT)) {
            rot -= .05;
        }
        if (Keys.isDown(KEY_W)) {
            pos = pos.add(new Vec2(0, -20. / zoom));
        }
        if (Keys.isDown(KEY_A)) {
            pos = pos.add(new Vec2(20. / zoom, 0));
        }
        if (Keys.isDown(KEY_S)) {
            pos = pos.add(new Vec2(0, 20. / zoom));
        }
        if (Keys.isDown(KEY_D)) {
            pos = pos.add(new Vec2(-20. / zoom, 0));
        }
        zoom += Mouse.getDWheel() / 120;
        if (zoom < 1) {
            zoom = 1;
        }
        
        glPushMatrix();
        glScaled(zoom, .75*zoom, 1);
        glTranslated(pos.x, pos.y, 0);
        glRotated(rot * 180 / Math.PI, 0, 0, 1);
        //glScaled(1,.5,1);

        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);

        Graphics.fillRect(new Vec2(-World.SIZE, -World.SIZE), new Vec2(2 * World.SIZE, 2 * World.SIZE), new Color4d(0, 0, .7, 1));

        for (Center c : world.centers) {
            //Straight edges
            glBegin(GL_POLYGON);
            {
                for (Corner co : c.corners) {
                    getColor(c, co).glColor();
                    glVertex2d(co.pos.x+ co.elevation*100*Math.sin(rot), co.pos.y + co.elevation*100*Math.cos(rot));
                }
            }
            glEnd();
            //Noisy edges
//            getColor(c, null).glColor();
//            for (Edge e : c.borders) {
//                glBegin(GL_TRIANGLE_FAN);
//                {
//                    glVertex2d(c.pos.x, c.pos.y);
//                    for (int i = 0; i < e.noisePath.size(); i++) {
//                        glVertex2d(e.noisePath.get(i).x, e.noisePath.get(i).y);
//
//                    }
//                }
//                glEnd();
//            }
        }

//        Color4d.BLACK.glColor();
//        glLineWidth(1);
//        for (Edge e : world.edges) {
//            //Straight edges
////            Graphics.drawLine(e.v0.pos, e.v1.pos, Color4d.BLACK, 1);
//            //Noisy edges
//            if (e.water == 0 || !e.isLand) {
//                {
//                    glBegin(GL_LINE_STRIP);
//                    {
//                        for (Vec2 v : e.noisePath) {
//                            glVertex2d(v.x, v.y);
//                        }
//                    }
//                    glEnd();
//                }
//            }
//        }
        //Rivers
        new Color4d(.1, .2, 1, 1).glColor();
        for (Edge e : world.edges) {
            if (e.water > 0 && e.isLand) {
                glLineWidth(zoom * 1.5f * (float) Math.sqrt(e.water));
                glBegin(GL_LINE_STRIP);
                {
                    for (Vec2 v : e.noisePath) {
                        //glVertex2d(v.x, v.y);
                        glVertex2d(v.x+ e.v0.elevation*100*Math.sin(rot), v.y + e.v0.elevation*100*Math.cos(rot));
                    }
                }
                glEnd();
            }
        }

        for (Corner c : world.corners) {
            if (c.isLand && !c.isCoast) {
                //Graphics.drawLine(c.pos, c.downslope.pos, Color4d.BLUE, 2);
            }
        }
        glPopMatrix();
    }

}
