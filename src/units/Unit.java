package units;

import core.AbstractEntity;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import movement.*;

public class Unit extends AbstractEntity {

    public Unit() {
        //components
        PositionComponent pc = add(new PositionComponent());
        //RotationComponent rc = add(new RotationComponent());
        DestinationComponent dc = add(new DestinationComponent());
        SpriteComponent sc = new SpriteComponent();

        //Systems
        add(new DestinationSystem(pc, dc));
        add(new RenderSystem(pc, sc));

    }

}
