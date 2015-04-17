package units;

import buildings.Building;
import buildings.BuildingType;
import static buildings.BuildingType.*;
import buildings.BuildingTypeComponent;
import core.AbstractSystem;
import core.Main;
import core.Sounds;
import movement.PositionComponent;
import world.GridPoint;
import world.Terrain;
import static world.Terrain.ROCK;
import static world.Terrain.TREE;
import world.TerrainComponent;
import world.World;

public class UnitSystem extends AbstractSystem {

    private PositionComponent pc;
    private DestinationComponent dc;
    private BuildingTypeComponent btc;
    private GatheringComponent gc;
    private AnimationComponent ac;

    public UnitSystem(PositionComponent pc, DestinationComponent dc, BuildingTypeComponent btc, GatheringComponent gc, AnimationComponent ac) {
        this.pc = pc;
        this.dc = dc;
        this.btc = btc;
        this.gc = gc;
        this.ac = ac;
    }

    public void AI(BuildingType type) {
        if (btc.type != null && dc.building != null && dc.building.getComponent(BuildingTypeComponent.class).type == SCHOOL) {
            btc.type = SCHOOL;
            gc.carrying = false;
            ac.color = btc.type.color;
        }
        switch (type) {
            case FIRE_STATION:
                break;
            case LUMBER_YARD:
                gather(LUMBER_YARD, TREE);
                break;
            case MINE:
                gather(MINE, ROCK);
                break;
            case HOUSE:
                break;
            case SCHOOL:
                if (btc.type != null && dc.building != null) {
                    btc.type = dc.building.getComponent(BuildingTypeComponent.class).type;
                    gc.carrying = false;
                    ac.color = btc.type.color;
                }
                break;
        }
    }

    public void gather(BuildingType type, Terrain terrain) {
        if (dc.building != null && dc.building.getComponent(BuildingTypeComponent.class).type == type) {
            if (gc.carrying && gc.timeRemaining == 0) {
                gc.carrying = false;
                Main.gameManager.rc.materials += 10;
            }
            dc.terrain = nearest(terrain);
            if (dc.terrain != null) {
                dc.des = dc.terrain.toVec2();
                dc.changed = true;
            }
        } else if (dc.terrain != null) {
            if (dc.terrain.terrain == null) {
                dc.terrain = nearest(terrain);
                if (dc.terrain != null) {
                    dc.des = dc.terrain.toVec2();
                    dc.changed = true;
                }
            }
            if (dc.terrain.terrain == terrain) {
                if (!gc.carrying) {
                    gc.timeRemaining = 60;
                    gc.carrying = true;
                } else {
                    if (gc.timeRemaining > 0) {
                        gc.timeRemaining--;
                        if (gc.timeRemaining % 30 == 25) {
                            Sounds.playSound("metal_box_1.wav", false, Math.min(1, 500. / pc.pos.subtract(Main.gameManager.rmc.viewPos).length()));
                        }
                        if (gc.timeRemaining == 0) {
                            dc.terrain.terrain = null;
                            dc.terrain.blocked = false;
                            Main.gameManager.elc.get(World.class).getComponent(TerrainComponent.class).terrainMap.get(terrain).remove(dc.terrain);
                            dc.terrain = null;
                            dc.building = nearest(type);
                            if (dc.building != null) {
                                dc.des = dc.building.getComponent(PositionComponent.class).pos;
                                dc.changed = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public Building nearest(BuildingType type) {
        Building r = null;
        for (Building b : Main.gameManager.elc.getList(Building.class)) {
            if (b.getComponent(BuildingTypeComponent.class).type == type) {
                if ((r == null && b.getComponent(PositionComponent.class).pos.subtract(pc.pos).lengthSquared() < 1200000)
                        || (r != null && b.getComponent(PositionComponent.class).pos.subtract(pc.pos).lengthSquared() < r.getComponent(PositionComponent.class).pos.subtract(pc.pos).lengthSquared())) {
                    r = b;
                }
            }
        }
        return r;
    }

    public GridPoint nearest(Terrain t) {
        GridPoint r = null;
        for (GridPoint gp : Main.gameManager.elc.get(World.class).getComponent(TerrainComponent.class).terrainMap.get(t)) {
            if ((r == null && gp.toVec2().subtract(pc.pos).lengthSquared() < 1000000)
                    || (r != null && gp.toVec2().subtract(pc.pos).lengthSquared() < r.toVec2().subtract(pc.pos).lengthSquared())) {
                r = gp;
            }
        }
        return r;
    }

    @Override
    public void update() {
        if (dc.atDest) {
            if (btc.type == null) {
                for (BuildingType type : BuildingType.values()) {
                    AI(type);
                }
            } else {
                AI(btc.type);
            }
        }
    }
}
