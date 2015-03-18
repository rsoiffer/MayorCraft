package units;

import core.AbstractSystem;
import core.Vec2;
import movement.PositionComponent;

public class AnimationSystem extends AbstractSystem {

    private AnimationComponent ac;
    private DestinationComponent dc;
    private PositionComponent pc;

    @Override
    public void update() {
        double speed = 4;
        Vec2 direction = dc.path.get(0).subtract(pc.pos).normalize();
        Vec2 vel = direction.multiply(speed);
//        ac.legL.target = pc.pos.add(direction.multiply(30)).add(direction.normal().multiply(8));
//        ac.legR.target = pc.pos.add(direction.multiply(30)).add(direction.normal().multiply(-8));
//        double distL2 = ac.legL.target.subtract(ac.legL.pos).lengthSquared();
//        if ()
        //optimization code

        double s_d = direction.y;
        double c_d = direction.x;

        double rotation_speed = 10;

        double r = Math.max(1, speed * 2.3);

        double target_direction = direction.direction();
        double diff_direction = Math.abs(target_direction - ac.direction + 4 * Math.PI) % 2 * Math.PI;
        if (diff_direction < rotation_speed) {
            ac.direction = target_direction;
        } else if (diff_direction < 180) {
            ac.direction += rotation_speed;
        } else {
            ac.direction -= rotation_speed;
        }

        //arm_direction = ac.direction;
        ac.legL.target = ac.legL.target.setX(pc.pos.x - 8 * s_d + c_d * speed * 30 / r);
        ac.legL.target = ac.legL.target.setY(pc.pos.y - 8 * c_d + s_d * speed * 30 / r);
        ac.legR.target = ac.legR.target.setX(pc.pos.x + 8 * s_d + c_d * speed * 30 / r);
        ac.legR.target = ac.legR.target.setY(pc.pos.y + 8 * c_d + s_d * speed * 30 / r);

        Vec2 dxy = ac.legL.pos.subtract(ac.legL.target);
        double ldist = dxy.length() + 0.1;
        boolean foot_down = false;
        if (speed < 1 && ldist < 2) {
            //foot_down = 0;
        } else if (ldist > ac.d) {
            foot_down = true;
        }

        dxy = ac.legR.pos.subtract(ac.legR.target);
        double rdist = dxy.length() + 0.1;
        if (speed < 1 && rdist < 2) {
            foot_down = true;
        } else if (ldist > ac.d) {
            foot_down = false;
        }

        if (foot_down) {
            ac.legL.pos = ac.legL.pos.add(ac.legL.target.subtract(ac.legL.pos).multiply(Math.min(1, r / ldist)));
        } else {
            ac.legR.pos = ac.legR.pos.add(ac.legR.target.subtract(ac.legR.pos).multiply(Math.min(1, r / rdist)));
        }
        //makes the character look like it's running if it's going fast enough
        if (speed > 2) {
            ac.legL.pos = ac.legL.pos.add(vel.multiply(.5));
            ac.legR.pos = ac.legR.pos.add(vel.multiply(.5));
        }

        if (ldist > ac.d && rdist > ac.d) {
            ac.legL.pos = new Vec2(pc.pos.x - 8 * s_d, pc.pos.y - 8 * c_d);
            ac.legR.pos = new Vec2(pc.pos.x + 8 * s_d, pc.pos.y + 8 * c_d);
        }
    }
}
