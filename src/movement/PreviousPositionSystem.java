package movement;

import core.AbstractSystem;

public class PreviousPositionSystem extends AbstractSystem {

    private PositionComponent pc;
    private PreviousPositionComponent ppc;

    public PreviousPositionSystem(PositionComponent pc, PreviousPositionComponent ppc) {
        this.pc = pc;
        this.ppc = ppc;
    }

    @Override
    protected boolean pauseable() {
        return true;
    }

    @Override
    public void update() {
        ppc.pos = pc.pos;
    }

}
