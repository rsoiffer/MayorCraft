package core;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class FPSManagerSystem extends AbstractSystem {

    private FPSManagerComponent fmc;

    public FPSManagerSystem(FPSManagerComponent fpsManagerComponent) {
        fmc = fpsManagerComponent;

        getDelta();
        fpsManagerComponent.lastFPSCheckTime = getTime();
    }

    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - fmc.lastFrameTime);
        fmc.lastFrameTime = time;
        return delta;
    }

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    @Override
    public void update() {
        fmc.delta = getDelta();
        if (getTime() - fmc.lastFPSCheckTime > 1000) {
            Display.setTitle("FPS: " + fmc.fps);
            fmc.fps = 0;
            fmc.lastFPSCheckTime = getTime();
        }
        fmc.fps++;
    }

}
