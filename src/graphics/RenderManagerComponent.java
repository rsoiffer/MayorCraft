package graphics;

import core.AbstractComponent;
import core.Vec2;

public class RenderManagerComponent extends AbstractComponent {

    public Vec2 viewPos;
    public Vec2 viewSize;
    public Vec2 displaySize;
    public boolean startFullscreen;
    public int zoom;

    public RenderManagerComponent() {
        displaySize = new Vec2(1920, 1080);
        startFullscreen = true;
        viewPos = new Vec2();
        viewSize = displaySize;
        zoom = 0;
    }

    public boolean inView(Vec2 pos) {
        return pos.x > LL().x && pos.x < UR().x && pos.y > LL().y && pos.y < UR().y;
    }

    public Vec2 LL() {
        return viewPos.subtract(viewSize.multiply(.5));
    }

    public boolean nearInView(Vec2 pos, double buffer) {
        return pos.x > LL().x - buffer && pos.x < UR().x + buffer && pos.y > LL().y - buffer && pos.y < UR().y + buffer;
    }

    public boolean potentiallyInView(Vec2 pos, Vec2 buffer) {
        return pos.subtract(viewPos).lengthSquared() < (viewSize.x + buffer.x) * (viewSize.x + buffer.x) + (viewSize.y + buffer.y) * (viewSize.y + buffer.y);
    }

    public Vec2 UR() {
        return viewPos.add(viewSize.multiply(.5));
    }
}
