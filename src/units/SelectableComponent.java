package units;

import core.AbstractComponent;
import core.Main;
import movement.PositionComponent;
import movement.VelocityComponent;

public class SelectableComponent extends AbstractComponent {

    public double size;
    public PositionComponent pc;
    public VelocityComponent vc;
    public DestinationComponent dc;
    public double invMass;

    public SelectableComponent(double size, PositionComponent pc) {
        this(size, pc, null, null, 0);
    }

    public SelectableComponent(double size, PositionComponent pc, VelocityComponent vc, DestinationComponent dc, double invMass) {
        this.size = size;
        this.pc = pc;
        this.vc = vc;
        this.dc = dc;
        this.invMass = invMass;
        Main.gameManager.sc.all.add(this);
    }

    @Override
    protected void destroy() {
        Main.gameManager.sc.all.remove(this);
        Main.gameManager.sc.selected.remove(this);
    }

}
