package graphics;

import core.Color4d;
import core.Vec2;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

public abstract class Graphics {

    public static void drawCut(ArrayList<Vec2> path) {
        if (path.size() < 3) {
            return;
        }
        //Smoothe list
        ArrayList<Vec2> newL = new ArrayList();
        newL.add(path.get(0));
        for (int i = 1; i < path.size() - 1; i++) {
            newL.add(path.get(i - 1).add(path.get(i)).add(path.get(i + 1)).multiply(1. / 3));
        }
        newL.add(path.get(path.size() - 1));
        path = newL;
        //Draw
        glDisable(GL_TEXTURE_2D);
        Color4d.WHITE.glColor();
        glBegin(GL_TRIANGLE_STRIP);
        {
            Vec2 p1 = path.get(0).add(path.get(0).subtract(path.get(1)).setLength(30));
            glVertex2d(p1.x, p1.y);
            for (int i = 1; i < path.size() - 1; i++) {
                Vec2 a = path.get(i - 1);
                Vec2 b = path.get(i);
                Vec2 c = b.add(b.subtract(a).normal().setLength(20 * (path.size() - i) / path.size()));
                Vec2 d = b.add(b.subtract(a).normal().setLength(-20 * (path.size() - i) / path.size()));
                c.glVertex();
                d.glVertex();
            }
            path.get(path.size() - 1).glVertex();
        }
        glEnd();
    }

    public static void drawEllipse(Vec2 pos, Vec2 size, Color4d color, double detail) {
        glDisable(GL_TEXTURE_2D);
        color.glColor();
        glBegin(GL_LINE_LOOP);
        {
            for (double angle = 0; angle < detail; angle++) {
                glVertex2d(pos.x + size.x * Math.cos(angle / detail * Math.PI * 2), pos.y + size.y * Math.sin(angle / detail * Math.PI * 2));
            }
        }
        glEnd();
    }

    public static void drawLine(Vec2 start, Vec2 end) {
        drawLine(start, end, Color4d.BLACK, 2);
    }

    public static void drawLine(Vec2 start, Vec2 end, Color4d color, int width) {
        glDisable(GL_TEXTURE_2D);
        glLineWidth(width);
        color.glColor();
        glBegin(GL_LINES);
        {
            start.glVertex();
            end.glVertex();
        }
        glEnd();
    }

    public static void drawRegPoly(Vec2 pos, double n, double size, Color4d color) {
        size = size / Math.cos(Math.PI / n);
        glDisable(GL_TEXTURE_2D);
        color.glColor();
        glBegin(GL_LINE_LOOP);
        {
            for (double angle = .5; angle < n; angle++) {
                glVertex2d(pos.x + size * Math.cos(angle / n * Math.PI * 2), pos.y + size * Math.sin(angle / n * Math.PI * 2));
            }
        }
        glEnd();
    }

    public static void drawSprite(Texture s, Vec2 pos, Vec2 scale, double angle, Color4d color) {
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);
        s.bind();
        //Translate twice to rotate at center
        color.glColor();
        glTranslated(pos.x, pos.y, 0);
        glRotated(angle * 180 / Math.PI, 0, 0, 1);
        glScaled(scale.x, scale.y, 1);
        glTranslated(-s.getImageWidth() / 2, -s.getImageHeight() / 2, 0);

        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            glVertex2d(0, s.getImageHeight()); //Height reversed because sprite y axis upside-down
            glTexCoord2d(0, s.getHeight());
            glVertex2d(0, 0);
            glTexCoord2d(s.getWidth(), s.getHeight());
            glVertex2d(s.getImageWidth(), 0);
            glTexCoord2d(s.getWidth(), 0);
            glVertex2d(s.getImageWidth(), s.getImageHeight());
        }
        glEnd();
        glPopMatrix();
    }

    public static void drawText(String s, Vec2 pos) {
        drawText(s, "Default", pos, Color.black);
    }

    public static void drawText(String s, String font, Vec2 pos, Color c) {
        TextureImpl.bindNone();
        FontContainer.get(font).drawString((float) pos.x, (float) pos.y, s, c);
    }

    public static void drawWideLine(Vec2 start, Vec2 end, Color4d color, double width) {
        glDisable(GL_TEXTURE_2D);
        color.glColor();
        Vec2 side = end.subtract(start).setLength(width).normal();
        glBegin(GL_QUADS);
        {
            start.add(side).glVertex();
            start.add(side.reverse()).glVertex();
            end.add(side.reverse()).glVertex();
            end.add(side).glVertex();
        }
        glEnd();
    }

    public static void fillEllipse(Vec2 pos, Vec2 size, Color4d color, double detail) {
        //glDisable(GL_TEXTURE_2D);
        color.glColor();
        glBegin(GL_TRIANGLE_FAN);
        {
            pos.glVertex();
            for (double angle = 0; angle <= detail; angle++) {
                glVertex2d(pos.x + size.x * Math.cos(angle / detail * Math.PI * 2), pos.y + size.y * Math.sin(angle / detail * Math.PI * 2));
            }
        }
        glEnd();
    }

    public static void fillRect(Vec2 pos, Vec2 size, Color4d color) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        color.glColor();
        glTranslated(pos.x, pos.y, 0);
        glScaled(size.x, size.y, 1);
        glBegin(GL_QUADS);
        {
            glVertex2d(0, 0);
            glVertex2d(1, 0);
            glVertex2d(1, 1);
            glVertex2d(0, 1);
        }
        glEnd();
        glPopMatrix();
    }
}
