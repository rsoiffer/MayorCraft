package movement;

import movement.GravityComponent;
import movement.VelocityComponent;
import core.AbstractSystem;

public class GravitySystem extends AbstractSystem {

    private VelocityComponent velocity;
    private GravityComponent gravity;

    public GravitySystem(VelocityComponent velocity, GravityComponent gravity) {
        this.velocity = velocity;
        this.gravity = gravity;
    }

    @Override
    public void update() {
        velocity.vel = velocity.vel.add(gravity.g);
    }

}
