package terrain;

import core.AbstractEntity;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.PositionComponent;
import movement.RotationComponent;
import units.SelectableComponent;
import units.SelectableSystem;

public class Tree extends AbstractEntity {

    public Tree(Vec2 pos) {
        //Components
        PositionComponent pc = add(new PositionComponent(pos));
        SpriteComponent sc = add(new SpriteComponent("tree"));
        RotationComponent rc = add(new RotationComponent());
        rc.rot = Math.random() * 2 * Math.PI;
        SelectableComponent slc = add(new SelectableComponent(32, pc));

        //Systems
        add(new SelectableSystem(slc));
        add(new RenderSystem(pc, rc, sc));
    }
}
