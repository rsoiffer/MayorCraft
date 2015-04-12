package movement;

import core.AbstractSystem;

public class GravitySystem extends AbstractSystem {

    private VelocityComponent vc;
    private GravityComponent gc;

    public GravitySystem(VelocityComponent vc, GravityComponent gc) {
        this.vc = vc;
        this.gc = gc;
    }

    @Override
    protected boolean pauseable() {
        return true;
    }

    @Override
    public void update() {
        vc.vel = vc.vel.add(gc.g);
    }

}
