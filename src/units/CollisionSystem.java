package units;

import core.AbstractSystem;
import core.Main;
import core.Vec2;
import movement.PositionComponent;
import movement.PreviousPositionComponent;
import movement.VelocityComponent;

public class CollisionSystem extends AbstractSystem {

    private PositionComponent pc;
    private PreviousPositionComponent ppc;
    private VelocityComponent vc;
    private SelectableComponent sc;
    private DestinationComponent dc;

    public CollisionSystem(PositionComponent pc, PreviousPositionComponent ppc, VelocityComponent vc, SelectableComponent sc, DestinationComponent dc) {
        this.pc = pc;
        this.ppc = ppc;
        this.vc = vc;
        this.sc = sc;
        this.dc = dc;
    }

    @Override
    protected boolean pauseable() {
        return true;
    }

    @Override
    public void update() {
        //Collide with units
        for (Unit u : Main.gameManager.elc.getList(Unit.class)) {
            SelectableComponent other = u.getComponent(SelectableComponent.class);
            if (other != sc) {
                Vec2 diff = other.pc.pos.subtract(pc.pos);
                if (diff.lengthSquared() < 3 * sc.size * sc.size) {//(sc.size + other.size) * (sc.size + other.size)) {
                    Vec2 diffN = diff.normalize();
                    if (vc.vel.dot(diffN) >= 0) {
                        Vec2 change = diff.subtract(diffN.multiply(other.size + sc.size)).multiply(.05);
                        pc.pos = pc.pos.add(change);
                        other.pc.pos = other.pc.pos.subtract(change);
                        vc.vel = vc.vel.subtract(diffN.multiply(vc.vel.dot(diffN)));
                    }
                }
            }
        }
        //Collide with walls
        if (!sc.open(pc.pos)) {
            Vec2 change = pc.pos.subtract(ppc.pos);
            pc.pos = ppc.pos;
            for (int i = 0; i < 10; i++) {
                if (sc.open(pc.pos.add(new Vec2(change.x * .1, 0)))) {
                    pc.pos = pc.pos.add(new Vec2(change.x * .1, 0));
                } else {
                    break;
                }
            }
            for (int i = 0; i < 10; i++) {
                if (sc.open(pc.pos.add(new Vec2(0, change.y * .1)))) {
                    pc.pos = pc.pos.add(new Vec2(0, change.y * .1));
                } else {
                    break;
                }
            }
            if (!sc.open(pc.pos)) {
                pc.pos = pc.pos.add(change);
            }
        }
        //Stop if near goal or can't move
        if (vc.vel.lengthSquared() < 1 && dc.des.subtract(pc.pos).lengthSquared() < 10000) {
            dc.atDest = true;
        }
    }
}
