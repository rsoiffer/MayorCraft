package core;

import static buildings.BuildingType.HOUSE;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import units.Unit;
import world.World;

public abstract class Main {

    public static void main(String[] args) throws IOException {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        try {
            init();
            run();
        } catch (Exception ex) {
            PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
            writer.println("Error: " + Calendar.getInstance().getTime().toGMTString());
            writer.println(ex);
            writer.close();
            ex.printStackTrace();
        } finally {
            destroy();
        }
        System.exit(0);
    }

    public static final int speed = 60;
    public static final int LAYERS = 3;
    public static ArrayList<ArrayList<AbstractSystem>> systems;
    public static GameManager gameManager;
    public static boolean paused = false;

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

        new Unit(Vec2.random(200), null);
        for (int i = 0; i < 10; i++) {
            new Unit(Vec2.random(200), HOUSE);
        }
    }

    public static void run() {
        while (!Display.isCloseRequested() && !Keys.isPressed(Keyboard.KEY_ESCAPE)) {
            //Input
            Keys.update();
            MouseInput.update();
            //Logic
            for (ArrayList<AbstractSystem> list : systems) {
                for (int i = 0; i < list.size(); i++) {
                    if (!paused || !list.get(i).pauseable()) {
                        list.get(i).update();
                    }
                }
            }
            //Graphics
            Display.update();
            Display.sync(speed);
        }
    }
}
