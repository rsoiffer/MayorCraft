package core;

import graphics.RenderManagerComponent;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public abstract class MouseInput {

    private static ArrayList<Integer> down = new ArrayList();
    private static ArrayList<Integer> pressed = new ArrayList();
    private static ArrayList<Integer> released = new ArrayList();
    private static int wheel;

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
        int dw = (int) rmc.viewSize.x;
        int dh = (int) rmc.viewSize.y;
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

        return new Vec2(rmc.LL().x + (Mouse.getX() - left) * dw / vw, rmc.LL().y + (Mouse.getY() - bottom) * dh / vh);
    }

    public static int wheel() {
        return wheel;
    }

    public static void update() {
        wheel = Mouse.getDWheel() / 120;
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
