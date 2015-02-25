package core;

public class FPSManagerComponent extends AbstractComponent {

    public long lastFrameTime;
    public int fps;
    public long lastFPSCheckTime;
    public long delta;

    public FPSManagerComponent() {
        lastFrameTime = 0;
        fps = 0;
        lastFPSCheckTime = 0;
        delta = 0;
    }
}
