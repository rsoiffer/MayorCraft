package movement;

import core.AbstractSystem;
import core.Vec2;

public class FrictionSystem extends AbstractSystem {

    private VelocityComponent vc;
    private RotationComponent rc;

    public FrictionSystem(VelocityComponent vc) {
        this(vc, null);
    }

    public FrictionSystem(VelocityComponent vc, RotationComponent rc) {
        this.vc = vc;
        this.rc = rc;
    }

    @Override
    protected boolean pauseable() {
        return true;
    }

    @Override
    public void update() {
        vc.vel = vc.vel.multiply(.99);
        if (vc.vel.lengthSquared() < .005) {
            vc.vel = new Vec2();
        }
        if (rc != null) {
            rc.aVel = rc.aVel * .99;
        }
    }
}
