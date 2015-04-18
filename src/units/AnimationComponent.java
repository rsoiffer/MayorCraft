package units;

import core.AbstractComponent;
import core.Color4d;
import core.Vec2;

public class AnimationComponent extends AbstractComponent {

    public double time;
    public double stride = 30;
    public double width = 8;
    public Color4d color;
    public Limb legL, legR, armL, armR;

    public AnimationComponent(Vec2 pos) {
        legL = new Limb("foot", pos);
        legR = new Limb("foot", pos);
        armL = new Limb("hand", pos);
        armR = new Limb("hand", pos);
    }

    public AnimationComponent(Vec2 pos, Color4d color) {
        this(pos);
        this.color = color;
    }
}
