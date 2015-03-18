package units;

import core.AbstractEntity;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.*;

public class Unit extends AbstractEntity {

    public Unit(Vec2 pos) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        VelocityComponent vc = add(new VelocityComponent());
        RotationComponent rc = add(new RotationComponent());
        SpriteComponent sc = new SpriteComponent("cavemanHead");
        AnimationComponent ac = new AnimationComponent(pos);
        DestinationComponent dc = add(new DestinationComponent());

        //Systems
        add(new VelocitySystem(pc, vc));
        add(new DestinationSystem(pc, vc, dc));
        add(new RenderSystem(pc, rc, sc));
        add(new AnimationSystem(ac,pc,vc,rc));
    }

}
