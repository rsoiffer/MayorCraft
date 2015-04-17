package gui;

import buildings.Building;
import buildings.BuildingType;
import core.*;
import game.ResourcesComponent;
import graphics.Graphics;
import units.SelectableComponent;
import units.SelectorComponent;
import world.GridComponent;

public class InterfaceSystem extends AbstractSystem {

    private ResourcesComponent rc;
    private SelectorComponent sc;
    private InterfaceComponent ic;

    public InterfaceSystem(ResourcesComponent rc, SelectorComponent sc, InterfaceComponent ic) {
        this.rc = rc;
        this.sc = sc;
        this.ic = ic;
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void update() {
        if (MouseInput.mouseScreen().y < 1030) {
            //Not on top bar
            if (ic.constructionMode) {
                if (MouseInput.mouseScreen().containedBy(new Vec2(1720, 1030 - BuildingType.values().length * 100), new Vec2(1920, 1030))) {
                    //On building bar
                    if (MouseInput.isReleased(0)) {
                        ic.buildingSelected = -1;
                        for (int i = 0; i < BuildingType.values().length; i++) {
                            if (MouseInput.mouseScreen().containedBy(new Vec2(1730, 950 - 100 * i), new Vec2(1910, 1020 - 100 * i))) {
                                ic.buildingSelected = i;
                            }
                        }
                    }
                    return;
                }
            }
            //Start dragging
            if (MouseInput.isPressed(0)) {
                sc.dragStart = MouseInput.mouse();
            }
            //While dragging
            if (MouseInput.isDown(0)) {
                if (sc.dragStart != null) {
                    if (MouseInput.getTime(0) > 10 && sc.dragStart.subtract(MouseInput.mouse()).lengthSquared() > 100) {
                        Graphics.fillRect(sc.dragStart, MouseInput.mouse().subtract(sc.dragStart), new Color4d(.2, .2, .8, .4));
                    }
                }
            }
            if (MouseInput.isReleased(0)) {
                if (sc.dragStart != null && MouseInput.getTime(0) > 10 && sc.dragStart.subtract(MouseInput.mouse()).lengthSquared() > 100) {
                    //Select by dragging
                    sc.selected.clear();
                    for (SelectableComponent s : sc.all) {
                        if (s.pc.pos.containedBy(sc.dragStart, MouseInput.mouse())) {
                            sc.selected.add(s);
                        }
                    }
                    if (sc.isUnitSelected()) {
                        for (int i = 0; i < sc.selected.size(); i++) {
                            if (sc.selected.get(i).dc == null) {
                                sc.selected.remove(i);
                                i--;
                            }
                        }
                    }
                    sc.dragStart = null;
                } else {
                    //Create building
                    if (ic.constructionMode && ic.buildingSelected >= 0) {
                        if (ic.selected().validPos() && ic.selected().enoughResources()) {
                            new Building(GridComponent.gridlock(MouseInput.mouse()), ic.selected());
                            ic.selected().spendResources();
                            ic.buildingSelected = -1;
                            return;
                        }
                    }
                    //Select unit
                    for (SelectableComponent s : sc.all) {
                        if (s.dc != null || !sc.isUnitSelected()) {
                            if (s.pc.pos.subtract(MouseInput.mouse()).lengthSquared() < s.size * s.size) {
                                sc.selected.clear();
                                sc.selected.add(s);
                                return;
                            }
                        }
                    }
                    //Move unit
                    for (SelectableComponent s : sc.selected) {
                        if (s.dc != null) {
                            s.dc.des = MouseInput.mouse();
                            s.dc.changed = true;
                        }
                    }
                }
            }
        } else {
            //On top bar
            if (MouseInput.isReleased(0)) {
                //Construction button
                if (MouseInput.mouseScreen().containedBy(new Vec2(1700, 1030), new Vec2(1800, 1080))) {
                    ic.constructionMode = !ic.constructionMode;
                }
                //Pause button
                if (MouseInput.mouseScreen().containedBy(new Vec2(1824, 1039), new Vec2(1856, 1071))) {
                    Main.paused = !Main.paused;
                }
                //Mute button
                if (MouseInput.mouseScreen().containedBy(new Vec2(1874, 1039), new Vec2(1906, 1071))) {
                    ic.muted = !ic.muted;
                    if (ic.muted) {
                        Sounds.stopAll();
                        Sounds.GLOBAL_VOLUME = 0;
                    } else {
                        Sounds.GLOBAL_VOLUME = 1;
                    }
                }
            }
        }
    }
}
