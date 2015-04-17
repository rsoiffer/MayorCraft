package units;

import buildings.BuildingType;
import buildings.BuildingTypeComponent;
import core.AbstractEntity;
import core.Color4d;
import core.Vec2;
import movement.*;

public class Unit extends AbstractEntity {

    public Unit(Vec2 pos, BuildingType type) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        VelocityComponent vc = add(new VelocityComponent());
        PreviousPositionComponent ppc = add(new PreviousPositionComponent(pos));
        RotationComponent rc = add(new RotationComponent());
        AnimationComponent ac = add(new AnimationComponent(pos, (type) != null ? type.color : new Color4d(1, .85, 0)));
        DestinationComponent dc = add(new DestinationComponent(pos));
        SelectableComponent slc = add(new SelectableComponent(16, pc, dc));
        BuildingTypeComponent btc = add(new BuildingTypeComponent(type));
        PathfindingComponent pac = add(new PathfindingComponent());
        GatheringComponent gc = add(new GatheringComponent());
        //Systems
        add(new DestinationSystem(pc, vc, dc));
        add(new PathfindingSystem(pc, vc, dc, pac, slc));
        add(new VelocitySystem(pc, vc));
        add(new CollisionSystem(pc, ppc, vc, slc, dc));
        add(new SelectableSystem(slc));
        add(new AnimationSystem(ac, pc, ppc, rc));
        add(new PreviousPositionSystem(pc, ppc));
        add(new UnitSystem(pc, dc, btc, gc, ac));
    }
}
