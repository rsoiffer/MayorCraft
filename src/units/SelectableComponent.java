package units;

import core.AbstractComponent;
import core.Main;
import core.Vec2;
import movement.PositionComponent;

public class SelectableComponent extends AbstractComponent {

    public int id;
    public double size;
    public PositionComponent pc;
    public DestinationComponent dc;

    public SelectableComponent(double size, PositionComponent pc) {
        this(size, pc, null);
    }

    public SelectableComponent(double size, PositionComponent pc, DestinationComponent dc) {
        id = Main.gameManager.sc.id++;
        this.size = size;
        this.pc = pc;
        this.dc = dc;
        Main.gameManager.sc.all.add(this);
    }

    @Override
    protected void destroy() {
        Main.gameManager.sc.all.remove(this);
        Main.gameManager.sc.selected.remove(this);
    }

    public boolean open(Vec2 pos) {
        return Main.gameManager.gc.open(pos.subtract(new Vec2(size, size)), pos.add(new Vec2(size, size)));
    }
}
