package buildings;

import core.AbstractEntity;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.PositionComponent;
import units.SelectableComponent;
import units.SelectableSystem;

public class Building extends AbstractEntity {

    public Building(Vec2 pos, BuildingType bt) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        SpriteComponent sc = add(new SpriteComponent("Door_Building"));
        sc.color = bt.color;
        sc.scale = new Vec2(2, 2);
        SelectableComponent slc = add(new SelectableComponent(150, pc));
        CollisionBoxComponent cbc = add(new CollisionBoxComponent(this, pos.subtract(new Vec2(90, 90)), pos.add(new Vec2(90, 90))));
        //Systems
        add(new RenderSystem(pc, sc));
        add(new SelectableSystem(slc));
    }
}
