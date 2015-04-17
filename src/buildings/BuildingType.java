package buildings;

import core.Color4d;
import core.Main;
import core.MouseInput;
import core.Vec2;
import units.SelectableComponent;
import world.GridComponent;
import world.GridPoint;

public enum BuildingType {

    FIRE_STATION(new Color4d(1, 0, 0), 100, 100),
    LUMBER_YARD(new Color4d(.6, .6, 0), 50, 0),
    MINE(new Color4d(.4, .4, .4), 50, 20),
    HOUSE(new Color4d(1, .8, .6), 50, 10),
    SCHOOL(new Color4d(.2, .2, .8), 200, 0);

    public final Color4d color;
    public final int materialsCost;
    public final int moneyCost;

    private BuildingType(Color4d color, int materialsCost, int moneyCost) {
        this.color = color;
        this.materialsCost = materialsCost;
        this.moneyCost = moneyCost;
    }

    public boolean enoughResources() {
        return materialsCost <= Main.gameManager.rc.materials && moneyCost <= Main.gameManager.rc.money;
    }

    public void spendResources() {
        Main.gameManager.rc.materials -= materialsCost;
        Main.gameManager.rc.money -= moneyCost;
    }

    @Override
    public String toString() {
        String r = "";
        for (String s : name().split("_")) {
            r += " " + s.substring(0, 1) + s.substring(1).toLowerCase();
        }
        return r.substring(1);
    }

    public boolean validPos() {
        Vec2 pos = GridComponent.gridlock(MouseInput.mouse());
        for (GridPoint gp : Main.gameManager.gc.points(pos.subtract(new Vec2(90, 90)), pos.add(new Vec2(90, 90)))) {
            if (gp.blocked || gp.onRiver) {
                return false;
            }
        }
        for (SelectableComponent sc : Main.gameManager.sc.all) {
            if (sc.dc != null && sc.pc.pos.subtract(new Vec2(sc.size, sc.size)).quadrant(pos.add(new Vec2(100, 100))) == 1
                    && sc.pc.pos.add(new Vec2(sc.size, sc.size)).quadrant(pos.subtract(new Vec2(100, 100))) == 3) {
                return false;
            }
        }
        return true;
    }
}
