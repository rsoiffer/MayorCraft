package movement;

import core.AbstractSystem;

public class VelocitySystem extends AbstractSystem {

    private PositionComponent pc;
    private VelocityComponent vc;

    public VelocitySystem(PositionComponent pc, VelocityComponent vc) {
        this.pc = pc;
        this.vc = vc;
    }

    @Override
    protected boolean pauseable() {
        return true;
    }

    @Override
    public void update() {
        pc.pos = pc.pos.add(vc.vel);
    }

}
