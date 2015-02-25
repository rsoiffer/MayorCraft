package movement;

import core.AbstractSystem;

public class VelocitySystem extends AbstractSystem {

    private PositionComponent position;
    private VelocityComponent velocity;

    public VelocitySystem(PositionComponent position, VelocityComponent velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    @Override
    public void update() {
        position.pos = position.pos.add(velocity.vel);
    }

}
