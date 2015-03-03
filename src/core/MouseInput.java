package core;

import graphics.RenderManagerComponent;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public abstract class MouseInput {

    private static ArrayList<Integer> down = new ArrayList();
    private static ArrayList<Integer> pressed = new ArrayList();
    private static ArrayList<Integer> released = new ArrayList();

    public static boolean isDown(int button) {
        return down.contains(button);
    }

    public static boolean isPressed(int button) {
        return pressed.contains(button);
    }

    public static boolean isReleased(int button) {
        return released.contains(button);
    }

    public static Vec2 mouse() {
        RenderManagerComponent rmc = Main.gameManager.rmc;
        int w = Display.getWidth();
        int h = Display.getHeight();
        int dw = rmc.viewWidth;
        int dh = rmc.viewHeight;
        double vw, vh;
        if (w * dh > h * dw) {
            vh = h;
            vw = dw * h / dh;
        } else {
            vw = w;
            vh = dh * w / dw;
        }
        double left = (w - vw) / 2;
        double bottom = (h - vh) / 2;

        return new Vec2(rmc.viewX + (Mouse.getX() - left) * dw / vw, rmc.viewY + (Mouse.getY() - bottom) * dh / vh);
    }

    public static void update() {
        pressed.clear();
        released.clear();
        while (Mouse.next()) {
            Integer button = Mouse.getEventButton();
            if (Mouse.getEventButtonState()) {
                down.add(button);
                pressed.add(button);
            } else {
                down.remove(button);
                released.add(button);
            }
        }
    }
}
