package graphics;

import core.AbstractSystem;
import core.Keys;
import core.MouseInput;
import core.Vec2;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class RenderManagerSystem extends AbstractSystem {

    private RenderManagerComponent rmc;

    public RenderManagerSystem(RenderManagerComponent renderManagerComponent) {
        rmc = renderManagerComponent;

        try {
            //Display Init
            setDisplayMode((int) rmc.displaySize.x, (int) rmc.displaySize.y, rmc.startFullscreen);
            Display.setVSyncEnabled(true);
            Display.setResizable(true);
            Display.setTitle("So how are you today?");
            Display.create();
            //OpenGL Init
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }

    private void calculateViewport() {
        int w = Display.getWidth();
        int h = Display.getHeight();
        int dw = (int) rmc.viewSize.x;
        int dh = (int) rmc.viewSize.y;
        int vw, vh;
        if (w * dh > h * dw) {
            vh = h;
            vw = dw * h / dh;
        } else {
            vw = w;
            vh = dh * w / dw;
        }
        int left = (w - vw) / 2;
        int bottom = (h - vh) / 2;
        glViewport(left, bottom, vw, vh);
    }

    public static void setDisplayMode(int width, int height, boolean fullscreen) {
        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }
        try {
            DisplayMode targetDisplayMode = null;
            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }
                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }
            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    @Override
    public void update() {
        if (Keys.isDown(KEY_W)) {
            rmc.viewPos = rmc.viewPos.add(new Vec2(0, 20 * rmc.viewSize.y / rmc.displaySize.y));
        }
        if (Keys.isDown(KEY_A)) {
            rmc.viewPos = rmc.viewPos.add(new Vec2(-20 * rmc.viewSize.x / rmc.displaySize.x, 0));
        }
        if (Keys.isDown(KEY_S)) {
            rmc.viewPos = rmc.viewPos.add(new Vec2(0, -20 * rmc.viewSize.y / rmc.displaySize.y));
        }
        if (Keys.isDown(KEY_D)) {
            rmc.viewPos = rmc.viewPos.add(new Vec2(20 * rmc.viewSize.x / rmc.displaySize.x, 0));
        }
        rmc.zoom -= MouseInput.wheel();
        if (rmc.zoom > 45) {
            rmc.zoom = 45;
        }
        if (rmc.zoom < -15) {
            rmc.zoom = -15;
        }
        rmc.viewSize = rmc.displaySize.multiply(Math.pow(1.1, rmc.zoom));

        if (Keys.isPressed(Keyboard.KEY_F11)) {
            setDisplayMode((int) rmc.displaySize.x, (int) rmc.displaySize.y, !Display.isFullscreen());
        }

        calculateViewport();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(rmc.LL().x, rmc.UR().x, rmc.LL().y, rmc.UR().y, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glClear(GL_COLOR_BUFFER_BIT);
        //glClearColor(1, 1, 1, 1);
    }

}
