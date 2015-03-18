package units;

import core.AbstractSystem;
import core.Vec2;
import movement.PositionComponent;
import movement.RotationComponent;
import movement.VelocityComponent;

public class AnimationSystem extends AbstractSystem {

    private AnimationComponent ac;
    private PositionComponent pc;
    private VelocityComponent vc;
    private RotationComponent rc;

    public AnimationSystem(AnimationComponent ac, PositionComponent pc, VelocityComponent vc, RotationComponent rc) {
        this.ac = ac;
        this.pc = pc;
        this.vc = vc;
        this.rc = rc;
    }

    @Override
    public void update() {
        System.out.println(pc.pos);
        System.out.println(vc.vel);
        double speed = vc.vel.length();
        Vec2 direction = vc.vel.multiply(1 / speed);
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
        double diff_direction = Math.abs(target_direction - rc.rot + 4 * Math.PI) % 2 * Math.PI;
        if (diff_direction < rotation_speed) {
            rc.rot = target_direction;
        } else if (diff_direction < 180) {
            rc.rot += rotation_speed;
        } else {
            rc.rot -= rotation_speed;
        }

        //arm_direction = rc.rot;
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
            ac.legL.pos = ac.legL.pos.add(vc.vel.multiply(.5));
            ac.legR.pos = ac.legR.pos.add(vc.vel.multiply(.5));
        }

        if (ldist > ac.d && rdist > ac.d) {
            ac.legL.pos = new Vec2(pc.pos.x - 8 * s_d, pc.pos.y - 8 * c_d);
            ac.legR.pos = new Vec2(pc.pos.x + 8 * s_d, pc.pos.y + 8 * c_d);
        }
    }
}
