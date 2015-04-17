package units;

import core.AbstractSystem;
import core.Color4d;
import core.Main;
import core.Vec2;
import graphics.Graphics;
import graphics.SpriteContainer;
import java.io.IOException;
import movement.PositionComponent;
import movement.PreviousPositionComponent;
import movement.RotationComponent;

public class AnimationSystem extends AbstractSystem {

    private AnimationComponent ac;
    private PositionComponent pc;
    private PreviousPositionComponent ppc;
    private RotationComponent rc;
    private final double DETAIL = 50;

    public AnimationSystem(AnimationComponent ac, PositionComponent pc, PreviousPositionComponent ppc, RotationComponent rc) {
        this.ac = ac;
        this.pc = pc;
        this.ppc = ppc;
        this.rc = rc;
    }

    public void drawLimb(Limb l, Vec2 direction, Vec2 side, double forward, double size) {
        //Draw end
        Graphics.drawSprite(l.sprite, l.pos, new Vec2(1, 1), rc.rot, Color4d.WHITE);

        //Calculate base and middle
        Vec2 base = pc.pos.add(side.multiply(.6));
        Vec2 middle = base.interpolate(l.pos, .5).add(direction.multiply(ac.width * forward));

        //Draw segments and middle
        Graphics.drawWideLine(l.pos, middle, ac.color, size);
        Graphics.fillEllipse(middle, new Vec2(size, size), ac.color, 10);
        Graphics.drawWideLine(base, middle, ac.color, size);
    }

    @Override
    public void update() {
        //Variables
        double speed = pc.pos.subtract(ppc.pos).length();
        if (!Main.paused) {
            if (speed > 1) {
                double target = pc.pos.subtract(ppc.pos).direction();
                //Turn slowly
                if ((target - rc.rot + 4 * Math.PI) % (2 * Math.PI) < .1) {
                    rc.rot = target;
                } else {
                    //Figure out which direction to complete the animation to move the legs back to 0
                    if ((target - rc.rot + 4 * Math.PI) % (2 * Math.PI) > Math.PI) {
                        rc.rot -= .1;
                    } else {
                        rc.rot += .1;
                    }
                }
                rc.rot = (rc.rot + 2 * Math.PI) % (2 * Math.PI);
                //rc.rot = vc.vel.direction();
            }
        }
        Vec2 direction = new Vec2(Math.cos(rc.rot), Math.sin(rc.rot));

        Vec2 left = direction.normal().multiply(ac.width);
        Vec2 right = left.reverse();

        //How fast to run the animation
        double walkSpeed = Math.max(1, speed) / ac.stride;

        if (!Main.paused) {
            //Walk forwards
            if (speed > 1) {
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
        }

        //Turn time into a position - triangle wave
        double offset = ac.stride * (.5 - Math.abs((ac.time + .5) % 2 - 1));
        double ahead = speed;

        //Move feet
        ac.legL.target = pc.pos.add(left).add(direction.multiply(offset + ahead));
        ac.legR.target = pc.pos.add(right).add(direction.multiply(-offset + ahead));
        ac.legL.pos = ac.legL.pos.interpolate(ac.legL.target, .5);
        ac.legR.pos = ac.legR.pos.interpolate(ac.legR.target, .5);

        //Move arms
        ac.armL.target = pc.pos.add(left.multiply(2)).add(direction.multiply(-offset + ahead));
        ac.armR.target = pc.pos.add(right.multiply(2)).add(direction.multiply(offset + ahead));
        ac.armL.pos = ac.armL.pos.interpolate(ac.armL.target, .5);
        ac.armR.pos = ac.armR.pos.interpolate(ac.armR.target, .5);

        if (speed > 1) {
            //Draw feet
            drawLimb(ac.legL, direction, left, .25, 4);
            drawLimb(ac.legR, direction, right, .25, 4);
            //Draw arms
            drawLimb(ac.armL, direction, left, -.5, 3);
            drawLimb(ac.armR, direction, right, -.5, 3);
        } else {
            //Draw feet
            drawLimb(ac.legL, direction, left, 0, 4);
            drawLimb(ac.legR, direction, right, 0, 4);
            //Draw arms
            drawLimb(ac.armL, direction, left, 0, 3);
            drawLimb(ac.armR, direction, right, 0, 3);
        }
        try {
            Graphics.drawSprite(SpriteContainer.loadSprite("PoliceHead2"), pc.pos, new Vec2(1, 1), rc.rot, ac.color);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//         Graphics.fillEllipse(pc.pos, new Vec2(7,8), ac.armL.color1, DETAIL);
//         Graphics.drawEllipse(pc.pos, new Vec2(7,8), Color4d.BLACK, DETAIL);
//       //  Graphics.fillEllipse(pc.pos.add(new Vec2(0,8)), new Vec2(1,1), new Color4d(255,211,168,255, true), DETAIL);
//         //Graphics.fillEllipse(pc.pos.add(new Vec2(0,-8)), new Vec2(1,1), new Color4d(255,211,168,255, true), DETAIL);
//         Graphics.drawEllipse(pc.pos.add(direction.multiply(5)), new Vec2(4,6), ac.armL.color1, DETAIL);
    }
}
