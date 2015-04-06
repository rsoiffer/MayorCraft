package units;

import core.AbstractComponent;
import core.Main;
import movement.PositionComponent;

public class SelectableComponent extends AbstractComponent {

    public double size;
    public PositionComponent pc;
    public DestinationComponent dc;

    public SelectableComponent(double size, PositionComponent pc) {
        this(size, pc, null);
    }

    public SelectableComponent(double size, PositionComponent pc, DestinationComponent dc) {
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

}
