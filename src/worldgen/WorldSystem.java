package worldgen;

import core.*;
import graphics.Graphics;
import graphics.RenderManagerComponent;
import static org.lwjgl.opengl.GL11.*;

public class WorldSystem extends AbstractSystem {

    private World world;
    private Vec2 pos;
    private static final Color4d RIVER = new Color4d(.1, .2, 1, 1);
    private static final Color4d LAKE = new Color4d(.2, .4, 1, 1);

    public WorldSystem(World world) {
        this.world = world;
        pos = new Vec2();
    }

    public Color4d getColor(Center c) {
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
            return waterColor(c.elevation);
//            if (c.isOnOcean) {
//                return new Color4d(0, 0, .7, 1); //Ocean
//            } else {
//                return new Color4d(.2, .4, 1, 1); //Lake
//            }
        }
    }

    @Override
    public void update() {
        RenderManagerComponent rmc = Main.gameManager.getComponent(RenderManagerComponent.class);

        glPushMatrix();
        glTranslated(pos.x, pos.y, 0);

        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);

        Graphics.fillRect(new Vec2(rmc.viewX, rmc.viewY), new Vec2(rmc.viewWidth, rmc.viewHeight), waterColor(0));

        //Draw land
        for (Center c : world.centers) {
            if (c.inView(rmc)) {
                if (c.isLand) {
                    getColor(c).glColor();
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
            }
        }
        //Draw borders
        Color4d.BLACK.glColor();
        glLineWidth(1);
        for (Edge e : world.edges) {
            if (e.inView(rmc)) {
                if ((e.water == 0 || !e.isLand) && (e.p0.isLand || e.p1.isLand)) {
                    {
                        glBegin(GL_LINE_STRIP);
                        {
                            for (Vec2 v : e.noisePath) {
                                glVertex2d(v.x, v.y);
                            }
                        }
                        glEnd();
                    }
                }
            }
        }
        //Draw rivers
        for (Edge e : world.edges) {
            if (e.inView(rmc)) {
                if (e.water > 0 && e.isLand) {
                    //New draw river
                    Graphics.fillEllipse(e.v0.pos, new Vec2(10 * Math.sqrt(e.water), 10 * Math.sqrt(e.water)), waterColor(e.v0.elevation));
                    glBegin(GL_TRIANGLE_STRIP);
                    {
                        //Main part
                        Vec2 dir = e.v1.pos.subtract(e.v0.pos);
                        for (int i = 0; i < e.noisePath.size() - 2; i++) {
                            Vec2 start = e.noisePath.get(i);
                            Vec2 end = e.noisePath.get(i + 1);
                            Vec2 side = end.subtract(start).setLength(10 * Math.sqrt(e.water)).normal();
                            start.add(side).glVertex();
                            start.add(side.reverse()).glVertex();
                            double perc = (end.dot(dir) - e.v0.pos.dot(dir)) / dir.lengthSquared();
                            waterColor(e.v0.elevation * (1 - perc) + e.v1.elevation * perc).glColor();
                            //waterColor(e.v0.elevation * (1 - (double) i / e.noisePath.size()) + e.v1.elevation * ((double) i / e.noisePath.size())).glColor();
                            end.add(side).glVertex();
                            end.add(side.reverse()).glVertex();
                        }
                        //Blend with next river
                        Edge ed = e.v1.edgeTo(e.v1.downslope);
                        Vec2 start = e.noisePath.get(e.noisePath.size() - 2);
                        Vec2 end = e.v1.pos;
                        Vec2 side = end.subtract(start).setLength(10 * Math.sqrt(e.water)).normal();
                        start.add(side).glVertex();
                        start.add(side.reverse()).glVertex();
                        waterColor(e.v1.elevation).glColor();
                        if (!ed.isLand) {
                            end.add(side).glVertex();
                            end.add(side.reverse()).glVertex();
                        } else {
                            Vec2 side1 = end.subtract(start).setLength(10 * Math.sqrt(ed.water)).normal();
                            end.add(side1).glVertex();
                            end.add(side1.reverse()).glVertex();
                            Vec2 start2 = ed.v0.pos;
                            Vec2 end2 = ed.noisePath.get(1);
                            Vec2 side2 = end2.subtract(start2).setLength(10 * Math.sqrt(ed.water)).normal();
                            start2.add(side2).glVertex();
                            start2.add(side2.reverse()).glVertex();
                        }
                    }
                    glEnd();
                    Graphics.fillEllipse(e.v1.pos, new Vec2(10 * Math.sqrt(e.water), 10 * Math.sqrt(e.water)), waterColor(e.v1.elevation));
                }
            }
        }
        //Draw water
        for (Center c : world.centers) {
            if (c.inView(rmc)) {
                if (!c.isLand) {
                    getColor(c).glColor();
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
            }
        }
        glPopMatrix();
    }

    public Color4d waterColor(double elevation) {
        return new Color4d(elevation / 4, elevation / 2, 1, 1);
    }

}
