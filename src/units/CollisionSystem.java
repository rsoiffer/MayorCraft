package units;

import core.AbstractSystem;
import core.Main;
import core.Vec2;
import movement.PositionComponent;
import movement.PreviousPositionComponent;
import movement.VelocityComponent;
import world.GridPoint;

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

    private boolean open(Vec2 pos) {
        return Main.gameManager.gc.open(new GridPoint(pos.subtract(new Vec2(sc.size, sc.size))), new GridPoint(pos.add(new Vec2(sc.size, sc.size))));
    }

    @Override
    public void update() {
        //Collide with units
        for (Unit u : Main.gameManager.elc.getList(Unit.class)) {
            SelectableComponent other = u.getComponent(SelectableComponent.class);
            if (other != sc) {
                Vec2 diff = other.pc.pos.subtract(pc.pos);
                if (diff.lengthSquared() < (sc.size + other.size) * (sc.size + other.size)) {
                    Vec2 diffN = diff.normalize();
                    if (vc.vel.dot(diffN) >= 0) {
                        Vec2 change = diff.subtract(diffN.multiply(other.size + sc.size)).multiply(.5);
                        pc.pos = pc.pos.add(change);
                        other.pc.pos = other.pc.pos.subtract(change);
                        vc.vel = vc.vel.subtract(diffN.multiply(vc.vel.dot(diffN)));
//                    vc.vel = vc.vel.add(change);
//                    if (s.vc != null) {
//                        s.vc.vel = s.vc.vel.subtract(change);
//                    } else {
//                        vc.vel = vc.vel.add(change);
//                    }
                    }
                }
            }
        }
        //Collide with walls
        if (!open(pc.pos)) {
            pc.pos = pc.pos.subtract(vc.vel);
            if (!open(pc.pos)) {
                pc.pos = ppc.pos;
            }
            int count = 0;
            while (open(pc.pos.add(vc.vel.multiply(.1))) && count++ < 10) {
                pc.pos = pc.pos.add(vc.vel.multiply(.1));
            }
        }
        //Stop if near goal or can't move
        if (vc.vel.lengthSquared() < 1 && dc.des.subtract(pc.pos).lengthSquared() < 10000) {
            dc.des = pc.pos;
        }
    }

}
