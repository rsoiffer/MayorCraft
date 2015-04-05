package units;

import core.AbstractSystem;
import core.Main;
import core.Vec2;
import movement.PositionComponent;
import movement.VelocityComponent;

public class CollisionSystem extends AbstractSystem {

    private PositionComponent pc;
    private VelocityComponent vc;
    private SelectableComponent sc;
    private DestinationComponent dc;

    public CollisionSystem(PositionComponent pc, VelocityComponent vc, SelectableComponent sc, DestinationComponent dc) {
        this.pc = pc;
        this.vc = vc;
        this.sc = sc;
        this.dc = dc;
    }

    @Override
    public void update() {
        for (SelectableComponent s : Main.gameManager.sc.all) {
            if (s != sc) {
                Vec2 diff = s.pc.pos.subtract(pc.pos);
                if (diff.lengthSquared() < (sc.size + s.size) * (sc.size + s.size)) {
                    Vec2 diffN = diff.normalize();
                    if (vc.vel.dot(diffN) >= 0) {
                        Vec2 change = diff.subtract(diffN.multiply(s.size + sc.size)).multiply(1 / (sc.invMass + s.invMass));
                        pc.pos = pc.pos.add(change.multiply(sc.invMass));
                        s.pc.pos = s.pc.pos.subtract(change.multiply(s.invMass));
//                    vc.vel = vc.vel.add(change);
//                    if (s.vc != null) {
//                        s.vc.vel = s.vc.vel.subtract(change);
//                    } else {
//                        vc.vel = vc.vel.add(change);
//                    }
                        vc.vel = vc.vel.subtract(diffN.multiply(vc.vel.dot(diffN)));
                        if (vc.vel.lengthSquared() < 1 && dc.des.subtract(pc.pos).lengthSquared() < 10000) {
                            dc.des = pc.pos;
                        }
                    }
                }
            }
        }
    }

}
