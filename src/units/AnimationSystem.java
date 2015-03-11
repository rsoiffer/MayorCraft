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
        ldist = sqrt(dx * dx + dy * dy) + 0.1;
        if (speed < 1 && ldist < 2) {
            foot_down = 0;
        } else if (ldist > d) {
            foot_down = 1;
        }

        dx = rfoot_x - rfoot_targetx;
        dy = rfoot_y - rfoot_targety;
        rdist = sqrt(dx * dx + dy * dy) + 0.1;
        if (speed < 1 && rdist < 2) {
            foot_down = 1;
        } else if (rdist > d) {
            foot_down = 0;
        }

        if (foot_down == 1) {
            lfoot_x += ((lfoot_targetx - lfoot_x) / ldist) * min(r, ldist);
            lfoot_y += ((lfoot_targety - lfoot_y) / ldist) * min(r, ldist);
        }

        if (foot_down == 0) {
            rfoot_x += ((rfoot_targetx - rfoot_x) / rdist) * min(r, rdist);
            rfoot_y += ((rfoot_targety - rfoot_y) / rdist) * min(r, rdist);
        }
//makes the character look like it's running if it's going fast enough
        if (speed > 2) {
            lfoot_x += hspeed / 2;
            rfoot_x += hspeed / 2;
            lfoot_y += vspeed / 2;
            rfoot_y += vspeed / 2;
        }

        if (ldist > d && rdist > d) {
            lfoot_x = x - 8 * s_d;
            lfoot_y = y - 8 * c_d;
            rfoot_x = x + 8 * s_d;
            rfoot_y = y + 8 * c_d;
        }
    }

}
