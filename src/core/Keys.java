package core;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

public abstract class Keys {

    private static ArrayList<Integer> down = new ArrayList();
    private static ArrayList<Integer> pressed = new ArrayList();
    private static ArrayList<Integer> released = new ArrayList();

    public static boolean isDown(int key) {
        return down.contains(key);
    }

    public static boolean isPressed(int key) {
        return pressed.contains(key);
    }

    public static boolean isReleased(int key) {
        return released.contains(key);
    }

    public static void update() {
        pressed.clear();
        released.clear();
        while (Keyboard.next()) {
            Integer key = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                down.add(key);
                pressed.add(key);
            } else {
                down.remove(key);
                released.add(key);
            }
        }
    }
}
