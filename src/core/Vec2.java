package core;

import static org.lwjgl.opengl.GL11.glVertex2d;

public class Vec2 {

    public final double x;
    public final double y;

    public Vec2() {
        x = 0;
        y = 0;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public boolean containedBy(Vec2 v1, Vec2 v2) {
        return v1.quadrant(this) != v2.quadrant(this) && v1.quadrant(this) % 2 == v2.quadrant(this) % 2;
    }
    


    public double cross(Vec2 other) {
        return x * other.y - y * other.x;
    }

    public double direction() {
        return Math.atan2(y, x);
    }

    public Vec2 divide(Vec2 v) {
        return new Vec2(x / v.x, y / v.y);
    }

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec2) {
            Vec2 v = (Vec2) o;
            return x == v.x && y == v.y;
        }
        return false;
    }

    public void glVertex() {
        glVertex2d(x, y);
    }

    public Vec2 interpolate(Vec2 other, double amt) {
        return multiply(amt).add(other.multiply(1 - amt));
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vec2 multiply(double d) {
        return new Vec2(x * d, y * d);
    }

    public Vec2 multiply(Vec2 other) {
        return new Vec2(x * other.x, y * other.y);
    }

    public Vec2 normal() {
        return new Vec2(-y, x);
    }

    public Vec2 normalize() {
        return multiply(1 / length());
    }

    public int quadrant(Vec2 other) {
        if (other.x >= x) {
            if (other.y >= y) {
                return 1;
            } else {
                return 4;
            }
        } else {
            if (other.y >= y) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public static Vec2 random(double r) {
        return new Vec2(Math.random() * 2 * r - r, Math.random() * 2 * r - r);
    }

    public Vec2 reverse() {
        return new Vec2(-x, -y);
    }

    public Vec2 setLength(double l) {
        return multiply(l / length());
    }

    public Vec2 setX(double x) {
        return new Vec2(x, y);
    }

    public Vec2 setY(double y) {
        return new Vec2(x, y);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
