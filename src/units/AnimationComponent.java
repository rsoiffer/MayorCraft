package units;

import core.AbstractComponent;
import core.Vec2;

public class AnimationComponent extends AbstractComponent {

    public boolean leftFootDown;
    public int armPose;
    //public double d = 36;
    public double time;
    public double stride = 30;
    public double width = 8;
    public Limb legL, legR, armL, armR;

    public AnimationComponent(Vec2 pos) {
        legL = new Limb("foot");
        legR = new Limb("foot");
        armL = new Limb("hand");
        armR = new Limb("hand");
    }
}
