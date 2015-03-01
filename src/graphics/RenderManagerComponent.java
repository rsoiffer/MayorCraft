package graphics;

import core.AbstractComponent;

public class RenderManagerComponent extends AbstractComponent {

    public int displayWidth;
    public int displayHeight;
    public boolean startFullscreen;
    public int viewX;
    public int viewY;
    public int viewWidth;
    public int viewHeight;

    public RenderManagerComponent() {
        displayWidth = 1920;
        displayHeight = 1080;
        startFullscreen = true;
        viewX = -500;
        viewY = -500;
        viewWidth = 1920;
        viewHeight = 1080;
    }
}
