package core;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import units.Unit;
import worldgen.World;

public abstract class Main {

    public static void main(String[] args) {
        try {
            init();
            run();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        } finally {
            destroy();
        }
    }

    public static final int LAYERS = 3;
    public static ArrayList<ArrayList<AbstractSystem>> systems;
    public static int speed = 60;
    public static GameManager gameManager;

    public static void destroy() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public static void init() throws LWJGLException {
        systems = new ArrayList();
        for (int i = 0; i < LAYERS; i++) {
            systems.add(new ArrayList());
        }
        gameManager = new GameManager();
        Keyboard.create();
        Mouse.create();

        //Create entities
        new World();
        new Unit(new Vec2());
    }

    public static void run() {
        while (!Display.isCloseRequested() && !Keys.isPressed(Keyboard.KEY_ESCAPE)) {
            //Input
            Keys.update();
            MouseInput.update();
            //Logic
            for (ArrayList<AbstractSystem> list : systems) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).update();
                }
            }
            //Graphics
            Display.update();
            Display.sync(speed);
        }
    }
}
