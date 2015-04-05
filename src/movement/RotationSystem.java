package movement;

import core.AbstractSystem;

public class RotationSystem extends AbstractSystem {

    private RotationComponent rc;

    public RotationSystem(RotationComponent rc) {
        this.rc = rc;
    }

    @Override
    public void update() {
        rc.rot += rc.aVel;
    }
}
