package graphics;

import core.AbstractSystem;
import core.Main;
import movement.PositionComponent;
import movement.RotationComponent;

public class RenderSystem extends AbstractSystem {

    private PositionComponent pc;
    private RotationComponent rc;
    private SpriteComponent sc;

    public RenderSystem(PositionComponent pc, SpriteComponent sc) {
        this(pc, new RotationComponent(), sc);
    }

    public RenderSystem(PositionComponent pc, RotationComponent rc, SpriteComponent sc) {
        this.pc = pc;
        this.rc = rc;
        this.sc = sc;
    }

    @Override
    protected int getLayer() {
        return 1;
    }

    @Override
    public void update() {
        sc.imageIndex += sc.imageSpeed;
        if (sc.visible) {
            if (Main.gameManager.rmc.nearInView(pc.pos, sc.getTexture().size().multiply(sc.scale))) {
                Graphics.drawSprite(sc.getTexture(), pc.pos, sc.scale, rc.rot, sc.color);
            }
        }
    }

}
