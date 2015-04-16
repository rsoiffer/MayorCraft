package gui;

import buildings.BuildingType;
import buildings.Building_2;
import core.*;
import graphics.Graphics;
import units.SelectableComponent;
import units.SelectorComponent;
import units.Unit;
import static world.GridComponent.SQUARE_SIZE;

public class InterfaceSystem extends AbstractSystem {

    private SelectorComponent sc;

    public InterfaceSystem(SelectorComponent sc) {
        this.sc = sc;
    }

    @Override
    public int getLayer() {
        return 2;
    }
    private Vec2 gridlock(Vec2 v){
        return new Vec2(v.x-v.x%SQUARE_SIZE+SQUARE_SIZE, v.y-v.y%SQUARE_SIZE);
    }
    private void buildRandom(){
               BuildingType buildingType=BuildingType.values()[(int) (Math.random() * BuildingType.values().length)];
                new Building_2(gridlock(MouseInput.mouse()), buildingType);
                new Unit(gridlock(MouseInput.mouse()).add(new Vec2(50,50)), buildingType);
    }
    private void build(BuildingType buildingType){
                new Building_2(gridlock(MouseInput.mouse()), buildingType);
                new Unit(gridlock(MouseInput.mouse()).add(new Vec2(50,50)), buildingType);
    }
     @Override
    public void update() {
        //if ((MouseInput.mouse().y - Main.gameManager.rmc.viewPos.y) / Main.gameManager.rmc.viewSize.y < .4537) {
        if (MouseInput.mouseScreen().y < 1030) {
            //Not on top bar
            if (MouseInput.isReleased(1)) {
             buildRandom();
            }   
            if (MouseInput.isPressed(0)) {
                sc.dragStart = MouseInput.mouse();
            }
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
                    sc.dragStart = null;
                } else {
                    //Select unit
                    for (SelectableComponent s : sc.all) {
                        if (s.pc.pos.subtract(MouseInput.mouse()).lengthSquared() < s.size * s.size) {
                            sc.selected.clear();
                            sc.selected.add(s);
                            return;
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
                if (MouseInput.mouseScreen().containedBy(new Vec2(1824, 1039), new Vec2(1856, 1071))) {
                    Main.paused = !Main.paused;
                }
        
            }
            
        }
    }
}
