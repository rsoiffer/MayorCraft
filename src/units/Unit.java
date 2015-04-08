package units;

import buildings.Building;
import core.AbstractComponent;
import core.AbstractEntity;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import java.util.ArrayList;
import movement.*;
import world.Terrain;

public class Unit extends AbstractEntity {

    public Unit(Vec2 pos, Terrain t, Building build) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        VelocityComponent vc = add(new VelocityComponent());
        PreviousPositionComponent ppc = add(new PreviousPositionComponent(pos));
        RotationComponent rc = add(new RotationComponent());
        SpriteComponent sc = add(new SpriteComponent("PoliceHead2"));//("cavemanHead"));
        AnimationComponent ac = add(new AnimationComponent(pos));
        DestinationComponent dc = add(new DestinationComponent());
        SelectableComponent slc = add(new SelectableComponent(16, pc, dc));
        ResourceComponent rsc = add(new ResourceComponent(t, build));

        //Systems
        add(new DestinationSystem(pc, vc, dc));
        add(new VelocitySystem(pc, vc));
        add(new CollisionSystem(pc, ppc, vc, slc, dc));
        add(new SelectableSystem(slc));
        add(new RenderSystem(pc, rc, sc));
        add(new AnimationSystem(ac, pc, ppc, rc));
        add(new PreviousPositionSystem(pc, ppc));
    }


}
