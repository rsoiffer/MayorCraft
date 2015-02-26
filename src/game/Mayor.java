package game;

import core.AbstractEntity;
import movement.PositionComponent;

public class Mayor extends AbstractEntity {
    public Mayor() {
        //Components
        PositionComponent pc = add(new PositionComponent());
        //Systems
    }
}
