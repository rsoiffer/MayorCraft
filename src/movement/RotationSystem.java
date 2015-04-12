package movement;

import core.AbstractSystem;

public class RotationSystem extends AbstractSystem {

    private RotationComponent rc;

    public RotationSystem(RotationComponent rc) {
        this.rc = rc;
    }

    @Override
    protected boolean pauseable() {
        return true;
    }

    @Override
    public void update() {
        rc.rot += rc.aVel;
    }
}
