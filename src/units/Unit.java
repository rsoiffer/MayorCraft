package units;

import buildings.Building;
import buildings.BuildingType;
import core.AbstractEntity;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.*;
import world.Terrain;

public class Unit extends AbstractEntity {

    public Unit(Vec2 pos, BuildingType build) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        VelocityComponent vc = add(new VelocityComponent());
        PreviousPositionComponent ppc = add(new PreviousPositionComponent(pos));
        RotationComponent rc = add(new RotationComponent());
        SpriteComponent sc = add(new SpriteComponent("PoliceHead2"));//("cavemanHead"));
        AnimationComponent ac = add(new AnimationComponent(pos, build.color));
        DestinationComponent dc = add(new DestinationComponent());
        SelectableComponent slc = add(new SelectableComponent(16, pc, dc));
        UnitComponent rsc = add(new UnitComponent(build));
        PathfindingComponent pac = add(new PathfindingComponent());

        //Systems
        add(new DestinationSystem(pc, vc, dc));
        add(new PathfindingSystem(pc, vc, dc, pac, slc));
        add(new VelocitySystem(pc, vc));
        add(new CollisionSystem(pc, ppc, vc, slc, dc));
        add(new SelectableSystem(slc));
       // add(new RenderSystem(pc, rc, sc));
        add(new AnimationSystem(ac, pc, ppc, rc));
        add(new PreviousPositionSystem(pc, ppc));
    }

}
