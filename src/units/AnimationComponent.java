package units;

import core.AbstractComponent;
import core.Vec2;

public class AnimationComponent extends AbstractComponent {

    public boolean footDown;
    public int armPose;
    public double direction;

    public Limb legL, legR, armL, armR;

    public AnimationComponent(Vec2 pos) {
        legL = new Limb("foot");
        legR = new Limb("foot");
        armL = new Limb("hand");
        armR = new Limb("hand");
    }
}
