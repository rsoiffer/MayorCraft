package movement;

import core.AbstractSystem;
import core.Vec2;

public class FrictionSystem extends AbstractSystem {

    private VelocityComponent velocity;
    private RotationComponent rotation;

    public FrictionSystem(VelocityComponent velocity) {
        this(velocity, new RotationComponent());
    }

    public FrictionSystem(VelocityComponent velocity, RotationComponent rotation) {
        this.velocity = velocity;
        this.rotation = rotation;
    }

    @Override
    public void update() {
        velocity.vel = velocity.vel.multiply(.99);
        if (velocity.vel.lengthSquared() < .005) {
            velocity.vel = new Vec2();
        }
        rotation.aVel = rotation.aVel * .99;
    }
}
