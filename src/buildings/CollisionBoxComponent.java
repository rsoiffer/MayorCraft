package buildings;

import core.AbstractComponent;
import core.Main;
import core.Vec2;
import java.util.ArrayList;
import world.GridComponent;
import world.GridPoint;

public class CollisionBoxComponent extends AbstractComponent {

    public ArrayList<GridPoint> covered;

    public CollisionBoxComponent(Building b, Vec2 LL, Vec2 UR) {
        covered = new ArrayList();
        GridComponent gc = Main.gameManager.gc;
        for (int i = gc.get(LL).x; i <= gc.get(UR).x; i++) {
            for (int j = gc.get(LL).y; j <= gc.get(UR).y; j++) {
                GridPoint gp = gc.get(i, j);
                covered.add(gp);
                gp.blocked = true;
                gp.building = b;
            }
        }
    }

    @Override
    public void destroy() {
        for (GridPoint gp : covered) {
            gp.blocked = false;
            gp.building = null;
        }
    }
}
