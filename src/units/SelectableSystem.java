package units;

import core.AbstractSystem;
import core.Color4d;
import core.Main;
import core.Vec2;
import graphics.Graphics;

public class SelectableSystem extends AbstractSystem {

    private SelectableComponent sc;

    public SelectableSystem(SelectableComponent sc) {
        this.sc = sc;
    }

    @Override
    public void update() {
        if (Main.gameManager.sc.selected.contains(sc)) {
            Graphics.drawEllipse(sc.pc.pos, new Vec2(sc.size, sc.size), Color4d.WHITE, 20);
        }
    }

}
