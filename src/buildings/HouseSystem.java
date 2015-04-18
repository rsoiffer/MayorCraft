package buildings;

import static buildings.BuildingType.HOUSE;
import core.AbstractSystem;
import core.Main;
import core.Vec2;
import movement.PositionComponent;
import units.DestinationComponent;
import units.Unit;

public class HouseSystem extends AbstractSystem {

    private PositionComponent pc;
    private int counter = 0;

    public HouseSystem(PositionComponent pc) {
        this.pc = pc;
    }

    @Override
    public void update() {
        if (counter % 1000 == 0 && Main.gameManager.elc.getList(Unit.class).size() < Main.gameManager.rc.food) {
            Unit u = new Unit(pc.pos, HOUSE);
            DestinationComponent dc = u.getComponent(DestinationComponent.class);
            dc.des = pc.pos.add(new Vec2(0,-120));
            dc.changed = true;
        }
        counter++;
    }
}
