package units;

import core.AbstractSystem;
import core.Color4d;
import core.Vec2;
import graphics.Graphics;
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
    
    public void drawLimb(Limb l, Vec2 direction, Vec2 side, double forward) {
        //Draw end
        Graphics.drawSprite(l.sprite, l.pos, new Vec2(1, 1), rc.rot, Color4d.WHITE);

        //Calculate base and middle
        Vec2 base = pc.pos.add(side.multiply(.6));
        Vec2 middle = base.interpolate(l.pos, .5).add(direction.multiply(ac.width*forward));

        //Draw segments and middle
        Graphics.drawWideLine(l.pos, middle, l.color1, 4);
        Graphics.fillEllipse(middle, new Vec2(4, 4), l.color2);
        Graphics.drawWideLine(base, middle, l.color2, 4);

    }

    @Override
    public void update() {
        //Variables
        double speed = vc.vel.length();
        Vec2 direction;
        if (speed > 0) {
            rc.rot = vc.vel.direction();
            direction = vc.vel.multiply(1 / speed);
        } else {
            direction = new Vec2(Math.cos(rc.rot), Math.sin(rc.rot));
        }
        Vec2 left = direction.normal().multiply(ac.width);
        Vec2 right = left.reverse();

        //How fast to run the animation
        double walkSpeed = Math.max(1, speed) / ac.stride;

        //Walk forwards
        if (speed > 0) {
            ac.time += walkSpeed;
        } else {
            //Round
            if (Math.abs(ac.time - Math.round(ac.time)) < .01) {
                ac.time = Math.round(ac.time);
            } else {
                //Figure out which direction to complete the animation to move the legs back to 0
                if (ac.time - (int) ac.time > .5) {
                    ac.time += walkSpeed;
                } else {
                    ac.time -= walkSpeed;
                }
            }
        }

        //Turn time into a position - triangle wave
        double offset = ac.stride * (.5 - Math.abs((ac.time + .5) % 2 - 1));
        double ahead = 2 * speed;

        //Move feet
        ac.legL.target = pc.pos.add(left).add(direction.multiply(offset + ahead));
        ac.legR.target = pc.pos.add(right).add(direction.multiply(-offset + ahead));
        ac.legL.pos = ac.legL.pos.interpolate(ac.legL.target, .5);
        ac.legR.pos = ac.legR.pos.interpolate(ac.legR.target, .5);

        //Move arms
        ac.armL.target = pc.pos.add(left.multiply(2)).add(direction.multiply(1.2*(-offset + ahead)));
        ac.armR.target = pc.pos.add(right.multiply(2)).add(direction.multiply(1.2*(offset + ahead)));
        ac.armL.pos = ac.armL.pos.interpolate(ac.armL.target, .5);
        ac.armR.pos = ac.armR.pos.interpolate(ac.armR.target, .5);

        //Draw feet
        drawLimb(ac.legL, direction, left, .25);
        drawLimb(ac.legR, direction, right, .25);
        //Draw arms
        drawLimb(ac.armL, direction, left, -.5);
        drawLimb(ac.armR, direction, right, -.5);
    }
}
