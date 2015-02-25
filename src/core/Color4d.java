package core;

import org.lwjgl.opengl.GL11;

public class Color4d {

    public final static Color4d WHITE = new Color4d(1, 1, 1, 1);
    public final static Color4d BLACK = new Color4d(0, 0, 0, 1);
    public final static Color4d RED = new Color4d(1, 0, 0, 1);
    public final static Color4d GREEN = new Color4d(0, 1, 0, 1);
    public final static Color4d BLUE = new Color4d(0, 0, 1, 1);
    public final double r;
    public final double g;
    public final double b;
    public final double a;

    public Color4d(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color4d(double r, double g, double b) {
        this(r, g, b, 1);
    }

    public Color4d() {
        this(1, 1, 1, 1);
    }

    public void glColor() {
        GL11.glColor4d(r, g, b, a);
    }

    public Color4d setR(double r) {
        return new Color4d(r, g, b, a);
    }

    public Color4d setG(double g) {
        return new Color4d(r, g, b, a);
    }

    public Color4d setB(double b) {
        return new Color4d(r, g, b, a);
    }

    public Color4d setA(double a) {
        return new Color4d(r, g, b, a);
    }
}
