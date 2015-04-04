package graphics;

import core.AbstractComponent;
import core.Vec2;

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
        viewX = -1000;
        viewY = -500;
        viewWidth = 1920;
        viewHeight = 1080;
    }

    public boolean inView(Vec2 pos) {
        return pos.x > viewX && pos.x < viewX + viewWidth && pos.y > viewY && pos.y < viewY + viewHeight;
    }

    public Vec2 middle() {
        return new Vec2(viewX + viewWidth / 2, viewY + viewHeight / 2);
    }

    public boolean potentiallyInView(Vec2 pos, Vec2 buffer) {
        return pos.subtract(middle()).lengthSquared() < (viewWidth + buffer.x) * (viewWidth + buffer.x) + (viewHeight + buffer.y) * (viewHeight + buffer.y);
    }
}
