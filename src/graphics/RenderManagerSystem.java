package graphics;

import core.AbstractSystem;
import core.Keys;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class RenderManagerSystem extends AbstractSystem {

    private RenderManagerComponent rmc;

    public RenderManagerSystem(RenderManagerComponent renderManagerComponent) {
        rmc = renderManagerComponent;

        try {
            //Display Init
            setDisplayMode(renderManagerComponent.displayWidth, renderManagerComponent.displayHeight, renderManagerComponent.startFullscreen);
            Display.setVSyncEnabled(true);
            Display.setResizable(true);
            Display.setTitle("So how are you today?");
            Display.create();
            //OpenGL Init
            glEnable(GL_BLEND);
            //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }

    private void calculateViewport() {
        int w = Display.getWidth();
        int h = Display.getHeight();
        int dw = rmc.viewWidth;
        int dh = rmc.viewHeight;
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
        if (Keys.isPressed(Keyboard.KEY_F11)) {
            setDisplayMode(rmc.displayWidth, rmc.displayHeight, !Display.isFullscreen());
        }

        calculateViewport();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(rmc.viewX, rmc.viewX + rmc.viewWidth, rmc.viewY, rmc.viewY + rmc.viewHeight, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0, 0, 0, 1);
    }

}
