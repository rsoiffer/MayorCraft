package buildings;

import core.AbstractEntity;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.PositionComponent;
import units.SelectableComponent;
import units.SelectableSystem;

public class Building_2 extends AbstractEntity {

    public Building_2(Vec2 pos, BuildingType bt) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        SpriteComponent sc = add(new SpriteComponent("temp_building"));
        sc.color = bt.color;
        SelectableComponent slc = add(new SelectableComponent(100, pc));
        CollisionBoxComponent cbc = add(new CollisionBoxComponent(this, pos.subtract(new Vec2(100, 100)), pos.add(new Vec2(100, 100))));
        //Systems
        add(new RenderSystem(pc, sc));
        add(new SelectableSystem(slc));
    }
}
