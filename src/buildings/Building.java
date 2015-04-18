package buildings;

import static buildings.BuildingType.FARM;
import static buildings.BuildingType.HOUSE;
import core.AbstractEntity;
import core.Main;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.PositionComponent;
import units.SelectableComponent;
import units.SelectableSystem;

public class Building extends AbstractEntity {

    public Building(Vec2 pos, BuildingType type) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        SpriteComponent sc = add(new SpriteComponent("Door_Building"));
        sc.color = type.color;
        sc.scale = new Vec2(2, 2);
        SelectableComponent slc = add(new SelectableComponent(150, pc));
        BuildingTypeComponent btc = add(new BuildingTypeComponent(type));
        CollisionBoxComponent cbc = add(new CollisionBoxComponent(this, pos.subtract(new Vec2(90, 90)), pos.add(new Vec2(90, 90))));
        //Systems
        add(new RenderSystem(pc, sc));
        add(new SelectableSystem(slc));

        if (type == FARM) {
            Main.gameManager.rc.food += 5;
        }
        if (type == HOUSE) {
            add(new HouseSystem(pc));
        }
    }

    @Override
    public void destroySelf() {
        if (getComponent(BuildingTypeComponent.class).type == FARM) {
            Main.gameManager.rc.food -= 5;
        }
        super.destroySelf();
    }
}
